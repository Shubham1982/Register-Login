package com.example.RegisterLogin.resource;

import java.util.List;
import com.example.RegisterLogin.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmails(
            @RequestParam("emails") String emailsStr,
            @RequestParam(value = "resume", required = false) MultipartFile resume) {
        
        if (emailsStr == null || emailsStr.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("The list of email addresses cannot be empty.");
        }

        // Split the comma-separated string into a list and trim whitespace
        List<String> emails = java.util.Arrays.stream(emailsStr.split(","))
                .map(String::trim)
                .filter(email -> !email.isEmpty())
                .collect(java.util.stream.Collectors.toList());

        if (emails.isEmpty()) {
            return ResponseEntity.badRequest().body("No valid email addresses found.");
        }

        // Send emails in a separate thread or just synchronously. Doing it synchronously here.
        emailService.sendAutomatedEmails(emails, resume);

        return ResponseEntity.ok("Emails are being sent successfully.");
    }

    @PostMapping("/send-bulk")
    public ResponseEntity<String> sendBulkEmails(
            @RequestParam("excel") MultipartFile excelFile,
            @RequestParam(value = "resume", required = false) MultipartFile resume) {

        if (excelFile == null || excelFile.isEmpty()) {
            return ResponseEntity.badRequest().body("The excel file cannot be empty.");
        }

        // Processing runs synchronously here
        emailService.sendBulkAutomatedEmails(excelFile, resume);

        return ResponseEntity.ok("Bulk emails from Excel are being sent successfully.");
    }
}
