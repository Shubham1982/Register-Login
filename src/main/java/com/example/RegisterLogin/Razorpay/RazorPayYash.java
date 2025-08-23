import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.Transfer;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RazorPayYash {

    public static void main(String args[]) {
        try {

            RazorpayClient razorpayClient = new RazorpayClient();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // The payment ID for which you want to fetch transfer details
            String paymentId = "pay_QqDawowqliGeEZ";


            //TOTAL AMOUNT

            float totalAmount = 0.0F;
            Payment payment = razorpayClient.payments.fetch(paymentId);
            System.out.println("Total payment of id " + paymentId + " is "  + Float.valueOf(payment.get("amount").toString()) / 100);


            List<Transfer> transfers = razorpayClient.payments.fetchAllTransfers(paymentId);
//
//            Transfer t1 = razorpayClient.transfers.fetch("trf_QM91jua8116f7i");
//
//            System.out.println(t1.toString());

            for (Transfer t: transfers) {

                if(t.get("status").equals("processed")) {
                    totalAmount += Float.valueOf(t.get("amount").toString()) / 100;
                }
            }

            System.out.println("Total Amount transfered " + totalAmount);

            System.out.println("Difference is "+ (Float.valueOf(payment.get("amount").toString()) / 100 - totalAmount) );


//            System.out.println(Instant.now().minusSeconds(86400));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}