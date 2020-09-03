package com.leoschulmann.podpishiplz.model;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    private int blendingMode;
    private float jpgQuality;
    private float resolution;
    private List<FrequentFile> topFiles;

    public Settings(int blendingMode, float jpgQuality, float resolution) {
              /*
            0: no-op blending mode
            1: Darken
            2: Multiply
             */
        this.blendingMode = blendingMode;
        this.jpgQuality = jpgQuality;
        this.resolution = resolution;
        topFiles = new ArrayList<>();
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

    public List<FrequentFile> getTopFiles() {
        return topFiles;
    }

    public void setTopFiles(List<FrequentFile> topFiles) {
        this.topFiles = topFiles;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "blendingMode=" + blendingMode +
                ", jpgQuality=" + jpgQuality +
                ", resolution=" + resolution +
                '}';
    }
}

class FrequentFile {    //todo implement
    String path;
    int count;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}