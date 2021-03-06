package com.leoschulmann.podpishiplz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.graphics.BlenderDarken;
import com.leoschulmann.podpishiplz.graphics.BlenderMultiply;
import com.leoschulmann.podpishiplz.model.Settings;
import com.leoschulmann.podpishiplz.view.SettingsDialogue;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class SettingsController {
    public static final String DEFAULT_COLOR = "00ff00";  //default is green
    private static final int DEFAULT_MAX_OVERLAYS = 20;  //default is 20
    private static SettingsDialogue settingsDialogue;
    private static File settingsFile;
    private static Settings settings;

    public static int getBlendingMode() {
        return settings.getBlendingMode();
    }

    static Class<? extends CompositeContext> getBlender() {
        Class<? extends CompositeContext> blender = null;
        switch (getBlendingMode()) {
            case (1):
                blender = BlenderDarken.class;
                break;
            case (2):
                blender = BlenderMultiply.class;
                break;
            default:
        }
        return blender;
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

    static void openSettings() {
        if (settingsDialogue == null) {
            settingsDialogue = new SettingsDialogue(BitteUnterschreiben.getApp());
        }
        settingsDialogue.setVisible(true);
    }

    public static void initSettings() throws IOException {
        if (System.getProperty("os.name").contains("Mac")) {
            settingsFile = new File(System.getProperty("user.home") +
                    "/Library/Application Support/BitteUnterschreiben/settings.yml");
        } else if (System.getProperty("os.name").contains("Windows")) {
            settingsFile = new File(System.getenv("APPDATA") + "/BitteUnterschreiben/settings.yml");
        }
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        if (!settingsFile.exists()) {
            log.info("{} file not found.", settingsFile.getName());
            settingsFile.getParentFile().mkdirs();
            settingsFile.createNewFile();
            createNewSettingsYML(om);
        } else {
            log.info("File {} exists, reading settings."
                    , settingsFile.getName());
            try {
                settings = om.readValue(settingsFile, Settings.class);
            } catch (IOException e) {
                log.warn("Exception during file deserialization");
                log.warn(e.getMessage());
                createNewSettingsYML(om);
            }
        }

        EventListener settingsEventListener = (event, object) -> {
            if (event == EventType.OVERLAY_LOADED_FROM_DISK) {
                addToUsedOverlays((File) object);
            }
        };
        EventController.subscribe(EventType.OVERLAY_LOADED_FROM_DISK, settingsEventListener);
    }

    private static void createNewSettingsYML(ObjectMapper om) throws IOException {
        // default values 'Darken', 50% quality, 200 ppi downsampling
        settings = new Settings();
        om.writeValue(settingsFile, settings);
        log.info("New file with default settings created.");
    }

    public static void saveYML() throws IOException {
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        om.writeValue(settingsFile, settings);
        log.info("{} file saved.", settingsFile.getName());
    }

    private static void addToUsedOverlays(File file) {
        settings.getUsedOverlays().compute(file, (file1, integer) -> {
            if (integer == null) {
                log.debug("{} added to used overlays list first time.", file.getName());
                return 1;
            }
            int counter = integer + 1;
            log.debug("{} added to used overlays list {} time.", file.getName(), counter);
            return counter;
        });
        try {
            saveYML();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<File> getUsedOverlays() {
        return settings.getUsedOverlays()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(getMaxOverlays())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static void removeOverlayFromList(File file) {
        log.debug("Removing overlay from list {}.", file.getName());

        settings.getUsedOverlays().remove(file);
        try {
            saveYML();
            log.debug("Saving {} overlays", settings.getUsedOverlays().keySet().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            log.error(e.getMessage(), e);
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

    public static int getMaxOverlays() {
        int i = settings.getMaxOverlays();
        if (i < 1) return DEFAULT_MAX_OVERLAYS;
        else return i;
    }

    public static void setMaxOverlays(int maxOverlays) {
        settings.setMaxOverlays(maxOverlays);
    }

    public static boolean colorVerify(String text) {
        try {
            Color.decode('#' + text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void setLanguage(String lang) {
        Locale.setDefault(new Locale(lang));
        settings.setLanguage(lang);
        EventController.notify(EventType.LOCALE_CHANGED, null);
    }

    public static String getLanguage() {
        if (settings.getLanguage() == null) return "en";
        else return settings.getLanguage();
    }
}
