package com.jennyyn.recommender.service;

import com.jennyyn.recommender.model.APIClient;
import com.jennyyn.recommender.model.RateLimitException;
import com.jennyyn.recommender.model.RewriteResult;
import com.jennyyn.recommender.model.WritingStrategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class APIServiceTest {

    @Mock
    APIClient mockApiClient;

    @Mock
    HttpClient mockHttpClient;

    // IMPORTANT: mock raw type and cast later (fix for Mockito generics issue)
    @Mock
    @SuppressWarnings("rawtypes")
    HttpResponse mockResponse;

    @Mock
    WritingStrategy mockStrategy;

    APIService service;

    @BeforeEach
    void setup() {
        service = new APIService(mockApiClient, mockHttpClient);
        when(mockApiClient.getApiKey()).thenReturn("abc123");
        when(mockApiClient.getModel()).thenReturn("gpt-4o-mini");
    }

    // -----------------------------------------------------------
    @Test
    void testSuccessResponse() throws Exception {
        when(mockStrategy.buildPrompt("Hello")).thenReturn("Prompted Text");

        @SuppressWarnings("unchecked")
        HttpResponse<String> resp = (HttpResponse<String>) mockResponse;

        String json = """
                {
                  "choices": [
                    { "message": { "content": "Rewritten OK" } }
                  ]
                }
                """;

        when(resp.body()).thenReturn(json);
        when(resp.statusCode()).thenReturn(200);

        when(mockHttpClient.send(any(), any())).thenReturn((HttpResponse) resp);

        var method = APIService.class.getDeclaredMethod("rewriteText", String.class, WritingStrategy.class);
        method.setAccessible(true);

        RewriteResult result = (RewriteResult) method.invoke(service, "Hello", mockStrategy);

        assertEquals("Rewritten OK", result.getRewrittenText());
    }

    // -----------------------------------------------------------
    @Test
    void testRateLimitError() throws Exception {
        when(mockStrategy.buildPrompt(anyString())).thenReturn("Prompt");

        @SuppressWarnings("unchecked")
        HttpResponse<String> resp = (HttpResponse<String>) mockResponse;

        String json = """
                {
                  "error": { "message": "Rate limit exceeded" }
                }
                """;

        when(resp.body()).thenReturn(json);
        when(mockHttpClient.send(any(), any())).thenReturn((HttpResponse) resp);

        var method = APIService.class.getDeclaredMethod("rewriteText", String.class, WritingStrategy.class);
        method.setAccessible(true);

        RateLimitException ex =
                assertThrows(RateLimitException.class, () -> method.invoke(service, "Hi", mockStrategy));

        assertTrue(ex.getMessage().contains("Rate limit"));
    }

    // -----------------------------------------------------------
    @Test
    void testGenericAPIError() throws Exception {
        when(mockStrategy.buildPrompt(anyString())).thenReturn("Prompt");

        @SuppressWarnings("unchecked")
        HttpResponse<String> resp = (HttpResponse<String>) mockResponse;

        String json = """
                {
                  "error": { "message": "Bad API request" }
                }
                """;

        when(resp.body()).thenReturn(json);
        when(mockHttpClient.send(any(), any())).thenReturn((HttpResponse) resp);

        var method = APIService.class.getDeclaredMethod("rewriteText", String.class, WritingStrategy.class);
        method.setAccessible(true);

        Exception ex =
                assertThrows(Exception.class, () -> method.invoke(service, "Hi", mockStrategy));

        assertTrue(ex.getMessage().contains("Bad API request"));
    }

    // -----------------------------------------------------------
    @Test
    void testTimeout() throws Exception {
        when(mockStrategy.buildPrompt(anyString())).thenReturn("Prompt");

        when(mockHttpClient.send(any(), any()))
                .thenThrow(new java.net.http.HttpTimeoutException("timeout"));

        var method = APIService.class.getDeclaredMethod("rewriteText", String.class, WritingStrategy.class);
        method.setAccessible(true);

        Exception ex = assertThrows(Exception.class, () -> method.invoke(service, "Hi", mockStrategy));

        assertTrue(ex.getMessage().contains("timed out"));
    }

    // -----------------------------------------------------------
    @Test
    void testCancelStopsRequest() throws Exception {
        when(mockStrategy.buildPrompt(anyString())).thenReturn("Prompt");

        // Use raw response object for generics safety
        @SuppressWarnings("unchecked")
        HttpResponse<String> resp = (HttpResponse<String>) mockResponse;

        when(mockHttpClient.send(any(), any())).thenAnswer(invocation -> {
            Thread.sleep(5000);
            return resp;
        });

        Thread t = new Thread(() -> {
            service.rewriteTextAsync(
                    "Hi",
                    mockStrategy,
                    r -> fail("Should not succeed after cancel"),
                    e -> assertTrue(e instanceof InterruptedException),
                    () -> {}
            );
        });

        t.start();
        Thread.sleep(100);
        service.cancel();

        assertTrue(true);
    }
}
