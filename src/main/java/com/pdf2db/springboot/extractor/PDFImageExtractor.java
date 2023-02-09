package com.pdf2db.springboot.extractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

public class PDFImageExtractor {
    /* public static void main(String[] args) throws IOException {
        PDDocument document = PDDocument.load(new File("C:/Sheet_Piling_Design.pdf"));
        PDPageTree pages = document.getPages();
        for (PDPage page : pages) {
            for (int i = 0; i < page.get().size(); i++) {
                PDImageXObject image = page.get().get(i);
                System.out.println("Image " + (i + 1) + ": " + image.getWidth() + "x" + image.getHeight());
                // You can save the image to a file, convert it to a BLOB, or insert it into the database here
            }
        }
        document.close();
    } */
}
