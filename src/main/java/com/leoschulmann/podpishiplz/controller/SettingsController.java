package com.leoschulmann.podpishiplz.controller;

public class SettingsController {
    /*
    0: no-op blending mode
    1: Darken
    2: Multiply
     */
    private static int blendingMode = 1;  //todo rewrite to .yml properties file
    private static float jpegQuality = 0.6f;
    private static float resolutionMultiplier = 0.667f;  // 1.0 == 300 dpi

    public static int getBlendingMode() {
        return blendingMode;
    }

    public static void setBlendingMode(int blendingMode) {
        SettingsController.blendingMode = blendingMode;
    }

    public static float getJpegQuality() {
        return jpegQuality;
    }

    public static void setJpegQuality(float jpegQuality) {
        SettingsController.jpegQuality = jpegQuality;
    }

    public static float getResolutionMultiplier() {
        return resolutionMultiplier;
    }

    public static void setResolutionMultiplier(float resolutionMultiplier) {
        SettingsController.resolutionMultiplier = resolutionMultiplier;
    }
}
