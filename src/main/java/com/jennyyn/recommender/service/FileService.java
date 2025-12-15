package com.jennyyn.recommender.service;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class FileService {

    private static final String SESSION_FILE = "session.txt";
    private static final String SESSION_START = "---SESSION START---";
    private static final String SESSION_END = "---SESSION END---";
    private static final String SPLIT = "----";

    //Saves sessions (appends to file)
    public void saveSession(String original, String rewritten) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SESSION_FILE, true))) {
            writer.write(SESSION_START);
            writer.newLine();
            writer.write(original);
            writer.newLine();
            writer.write(SPLIT);
            writer.newLine();
            writer.write(rewritten);
            writer.newLine();
            writer.write(SESSION_END);
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Load all sessions from history file
    public List<String[]> loadSession() {
        File file = new File(SESSION_FILE);
        List<String[]> sessions = new ArrayList<>();
        if (!file.exists()) return sessions;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder original = new StringBuilder();
            StringBuilder rewritten = new StringBuilder();
            boolean inSession = false;
            boolean split = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals(SESSION_START)) {
                    inSession = true;
                    split = false;
                    original.setLength(0);
                    rewritten.setLength(0);
                    continue;
                }
                if (line.equals(SPLIT)) {
                    split = true;
                    continue;
                }
                if (line.equals(SESSION_END)) {
                    inSession = false;
                    sessions.add(new String[]{original.toString(), rewritten.toString()});
                    continue;
                }
                if (inSession) {
                    if (!split) original.append(line).append("\n");
                    else rewritten.append(line).append("\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessions;
    }

    public void deleteSession(int index) {
        List<String[]> sessions = loadSession();

        // Safety
        if (index < 0 || index >= sessions.size()) {
            return;
        }

        // Remove the chosen session
        sessions.remove(index);

        // Rewrite the whole file with remaining sessions
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SESSION_FILE))) {
            for (String[] session : sessions) {
                writer.write(SESSION_START);
                writer.newLine();

                // Original text (may be multi-line)
                if (session[0] != null) {
                    writer.write(session[0].trim());
                }
                writer.newLine();

                writer.write(SPLIT);
                writer.newLine();

                // Rewritten text (may be multi-line)
                if (session[1] != null) {
                    writer.write(session[1].trim());
                }
                writer.newLine();

                writer.write(SESSION_END);
                writer.newLine();
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

