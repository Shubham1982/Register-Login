package com.example.RegisterLogin.AutomatePaymentIDsAndAmount;

import com.razorpay.RazorpayClient;
import com.razorpay.Transfer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

public class AutomatePaymentIDsAndAmount {

    public static void main(String[] args) {
        String inputFilePath = "/home/lt-444/Output Files/TranferIds.xlsx";
        String outputFilePath = "/home/lt-444/Output Files/OutputFile.xlsx";

        try (
                FileInputStream fis = new FileInputStream(inputFilePath);
                Workbook inputWorkbook = new XSSFWorkbook(fis);
                Workbook outputWorkbook = new XSSFWorkbook()
        ) {
            Sheet inputSheet = inputWorkbook.getSheetAt(0);
            Sheet outputSheet = outputWorkbook.createSheet("TransferDetails");

            // Add header row
            Row headerRow = outputSheet.createRow(0);
            headerRow.createCell(0).setCellValue("Transfer ID");
            headerRow.createCell(1).setCellValue("Settlement ID");

            RazorpayClient razorpayClient = new RazorpayClient("rzp_live_9V7qfX1xJMtx6a", "5FzAWpfxN2iKuZGHoZFMf3Rh");

            int outputRowNum = 1;

            for (Row inputRow : inputSheet) {
                Cell transferIdCell = inputRow.getCell(0); //Transfer ID is in the first column
                if (transferIdCell == null) continue;

                String transferId = transferIdCell.getStringCellValue();

                try {
                    Transfer transfer = razorpayClient.transfers.fetch(transferId);
                    String settlementId = transfer.get("recipient_settlement_id");

                    // Write to output sheet
                    Row row = outputSheet.createRow(outputRowNum++);
                    row.createCell(0).setCellValue(transferId);
                    row.createCell(1).setCellValue(settlementId);

                    System.out.println("Fetched: " + transferId + " -> " + settlementId);

                } catch (Exception e) {
                    System.err.println("Failed to fetch transfer for ID: " + transferId);
                    e.printStackTrace();
                }
            }

            // Write the output workbook to file
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                outputWorkbook.write(fos);
                System.out.println("Output written to: " + outputFilePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}