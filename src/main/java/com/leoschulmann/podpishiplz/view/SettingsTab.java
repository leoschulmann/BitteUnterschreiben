package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.EventListener;

public interface SettingsTab extends EventListener {
    void init();
    void saveState();
    String getTitle();
}
