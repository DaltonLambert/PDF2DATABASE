package com.pdf2db.springboot.extractor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PDFTextExtractor {
    public static void main(String[] args) throws IOException {
        PDDocument document = PDDocument.load(new File("C:/Sheet_Piling_Design.pdf"));
        PDFTextStripper pdfStripper = new PDFTextStripper();
        pdfStripper.setStartPage(19);
        pdfStripper.setEndPage(36);
        String text = pdfStripper.getText(document);
        System.out.println(text);
        document.close();
    }
}
