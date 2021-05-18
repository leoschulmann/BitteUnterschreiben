package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.graphics.Resizer;
import com.leoschulmann.podpishiplz.model.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
public class PDFController {
    static BufferedImage[] generatePageThumbnails(PDDocument pdDocument, boolean[] selectedPages) {
        log.debug("Building thumbnails.");
        BufferedImage[] images = new BufferedImage[pdDocument.getNumberOfPages()];
        try {
            PDFRenderer renderer = new PDFRenderer(pdDocument);
//            setting rendering hints in PDFBox yields ugly results :(
//            renderer.setRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION,
//            RenderingHints.VALUE_INTERPOLATION_BILINEAR));
            for (int i = 0; i < pdDocument.getNumberOfPages(); i++) {
                if (!selectedPages[i]) continue;
                //setup intermediate image height (appr 585 px for A4)
                //might lower the init render size to cut some rescaling passes
                BufferedImage raw = renderer.renderImageWithDPI(i, 50f);
                images[i] = Resizer.resize(raw, 75, 75);  // input image and desired thumbnail height
            }
            pdDocument.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return images;
    }

    static PDRectangle[] getMediaBoxes(PDDocument pdDocument, boolean[] selectedPages) {
        PDRectangle[] boxes = new PDRectangle[pdDocument.getNumberOfPages()];
        for (int i = 0; i < pdDocument.getNumberOfPages(); i++) {
            if (!selectedPages[i]) continue;
            boxes[i] = pdDocument.getPage(i).getMediaBox();
        }
        return boxes;
    }

    public static BufferedImage get300dpiPage(File file, int page) {
        log.debug("Rendering 300dpi image of {}({}).", file.getName(), page);
        PDDocument pdDocument;
        BufferedImage bufferedImage = null;
        try {
            pdDocument = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            bufferedImage = renderer.renderImageWithDPI(page, 300f);
            pdDocument.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }

    public static PDDocument createPdf() {
        log.debug("Initiating building PDF.");
        PDDocument pdf = new PDDocument();
        String producer = SettingsController.isProducerOverride() ?
                SettingsController.getProducer() : SettingsController.getDefaultProducer();
        pdf.getDocumentInformation().setProducer(producer);
        pdf.getDocumentInformation().setCreator(SettingsController.getCreator());
        log.info("Creator field set to {}", producer);
        return pdf;
    }

    public static void addPage(PDDocument pdf, Page pg, float jpgQ) throws IOException {
        int width = pg.getMediaWidth();
        int height = pg.getMediaHeight();
        PDPage page = new PDPage(new PDRectangle(width, height));
        pdf.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(pdf, page, PDPageContentStream.AppendMode.APPEND, false);
        PDImageXObject imXObj = JPEGFactory.createFromImage(pdf, pg.getRenderedImage(), jpgQ);
        contentStream.drawImage(imXObj, 0, 0, width, height);
        contentStream.close();
    }
}
