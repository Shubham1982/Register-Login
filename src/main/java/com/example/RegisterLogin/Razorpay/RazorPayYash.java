import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.Transfer;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class RazorPayYash {

    public static void main(String args[]) {
        try {

            RazorpayClient razorpayClient = new RazorpayClient("rzp_live_9V7qfX1xJMtx6a", "5FzAWpfxN2iKuZGHoZFMf3Rh");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // The payment ID for which you want to fetch transfer details
            String paymentId = "pay_R0Sw9yuWBaCXu7";


            //TOTAL AMOUNT

            float totalAmount = 0.0F;
            Payment payment = razorpayClient.payments.fetch(paymentId);
            System.out.println("Total payment of id " + paymentId + " is "  + Float.valueOf(payment.get("amount").toString()) / 100);


            List<Transfer> transfers = razorpayClient.payments.fetchAllTransfers(paymentId);

            Transfer t1 = razorpayClient.transfers.fetch("trf_Qssa57Zn6kHRlY");

            System.out.println(t1.toString());

            for (Transfer t: transfers) {

                if(t.get("status").equals("processed")) {
                    totalAmount += Float.valueOf(t.get("amount").toString()) / 100;
                }
            }

            System.out.println("Total Amount transfered " + totalAmount);

            System.out.println("Difference is "+ (Float.valueOf(payment.get("amount").toString()) / 100 - totalAmount) );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}