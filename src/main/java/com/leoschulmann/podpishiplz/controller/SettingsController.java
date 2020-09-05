package com.leoschulmann.podpishiplz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.model.Settings;
import com.leoschulmann.podpishiplz.view.SettingsDialogue;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SettingsController {
    private static SettingsDialogue settingsDialogue;
    private static final File settingsFile = new File("./settings.yml");
    private static Settings settings;

    public static int getBlendingMode() {
        return settings.getBlendingMode();
    }

    public static void setBlendingMode(int blendingMode) {
        settings.setBlendingMode(blendingMode);
    }

    public static float getJpegQuality() {
        return settings.getJpgQuality();
    }

    public static void setJpegQuality(float jpegQuality) {
        settings.setJpgQuality(jpegQuality);
    }

    public static float getResolutionMultiplier() {
        return settings.getResolution();
    }

    public static void setResolutionMultiplier(float resolutionMultiplier) {
        settings.setResolution(resolutionMultiplier);
    }

    public static void openSettings() {
        if (settingsDialogue == null) {
            settingsDialogue = new SettingsDialogue(BitteUnterschreiben.getApp(),
                    settings.getBlendingMode(),
                    settings.getJpgQuality(),
                    settings.getResolution()
            );
        }
        //update sliders and radios if the .yml was modified externally
        settingsDialogue.setBlendingMode(settings.getBlendingMode());
        settingsDialogue.setJpgQuality(settings.getJpgQuality());
        settingsDialogue.setResolution(settings.getResolution());
        settingsDialogue.setVisible(true);
    }

    public static void initSettings() throws IOException {
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        if (!settingsFile.exists()) {
            // default values 'Darken', 50% quality, 200 ppi downsampling
            settings = new Settings(1, 0.5f, 0.6666667f);
            settingsFile.createNewFile();
            om.writeValue(settingsFile, settings);
        } else {
            settings = om.readValue(settingsFile, Settings.class);
        }

        EventListener settingsEventListener = (event, object) -> {
            switch (event) {
                case OVERLAY_LOADED_FROM_DISK:
                    addToUsedOverlays((File) object);
                    break;
            }
        };
        EventController.subscribe(EventType.OVERLAY_LOADED_FROM_DISK, settingsEventListener);
    }

    public static void saveYML() throws IOException {
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        om.writeValue(settingsFile, settings);
    }

    public static void addToUsedOverlays(File file) {
        settings.getUsedOverlays().compute(file, (file1, integer) -> {
            if (integer == null) return 1;
            return integer + 1;
        });
        try {
            saveYML();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<File> getUsedOverlays() {
        return settings.getUsedOverlays()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static void removeOverlayFromList(File file) {
        settings.getUsedOverlays().remove(file);
    }
}
