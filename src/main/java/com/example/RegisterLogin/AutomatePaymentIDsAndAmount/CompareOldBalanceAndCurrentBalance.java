package com.example.RegisterLogin.AutomatePaymentIDsAndAmount;

import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.Transfer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

public class CompareOldBalanceAndCurrentBalance {

    public static void main(String[] args) {

        String inputFilePath =
                "/Users/shubhamgore/Downloads/razorpay balance check.xlsx";

        String outputFilePath =
                "/Users/shubhamgore/Downloads/FinalBalanceSheet.xlsx";

        try {

            // Razorpay client
            RazorpayClient razorpayClient =
                    new RazorpayClient("rzp_live_9V7qfX1xJMtx6a", "5FzAWpfxN2iKuZGHoZFMf3Rh");

            // Load input Excel
            FileInputStream fis = new FileInputStream(inputFilePath);

            Workbook workbook = new XSSFWorkbook(fis);

            Sheet sheet = workbook.getSheetAt(0);

            // Create output workbook
            Workbook outputWorkbook = new XSSFWorkbook();

            Sheet outputSheet =
                    outputWorkbook.createSheet("Result");

            // Header Row
            Row header = outputSheet.createRow(0);

            header.createCell(0).setCellValue("Payment ID");
            header.createCell(1).setCellValue("Old Balance");
            header.createCell(2).setCellValue("Razorpay Balance");
            header.createCell(3).setCellValue("Status");

            int rowIndex = 1;

            // Formatter to safely read any cell type
            DataFormatter formatter = new DataFormatter();

            // Iterate input rows
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();

                try {

                    // Read Payment ID safely
                    String paymentId =
                            formatter.formatCellValue(row.getCell(0)).trim();

                    // Read Old Balance safely
                    String oldBalanceStr =
                            formatter.formatCellValue(row.getCell(1)).trim();

                    // Skip empty payment IDs
                    if (paymentId.isEmpty()) {

                        System.out.println("Skipping empty payment ID row");

                        continue;
                    }

                    double oldBalance = 0.0;

                    try {

                        if (!oldBalanceStr.isEmpty()) {
                            oldBalance = Double.parseDouble(oldBalanceStr);
                        }

                    } catch (NumberFormatException e) {

                        System.out.println(
                                "Invalid old balance for Payment ID: "
                                        + paymentId
                        );
                    }

                    // Fetch payment from Razorpay
                    Payment payment =
                            razorpayClient.payments.fetch(paymentId);

                    double paymentAmount =
                            Double.parseDouble(
                                    payment.get("amount").toString()
                            ) / 100;

                    // Refunded amount
                    double refundedAmount = 0.0;

                    if (payment.has("amount_refunded")) {

                        refundedAmount =
                                Double.parseDouble(
                                        payment.get("amount_refunded").toString()
                                ) / 100;
                    }

                    // Fetch transfers
                    List<Transfer> transfers =
                            razorpayClient.payments.fetchAllTransfers(paymentId);

                    double totalTransferred = 0.0;

                    for (Transfer transfer : transfers) {

                        String status =
                                transfer.get("status").toString();

                        if ("processed".equalsIgnoreCase(status)) {

                            totalTransferred +=
                                    Double.parseDouble(
                                            transfer.get("amount").toString()
                                    ) / 100;
                        }
                    }

                    // Calculate Razorpay Balance
                    double razorpayBalance =
                            paymentAmount
                                    - totalTransferred
                                    - refundedAmount;

                    // Compare balances
                    String status;

                    if (Math.abs(oldBalance - razorpayBalance) < 0.01) {

                        status = "MATCHED";

                    } else {

                        status = "NOT MATCHED";
                    }

                    // Console log
                    System.out.println(
                            "Row: " + rowIndex
                                    + " | Payment ID: " + paymentId
                                    + " | Old Balance: " + oldBalance
                                    + " | Razorpay Balance: " + razorpayBalance
                                    + " | Status: " + status
                    );

                    // Write to output Excel
                    Row outputRow =
                            outputSheet.createRow(rowIndex++);

                    outputRow.createCell(0)
                            .setCellValue(paymentId);

                    outputRow.createCell(1)
                            .setCellValue(oldBalance);

                    outputRow.createCell(2)
                            .setCellValue(razorpayBalance);

                    outputRow.createCell(3)
                            .setCellValue(status);

                } catch (Exception e) {

                    System.out.println(
                            "Error processing row: "
                                    + row.getRowNum()
                    );

                    e.printStackTrace();
                }
            }

            // Auto size columns
            for (int i = 0; i < 4; i++) {
                outputSheet.autoSizeColumn(i);
            }

            // Save output workbook
            FileOutputStream fos =
                    new FileOutputStream(outputFilePath);

            outputWorkbook.write(fos);

            fos.close();

            outputWorkbook.close();
            workbook.close();
            fis.close();

            System.out.println(
                    "\n✅ Output file created successfully at:\n"
                            + outputFilePath
            );

        } catch (Exception e) {

            System.out.println("Fatal Error:");

            e.printStackTrace();
        }
    }
}