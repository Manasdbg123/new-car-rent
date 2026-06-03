package com.carrental.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    // The @Async annotation tells Spring to run this on a separate background thread!
    @Async
    public void sendWelcomeEmail(String toEmail, String userName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Welcome to DriveX Premium Rentals! 🚗");

            // A beautifully formatted HTML email
            String htmlContent = """
                    <div style="font-family: Arial, sans-serif; padding: 20px; max-width: 600px; margin: 0 auto; border: 1px solid #eee; border-radius: 10px;">
                        <h2 style="color: #0056b3;">Welcome to DriveX, %s!</h2>
                        <p>We are thrilled to have you on board. Your account has been successfully created.</p>
                        <p>Before you make your first reservation, please ensure you log in and upload your Driver's License for KYC verification.</p>
                        <br/>
                        <a href="http://localhost:8080/dashboard" style="background-color: #ffb700; color: #222; padding: 12px 25px; text-decoration: none; font-weight: bold; border-radius: 5px;">Go to Dashboard</a>
                        <br/><br/>
                        <p style="color: #777; font-size: 0.9em;">Safe travels,<br/>The DriveX Team</p>
                    </div>
                    """.formatted(userName);

            helper.setText(htmlContent, true); // Set to 'true' to enable HTML
            mailSender.send(message);

            log.info("Successfully sent Welcome Email in the background to: {}", toEmail);

        } catch (MessagingException e) {
            log.error("Failed to send welcome email to {}", toEmail, e);
        }
    }
    @Async
    public void sendBookingInvoice(String toEmail, String userName, String carName, String totalAmount) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Your DriveX Reservation is Confirmed! 🚘");

            // A beautifully formatted HTML Invoice
            String htmlContent = """
                    <div style="font-family: Arial, sans-serif; padding: 20px; max-width: 600px; margin: 0 auto; border: 1px solid #eee; border-radius: 10px;">
                        <h2 style="color: #28a745;">Booking Confirmed!</h2>
                        <p>Hi %s,</p>
                        <p>Your reservation for the <strong>%s</strong> has been successfully processed.</p>
                        
                        <div style="background: #f8f9fa; padding: 15px; border-radius: 8px; margin: 20px 0;">
                            <h3 style="margin-top: 0; color: #333;">Invoice Summary</h3>
                            <p style="font-size: 1.2rem; margin-bottom: 0;">Total Paid: <strong style="color: #0056b3;">$%s</strong></p>
                        </div>
                        
                        <p>You can view your full itinerary, pickup instructions, and manage your reservation directly from your client dashboard.</p>
                        <br/>
                        <a href="http://localhost:8080/dashboard" style="background-color: #0056b3; color: white; padding: 12px 25px; text-decoration: none; font-weight: bold; border-radius: 5px;">View My Dashboard</a>
                        <br/><br/>
                        <p style="color: #777; font-size: 0.9em;">Thank you for choosing DriveX! Safe travels.</p>
                    </div>
                    """.formatted(userName, carName, totalAmount);

            helper.setText(htmlContent, true);
            mailSender.send(message);

            log.info("Successfully sent Booking Invoice in the background to: {}", toEmail);

        } catch (MessagingException e) {
            log.error("Failed to send booking invoice to {}", toEmail, e);
        }
    }
}