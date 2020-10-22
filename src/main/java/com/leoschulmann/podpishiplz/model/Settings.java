package com.leoschulmann.podpishiplz.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Settings {
    private int blendingMode;
    private float jpgQuality;
    private float resolution;
    private String producer;
    private String creator;
    private boolean producerOverridden;
    private float zoomSpeed;
    private boolean invertZoom;
    private String selectionColor;
    private int maxOverlays;
    private String language;

    private Map<File, Integer> usedOverlays;

    public Settings() {
        this.blendingMode = 1;  //0: no-op blending mode        1: Darken         2: Multiply
        this.jpgQuality = 0.5f;
        this.resolution = 0.6666667f;
        this.usedOverlays = new HashMap<>();
        this.language = "en";
        this.maxOverlays = 10;
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

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public boolean isProducerOverridden() {
        return producerOverridden;
    }

    public void setProducerOverridden(boolean producerOverridden) {
        this.producerOverridden = producerOverridden;
    }

    public float getZoomSpeed() {
        return zoomSpeed;
    }

    public void setZoomSpeed(float zoomSpeed) {
        this.zoomSpeed = zoomSpeed;
    }

    public boolean isInvertZoom() {
        return invertZoom;
    }

    public void setInvertZoom(boolean invertZoom) {
        this.invertZoom = invertZoom;
    }

    public String getSelectionColor() {
        return selectionColor;
    }

    public void setSelectionColor(String selectionColor) {
        this.selectionColor = selectionColor;
    }

    public int getMaxOverlays() {
        return maxOverlays;
    }

    public void setMaxOverlays(int maxOverlays) {
        this.maxOverlays = maxOverlays;
    }

    public void setLanguage(String lang) {
        this.language = lang;
    }

    public String getLanguage() {
        return language;
    }
}