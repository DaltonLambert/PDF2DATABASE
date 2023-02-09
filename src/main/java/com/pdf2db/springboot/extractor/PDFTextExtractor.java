package com.pdf2db.springboot.extractor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFTextExtractor {
    public static void main(String[] args) throws IOException {
        PDDocument document = PDDocument.load(new File("C:/Sheet_Piling_Design.pdf"));
        PDFTextStripper pdfStripper = new PDFTextStripper();
        pdfStripper.setStartPage(19);
        pdfStripper.setEndPage(36);
        String text = pdfStripper.getText(document);
        document.close();

        Map<String, List<String>> headingInformation = new HashMap<>();
        List<String> headings = new ArrayList<>();
        List<String> information = new ArrayList<>();
        String[] lines = text.split("\r\n");

        Pattern headingPattern = Pattern.compile("^(\\d+(?:\\.\\d+)*)\\.\\s+(.*)");
        Matcher matcher;
        boolean headingFound = false;
        String currentHeadingNumber = "";
        for (String line : lines) {
            matcher = headingPattern.matcher(line);
            if (matcher.matches()) {
                String headingNumber = matcher.group(1);
                String headingText = matcher.group(2);
                if (headingFound) {
                    headingInformation.put(currentHeadingNumber + " " + headings.get(headings.size() - 1), information);
                    information = new ArrayList<>();
                }
                headingFound = true;
                currentHeadingNumber = headingNumber;
                headings.add(headingText);
            } else {
                if (headingFound) {
                    information.add(line);
                }
            }
        }
        headingInformation.put(currentHeadingNumber + " " + headings.get(headings.size() - 1), information);

        for (Map.Entry<String, List<String>> entry : headingInformation.entrySet()) {
            System.out.println("Heading: " + entry.getKey());
            System.out.println("Information:");
            for (String info : entry.getValue()) {
                System.out.println(info);
            }
            System.out.println();
        }
    }
}

