package com.assistantvocal.service;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class LauncherService {
    public void searchWeb(String query) {
        if (query == null || query.isBlank()) {
            System.out.println("Search query cannot be empty.");
            return;
        }
        try {
            String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
            URI uri = new URI("https://www.google.com/search?q=" + encoded);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(uri);
                System.out.println("Opened browser search for: " + query);
            } else {
                System.out.println("Desktop browsing is not supported on this platform.");
            }
        } catch (Exception e) {
            System.err.println("Unable to perform search: " + e.getMessage());
        }
    }

    public void openApp(String command) {
        if (command == null || command.isBlank()) {
            System.out.println("Application command cannot be empty.");
            return;
        }
        try {
            Runtime.getRuntime().exec(command);
            System.out.println("Trying to launch: " + command);
        } catch (IOException e) {
            System.err.println("Unable to open application: " + e.getMessage());
        }
    }
}
