package com.leoschulmann.podpishiplz.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Settings {
    private int blendingMode;
    private float jpgQuality;
    private float resolution;
    private Map<File, Integer> usedOverlays;

    public Settings(int blendingMode, float jpgQuality, float resolution) {
              /*
            0: no-op blending mode
            1: Darken
            2: Multiply
             */
        this.blendingMode = blendingMode;
        this.jpgQuality = jpgQuality;
        this.resolution = resolution;
        this.usedOverlays = new HashMap<>();
    }

    public Settings() {
    }

    public int getBlendingMode() {
        return blendingMode;
    }

    public void setBlendingMode(int blendingMode) {
        this.blendingMode = blendingMode;
    }

    public float getJpgQuality() {
        return jpgQuality;
    }

    public void setJpgQuality(float jpgQuality) {
        this.jpgQuality = jpgQuality;
    }

    public float getResolution() {
        return resolution;
    }

    public void setResolution(float resolution) {
        this.resolution = resolution;
    }

    public Map<File, Integer> getUsedOverlays() {
        return usedOverlays;
    }
}