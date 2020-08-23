package com.leoschulmann.podpishiplz.controller;

public class SettingsController {
    /*
    0: no-op blending mode
    1: Darken
    2: Multiply
     */
    private static int blendingMode = 1;  //todo rewrite to .yml properties file

    public static int getBlendingMode() {
        return blendingMode;
    }

    public static void setBlendingMode(int blendingMode) {
        SettingsController.blendingMode = blendingMode;
    }
}
