package com.chatter.chatter_app.modules.auth.service;

public interface TwilioSmsService {
    /**
     * Sends SMS verification code to the specified phone number
     * @param phoneNumber The phone number to send SMS to (e.g., "+5511999999999")
     * @param verificationCode The 6-digit code to send
     * @return true if SMS was sent successfully, false otherwise
     */
    boolean sendVerificationSms(String phoneNumber, String verificationCode);
}
