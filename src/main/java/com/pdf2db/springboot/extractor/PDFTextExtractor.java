package com.pdf2db.springboot.extractor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//test1
//test2


public class PDFTextExtractor {
    public static void main(String[] args) throws IOException, SQLException {
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
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/application.properties"));

        String url = properties.getProperty("spring.datasource.url");
        String username = properties.getProperty("spring.datasource.username");
        String password = properties.getProperty("spring.datasource.password");

        Connection connection = DriverManager.getConnection(url, username, password);

        for (Map.Entry<String, List<String>> entry : headingInformation.entrySet()) {
            String sqlHeading = entry.getKey();
            String sqlInformation = String.join("\r\n", entry.getValue());

            String sql = "INSERT INTO headings_information (heading, information) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, sqlHeading);
            statement.setString(2, sqlInformation);
            statement.executeUpdate();
        }
        connection.close();


    }
}


