package com.ashot.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScreenshotAttachmentUtils {

    private static final String BASELINES_DIR = "baselines";
    private static final String SCREENSHOTS_DIR = "screenshots";
    private static final String DIFF_DIR = "diff";

    public static FileInputStream getBaselineScreenshot(String name) throws IOException {
        Path path = Paths.get(System.getProperty("user.dir"), BASELINES_DIR, name + ".png");
        if (!path.toFile().exists()) {
            throw new IOException("Baseline screenshot not found: " + path);
        }
        return new FileInputStream(path.toFile());
    }

    public static FileInputStream getCurrentScreenshot(String name) throws IOException {
        Path path = Paths.get(System.getProperty("user.dir"), SCREENSHOTS_DIR, name + ".png");
        if (!path.toFile().exists()) {
            throw new IOException("Current screenshot not found: " + path);
        }
        return new FileInputStream(path.toFile());
    }

    public static FileInputStream getDiffScreenshot(String name) throws IOException {
        Path path = Paths.get(System.getProperty("user.dir"), DIFF_DIR, name + ".png");
        if (!path.toFile().exists()) {
            throw new IOException("Diff screenshot not found: " + path);
        }
        return new FileInputStream(path.toFile());
    }
}
