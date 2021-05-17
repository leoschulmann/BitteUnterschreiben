package com.leoschulmann.podpishiplz.worker;

import com.leoschulmann.podpishiplz.controller.EventController;
import com.leoschulmann.podpishiplz.controller.EventType;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;

public class OpeningWorkerListener implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingWorker task = (SwingWorker) evt.getSource();

        if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
            try {
                task.get();
                EventController.notify(EventType.OPEN_WORKER_FINISHED, null);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
