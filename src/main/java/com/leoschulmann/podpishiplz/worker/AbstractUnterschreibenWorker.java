package com.leoschulmann.podpishiplz.worker;

import javax.swing.*;

public abstract class AbstractUnterschreibenWorker extends SwingWorker<Object, Object> {
    @Override
    abstract protected Object doInBackground();

    protected final WorkerDialog diag;

    public AbstractUnterschreibenWorker(JFrame owner, String text) {
        this.diag = new WorkerDialog(owner, text);
    }

    public void runDialog() {
        diag.setVisible(true);
    }
}
