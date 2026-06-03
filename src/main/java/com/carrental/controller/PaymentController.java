package com.carrental.controller;

import com.carrental.dto.response.ApiResponse;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostMapping("/create-checkout-session")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Map<String, String>>> createCheckoutSession(
            @RequestBody Map<String, Object> paymentData) throws Exception {

        // Initialize Stripe with your Secret Key
        Stripe.apiKey = stripeApiKey;

        String carName = (String) paymentData.get("carName");
        // Stripe requires the amount to be in cents (e.g., $100.00 = 10000 cents)
        long amountInCents = ((Number) paymentData.get("amount")).longValue() * 100;

        // Build the Stripe Checkout Session
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                // Where Stripe should redirect the user after paying:
                .setSuccessUrl("http://localhost:8080/dashboard?payment=success")
                .setCancelUrl("http://localhost:8080/dashboard?payment=cancelled")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(amountInCents)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("DriveX Vehicle Rental: " + carName)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);

        Map<String, String> responseData = new HashMap<>();
        responseData.put("checkoutUrl", session.getUrl()); // The secure Stripe link

        return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                .success(true)
                .data(responseData)
                .timestamp(LocalDateTime.now())
                .build());
    }
}