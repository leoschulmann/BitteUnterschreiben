package com.leoschulmann.podpishiplz.controller;

import lombok.SneakyThrows;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;

public class DnDListener implements DropTargetListener {
        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
            EventController.notify(EventType.DRAG_ENTER_EVENT, null);
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {

        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {

        }

        @Override
        public void dragExit(DropTargetEvent dte) {
            EventController.notify(EventType.DRAG_EXIT_EVENT, null);
        }

        @SneakyThrows
        @Override
        public void drop(DropTargetDropEvent dtde)  {
            dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

            Transferable t = dtde.getTransferable();

            List<File> l = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
            File file = l.get(0);
            String ext = file.getName().substring(file.getName().lastIndexOf(".")).toLowerCase();
            if (ext.equals(".pdf")) {
                EventController.notify(EventType.DROP_EVENT, file.getAbsolutePath());
            }
        }
    }
