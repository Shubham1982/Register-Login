package com.example.RegisterLogin.Razorpay;

import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import java.util.Arrays;
import java.util.List;

public class RazorpayPaymentChecker {

    public static void main(String[] args) {

        try {

            RazorpayClient razorpay =
                    new RazorpayClient("rzp_live_9V7qfX1xJMtx6a", "5FzAWpfxN2iKuZGHoZFMf3Rh");

            List<String> paymentIds = Arrays.asList(
                    "pay_S3OcdItThPRvPS","pay_S3mYm8os3wTpQ4","pay_S4YH7mgz1aLx4J","pay_S4ZguewOJsEBn3","pay_S4yJV1Q8oPuDYq","pay_S5l7raLdAXlevL","pay_S6ZSUAfVyf9CSr","pay_S6yK3XCqzq8o6K","pay_S8lXf2WVjK23qm","pay_S8tdujx27fGkeF","pay_S8ukOhQ8SQ2ASo","pay_S9KTgO3EVFAyZK","pay_S9zFKyTnkADqRp","pay_SA7b1ThtYfEGEJ","pay_SAVBkgxrcpoHwR","pay_SBIXF8ZRewZ3hX","pay_SBhpzelme7SAxL","pay_SC6J1E47idc5VW","pay_SCU88FpbRnr71s","pay_SCsa4sAJCwYrmY","pay_SE5dB1Nx9u49kM","pay_SER9XbbMLvJl7E","pay_SESyor6gClx4CF","pay_SFGscPAzPuNgCQ","pay_SFf62GYRi4t2nb","pay_SGq6E3S33kYLGy","pay_SHFg6Gbn5rPOjQ","pay_SHe2nW6CIvgZF4","pay_SIQudIcDcfKNmf","pay_SIpHGkPMw2ozNV","pay_SJcZ6aqfA3Bcgd","pay_SK1GlljKAwD5pa","pay_SKfEmRXWITrYPp","pay_SKo0nKiEmbhHzA","pay_SLCdHYh12DpJkI"
            );

            for (String paymentId : paymentIds) {

                try {

                    Payment payment = razorpay.payments.fetch(paymentId);

                    double amount =
                            Double.parseDouble(
                                    payment.get("amount").toString()
                            ) / 100;

                    System.out.println("=================================");
                    System.out.println("PAYMENT FOUND");
                    System.out.println("Payment ID : " + paymentId);
                    System.out.println("Amount     : " + amount);
                    System.out.println("Status     : " + payment.get("status"));

                } catch (RazorpayException e) {

                    if (e.getMessage().contains("older than 180 days")) {

                        System.out.println("=================================");
                        System.out.println("OLDER THAN 180 DAYS");
                        System.out.println("Payment ID : " + paymentId);

                    } else {

                        System.out.println("=================================");
                        System.out.println("ERROR");
                        System.out.println("Payment ID : " + paymentId);
                        System.out.println("Message    : " + e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}