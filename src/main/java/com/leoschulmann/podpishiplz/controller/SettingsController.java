package com.leoschulmann.podpishiplz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.model.Settings;
import com.leoschulmann.podpishiplz.view.SettingsDialogue;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class SettingsController {
    public static final String DEFAULT_COLOR = "00ff00";  //default is green
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
            settingsDialogue = new SettingsDialogue(BitteUnterschreiben.getApp());
        }
        settingsDialogue.setVisible(true);
    }

    public static void initSettings() throws IOException {
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        if (settingsFile.createNewFile()) {
            LoggerFactory.getLogger(SettingsController.class).info("{} file not found.", settingsFile.getName());
            // default values 'Darken', 50% quality, 200 ppi downsampling
            settings = new Settings(1, 0.5f, 0.6666667f);
            om.writeValue(settingsFile, settings);
            LoggerFactory.getLogger(SettingsController.class).info("New file with default settings created.");
        } else {
            LoggerFactory.getLogger(SettingsController.class).info("File {} exists, reading settings."
                    , settingsFile.getName());
            settings = om.readValue(settingsFile, Settings.class);
        }

        EventListener settingsEventListener = (event, object) -> {
            if (event == EventType.OVERLAY_LOADED_FROM_DISK) {
                addToUsedOverlays((File) object);
            }
        };
        EventController.subscribe(EventType.OVERLAY_LOADED_FROM_DISK, settingsEventListener);
    }

    public static void saveYML() throws IOException {
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        om.writeValue(settingsFile, settings);
        LoggerFactory.getLogger(SettingsController.class).info("{} file saved.", settingsFile.getName());
    }

    public static void addToUsedOverlays(File file) {
        settings.getUsedOverlays().compute(file, (file1, integer) -> {
            if (integer == null) {
                LoggerFactory.getLogger(SettingsController.class)
                        .debug("{} added to used overlays list first time.", file.getName());
                return 1;
            }
            int counter = integer + 1;
            LoggerFactory.getLogger(SettingsController.class)
                    .debug("{} added to used overlays list {} time.", file.getName(), counter);
            return counter;
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
        LoggerFactory.getLogger(SettingsController.class)
                .debug("Removing overlay from list {}.", file.getName());

        settings.getUsedOverlays().remove(file);
        EventController.notify(EventType.REFRESH_OVERLAYS_PANEL, null);
    }

    public static void setCreator(String creator) {
        settings.setCreator(creator);
    }

    public static String getCreator() {
        return settings.getCreator();
    }

    public static String getDefaultProducer() {
        Properties prop = new Properties();
        String producer = "BitteUnterschreiben";
        try {
            prop.load(PDFController.class.getClassLoader().getResourceAsStream("META-INF/app.properties"));
            if (prop.getProperty("app.version") != null) {
                producer = producer + " v" + prop.getProperty("app.version");
            }
        } catch (IOException e) {
            LoggerFactory.getLogger(SettingsController.class).error(e.getMessage(), e);
        }
        return producer;
    }

    public static void setProducerOverride(boolean b) {
        settings.setProducerOverridden(b);
    }

    public static boolean isProducerOverride() {
        return settings.isProducerOverridden();
    }

    public static void setProducer(String producer) {
        settings.setProducer(producer);
    }

    public static String getProducer() {
        return settings.getProducer();
    }

    public static float getZoomSpeed() {
        float f = settings.getZoomSpeed();
        if (f < 1.01 || f > 1.1) f = 1.01f;
        return f;
    }

    public static void setZoomSpeed(float speed) {
        settings.setZoomSpeed(speed);
    }

    public static boolean isInvertZoom() {
        return settings.isInvertZoom();
    }

    public static void setInvertZoom(boolean b) {
        settings.setInvertZoom(b);
    }

    public static String getSelectionColor() {
        if (colorVerify(settings.getSelectionColor())) {
            return settings.getSelectionColor();
        } else return DEFAULT_COLOR;
    }

    public static void setSelectionColor(String color) {
        settings.setSelectionColor(color);
    }

    public static boolean colorVerify(String text) {
        try {
            Color.decode('#' + text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
