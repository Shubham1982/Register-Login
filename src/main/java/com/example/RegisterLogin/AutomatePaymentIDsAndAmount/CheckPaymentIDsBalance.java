package com.example.RegisterLogin.AutomatePaymentIDsAndAmount;

import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.Transfer;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public class CheckPaymentIDsBalance {

    public static void main(String[] args) {
        String inputFilePath = "/home/lt-444/Balance Check/payment_check.xlsx";  // input Excel
        String outputFilePath = "/home/lt-444/Balance Check/output_payment_check.xlsx"; // output Excel

        try {
            // Razorpay client (provide your API key & secret here)
            RazorpayClient razorpayClient = new RazorpayClient("rzp_live_9V7qfX1xJMtx6a", "5FzAWpfxN2iKuZGHoZFMf3Rh");

            // Load input Excel
            FileInputStream fis = new FileInputStream(inputFilePath);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            // Create new workbook for output
            Workbook outputWorkbook = new XSSFWorkbook();
            Sheet outputSheet = outputWorkbook.createSheet("Result");

            // Write header
            Row header = outputSheet.createRow(0);
            header.createCell(0).setCellValue("Payment ID");

            int rowIndex = 1;

            // Iterate rows in input sheet
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // skip header if exists

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String paymentId = row.getCell(0).getStringCellValue();

                try {
                    // Fetch payment
                    Payment payment = razorpayClient.payments.fetch(paymentId);

                    // Calculate total transferred
                    double totalTransferred = 0.0;
                    List<Transfer> transfers = razorpayClient.payments.fetchAllTransfers(paymentId);
                    double refundedAmount = 0.0;

                    if (payment.has("amount_refunded")) {
                        refundedAmount = Double.parseDouble(payment.get("amount_refunded").toString()) / 100;   
                    }

                    for (Transfer t : transfers) {
                        if ("processed".equals(t.get("status"))) {
                            totalTransferred += Double.parseDouble(t.get("amount").toString()) / 100;
                        }
                    }

                    // Razorpay remaining balance
                    double razorpayBalance = (Double.parseDouble(payment.get("amount").toString()) / 100) - totalTransferred - refundedAmount;

                    // Decide final balance

                    // Write to output Excel
                    System.out.println("Row read: "+ rowIndex + "payment ID: "+paymentId+ "   balance: "+ razorpayBalance);
                    Row outputRow = outputSheet.createRow(rowIndex++);
                    outputRow.createCell(0).setCellValue(paymentId);
                    outputRow.createCell(1).setCellValue(razorpayBalance);


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
