package com.example.RegisterLogin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendAutomatedEmails(List<String> toEmails, MultipartFile resume) {
        String emailContent = "Hi Hiring Team,<br><br>" +
                "I hope you're doing well. I'm Shubham Gore, a Java Backend Developer with 3 years of experience building scalable microservices using Java, Spring Boot, and SQL, along with hands-on expertise in Redis, Elasticsearch, and Docker/Kubernetes.<br><br>" +
                "I'm currently exploring new opportunities and am an immediate joiner. I would love to be considered for any relevant openings at your organization. I've attached my resume for your reference and would appreciate the chance to discuss how my background could add value to your team.<br><br>" +
                "Looking forward to hearing from you.<br><br>" +
                "Best regards,<br>" +
                "Shubham Gore<br>" +
                "+91-9373651636<br>" +
                "shubham.gore2025@gmail.com<br>" +
                "<a href=\"https://www.linkedin.com/in/shubham-gore-linked-in/?trk=public-profile-join-page\">LinkedIn: shubham-gore</a>";

        byte[] resumeBytes = null;
        String resumeFilename = null;
        if (resume != null && !resume.isEmpty()) {
            try {
                resumeBytes = resume.getBytes();
                resumeFilename = resume.getOriginalFilename();
            } catch (IOException e) {
                System.err.println("Failed to read resume file");
                e.printStackTrace();
            }
        }

        for (String toEmail : toEmails) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                
                helper.setTo(toEmail);
                helper.setSubject("Application for Java Software Engineer Role – Shubham Gore (Immediate Joiner)");
                helper.setText(emailContent, true); // true indicates HTML content
                
                if (resumeBytes != null) {
                    String filename = (resumeFilename != null && !resumeFilename.isEmpty()) ? resumeFilename : "Resume.pdf";
                    helper.addAttachment(filename, new ByteArrayResource(resumeBytes));
                }

                mailSender.send(message);
                System.out.println("Email sent successfully to: " + toEmail);
                
                // Random delay between 11 and 20 seconds to avoid spam filters
                int randomDelaySeconds = ThreadLocalRandom.current().nextInt(11, 21);
                System.out.println("Waiting for " + randomDelaySeconds + " seconds before sending the next email...");
                Thread.sleep(randomDelaySeconds * 1000L);
            } catch (MessagingException e) {
                System.err.println("Failed to send email to: " + toEmail);
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.err.println("Thread was interrupted during sleep");
                Thread.currentThread().interrupt();
            }
        }
    }

    public void sendBulkAutomatedEmails(MultipartFile excelFile, MultipartFile resume) {
        byte[] resumeBytes = null;
        String resumeFilename = null;
        if (resume != null && !resume.isEmpty()) {
            try {
                resumeBytes = resume.getBytes();
                resumeFilename = resume.getOriginalFilename();
            } catch (IOException e) {
                System.err.println("Failed to read resume file");
                e.printStackTrace();
            }
        }

        try (InputStream is = excelFile.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (!rows.hasNext()) return;

            Row headerRow = rows.next();

            // 1. Read the header row to get all column names
            List<String> columnNames = new ArrayList<>();
            for (Cell cell : headerRow) {
                columnNames.add(getCellValue(cell).trim().toLowerCase());
            }

            if (!columnNames.contains("email")) {
                System.err.println("No 'email' column found in excel.");
                return;
            }

            // 2. Process each data row
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                String email = getCellValue(currentRow.getCell(0)).trim();
                String hrName = StringUtils.capitalize(getCellValue(currentRow.getCell(1)).trim());
                String title = getCellValue(currentRow.getCell(2)).trim();

                if(hrName.isEmpty()){
                    hrName = "Hiring Team";
                }
                if(title.isEmpty()){
                    title = "Java Software Engineer";
                }
                if (email == null || email.isEmpty()) {
                    throw new RuntimeException("Email not found in file");
                }

                String subject = "Application for " + title + " Role – Shubham Gore (Immediate Joiner)";

                String emailContent = "Hi " + hrName + ",<br><br>" +
                    "I hope you're doing well. I'm Shubham Gore, a Java Backend Developer with 3 years of experience building scalable microservices using Java, Spring Boot, and SQL, along with hands-on expertise in Redis, Elasticsearch, and Docker/Kubernetes.<br><br>" +
                    "I'm currently exploring new opportunities and am an immediate joiner. I would love to be considered for any relevant openings at your organization. I've attached my resume for your reference and would appreciate the chance to discuss how my background could add value to your team.<br><br>" +
                    "Looking forward to hearing from you.<br><br>" +
                    "Best regards,<br>" +
                    "Shubham Gore<br>" +
                    "+91-9373651636<br>" +
                    "shubham.gore2025@gmail.com<br>" +
                    "<a href=\"https://www.linkedin.com/in/shubham-gore-linked-in/?trk=public-profile-join-page\">LinkedIn: shubham-gore</a>";

                try {
                    MimeMessage message = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                    helper.setTo(email);
                    helper.setSubject(subject);
                    helper.setText(emailContent, true);

                    if (resumeBytes != null) {
                        String filename = (resumeFilename != null && !resumeFilename.isEmpty()) ? resumeFilename : "Resume.pdf";
                        helper.addAttachment(filename, new ByteArrayResource(resumeBytes));
                    }

                    mailSender.send(message);
                    System.out.println("Email sent successfully to: " + email + " (Title: " + title + ", HR: " + hrName + ")");

                    // Random delay between 11 and 20 seconds
                    int randomDelaySeconds = ThreadLocalRandom.current().nextInt(11, 21);
                    System.out.println("Waiting for " + randomDelaySeconds + " seconds before sending the next email...");
                    Thread.sleep(randomDelaySeconds * 1000L);
                } catch (MessagingException e) {
                    System.err.println("Failed to send email to: " + email);
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    System.err.println("Thread was interrupted during sleep");
                    Thread.currentThread().interrupt();
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to process Excel file");
            e.printStackTrace();
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }
}
