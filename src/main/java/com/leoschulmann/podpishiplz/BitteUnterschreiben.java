package com.leoschulmann.podpishiplz;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.leoschulmann.podpishiplz.controller.*;
import com.leoschulmann.podpishiplz.view.AppWindow;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

@Slf4j
public class BitteUnterschreiben {
    static AppWindow app;

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {

        //default behavior: only write errors to err.log
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        Appender<ILoggingEvent> consoleLogger = root.getAppender("STDOUT");
        root.detachAppender(consoleLogger);
        root.setLevel(Level.WARN);

        //'-v' argument enables 'verbose' and writes all debug and info messages to console
        if (args.length > 0 && args[0].equals("-v")) {
            root.setLevel(Level.DEBUG);
            root.addAppender(consoleLogger);
            log.info("Console logging enabled (-v)");
        }
        SwingUtilities.invokeAndWait(() -> app = new AppWindow());
        init();
    }

    private static void init() {
        DocumentController.createDocument();
        DocumentController.initListener();
        OverlaysPanelController.initListener();
        TopPanelController.initListener();
        MainPanelController.initListener();
        MenuBarController.initListener();
        try {
            SettingsController.initSettings();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(app, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        Locale.setDefault(new Locale(SettingsController.getLanguage()));

        EventController.notify(EventType.LOCALE_CHANGED, null);
        EventController.notify(EventType.REFRESH_OVERLAYS_PANEL, null);
        EventController.notify(EventType.MAIN_PANEL_EMPTY, null);
        EventController.notify(EventType.NO_PAGES_IN_DOCUMENT, null);
    }

    public static AppWindow getApp() {
        return app;
    }
}
