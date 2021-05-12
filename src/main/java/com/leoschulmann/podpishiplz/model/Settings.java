package com.leoschulmann.podpishiplz.model;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
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

    private final Map<File, Integer> usedOverlays;

    public Settings() {
        this.blendingMode = 1;  //0: no-op blending mode        1: Darken         2: Multiply
        this.jpgQuality = 0.5f;
        this.resolution = 0.6666667f;
        this.usedOverlays = new HashMap<>();
        this.language = "en";
        this.maxOverlays = 10;
    }
}