package com.example.RegisterLogin.AutomatePaymentIDsAndAmount;

import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.Transfer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class RazorPayExcelChecker {

    public static void main(String[] args) {
        String inputFilePath = "/home/lt-444/PaymentIDs.xlsx";  // input Excel
        String outputFilePath = "/home/lt-444/PaymentIDs_Output.xlsx"; // output Excel

        try {
            // Razorpay client (provide your API key & secret here)
            RazorpayClient razorpayClient = new RazorpayClient("API_KEY", "API_SECRET");

            // Load input Excel
            FileInputStream fis = new FileInputStream(inputFilePath);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            // Create new workbook for output
            Workbook outputWorkbook = new XSSFWorkbook();
            Sheet outputSheet = outputWorkbook.createSheet("Result");

            // Write header
            Row header = outputSheet.createRow(0);
            header.createCell(0).setCellValue("PaymentID");
            header.createCell(1).setCellValue("Final Balance");

            int rowIndex = 1;

            // Iterate rows in input sheet
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // skip header if exists

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String paymentId = row.getCell(0).getStringCellValue();
                double sheetBalance = row.getCell(1).getNumericCellValue();

                try {
                    // Fetch payment
                    Payment payment = razorpayClient.payments.fetch(paymentId);

                    // Calculate total transferred
                    double totalTransferred = 0.0;
                    List<Transfer> transfers = razorpayClient.payments.fetchAllTransfers(paymentId);

                    for (Transfer t : transfers) {
                        if ("processed".equals(t.get("status"))) {
                            totalTransferred += Double.parseDouble(t.get("amount").toString()) / 100;
                        }
                    }

                    // Razorpay remaining balance
                    double razorpayBalance = (Double.parseDouble(payment.get("amount").toString()) / 100) - totalTransferred;

                    // Decide final balance
                    double finalBalance;
                    if (razorpayBalance < sheetBalance) {
                        finalBalance = razorpayBalance;
                    } else {
                        finalBalance = sheetBalance;
                    }

                    // Write to output Excel
                    Row outputRow = outputSheet.createRow(rowIndex++);
                    outputRow.createCell(0).setCellValue(paymentId);
                    outputRow.createCell(1).setCellValue(finalBalance);

                } catch (Exception e) {
                    System.out.println("Error fetching data for PaymentID: " + paymentId);
                    e.printStackTrace();
                }
            }

            // Save output file
            FileOutputStream fos = new FileOutputStream(outputFilePath);
            outputWorkbook.write(fos);
            fos.close();
            outputWorkbook.close();
            workbook.close();

            System.out.println("✅ Output file created at: " + outputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
