package com.jennyyn.recommender.service;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    private FileService fileService;
    private static final String SESSION_FILE = "session.txt";

    @BeforeEach
    void setUp() throws Exception {
        fileService = new FileService();

        // Delete session file before each test
        Files.deleteIfExists(Path.of(SESSION_FILE));
    }

    @Test
    void testSaveSessionCreatesCorrectFormat() throws Exception {
        fileService.saveSession("hello", "hi");

        String content = Files.readString(Path.of(SESSION_FILE));

        assertTrue(content.contains("---SESSION START---"));
        assertTrue(content.contains("hello"));
        assertTrue(content.contains("----"));
        assertTrue(content.contains("hi"));
        assertTrue(content.contains("---SESSION END---"));
    }

    @Test
    void testSaveSessionAppendsMultipleSessions() throws Exception {
        fileService.saveSession("A", "B");
        fileService.saveSession("C", "D");

        String content = Files.readString(Path.of(SESSION_FILE));

        assertEquals(2, content.split("---SESSION START---").length - 1);
    }

    @Test
    void testLoadSessionReadsCorrectly() {
        fileService.saveSession("line1\nline2", "rewrite");

        List<String[]> sessions = fileService.loadSession();

        assertEquals(1, sessions.size());
        assertEquals("line1\nline2\n", sessions.get(0)[0]);
        assertEquals("rewrite\n", sessions.get(0)[1]);
    }

    @Test
    void testLoadSessionReturnsEmptyWhenNoFile() {
        List<String[]> sessions = fileService.loadSession();
        assertTrue(sessions.isEmpty());
    }

    @Test
    void testDeleteSessionRemovesCorrectIndex() {
        fileService.saveSession("A", "B");
        fileService.saveSession("C", "D");
        fileService.saveSession("E", "F");

        fileService.deleteSession(1);

        List<String[]> sessions = fileService.loadSession();

        assertEquals(2, sessions.size());
        assertEquals("A\n", sessions.get(0)[0]);  // index 0 remains
        assertEquals("E\n", sessions.get(1)[0]);  // original index 2 shifted
    }

    @Test
    void testDeleteSessionInvalidIndexDoesNothing() {
        fileService.saveSession("A", "B");

        fileService.deleteSession(999); // invalid

        List<String[]> sessions = fileService.loadSession();

        assertEquals(1, sessions.size());
    }
}

