package com.felixstudio.automationcontrol.security;

import java.util.regex.Pattern;


public class FileSecurity {
    private static final Pattern SAFE_FILENAME = Pattern.compile("^[a-zA-Z0-9._-]+$");
    public static String sanitizeFileName(String fileName) {
        if (fileName == null || !SAFE_FILENAME.matcher(fileName).matches()) {
            throw new IllegalArgumentException("Illegal filename: " + fileName);
        }
        return fileName;
    }
}
