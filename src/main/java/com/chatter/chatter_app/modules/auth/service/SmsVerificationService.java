package com.chatter.chatter_app.modules.auth.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatter.chatter_app.modules.auth.model.SmsVerification;
import com.chatter.chatter_app.modules.auth.repository.SmsVerificationRepository;

@Service
@Transactional
public class SmsVerificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(SmsVerificationService.class);
    private static final int MAX_SMS_PER_HOUR = 3;
    private final SecureRandom random = new SecureRandom();
    
    @Autowired
    private SmsVerificationRepository smsVerificationRepository;
    
    @Autowired
    private TwilioSmsService twilioSmsService;
    
    public void sendVerificationCode(String phoneNumber) {
      // Rate limiting check
      LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
      long recentSmsCount = smsVerificationRepository.countByPhoneNumberAndCreatedAtAfter(phoneNumber, oneHourAgo);
      
      if (recentSmsCount >= MAX_SMS_PER_HOUR) {
          throw new RuntimeException("SMS limit exceeded. Please try again later.");
      }
      
      try {
        // Clean up old verification codes
        smsVerificationRepository.deleteByPhoneNumber(phoneNumber);
        
        // Generate 6-digit code
        String code = String.format("%06d", random.nextInt(1000000));
        
        // Save verification code
        SmsVerification verification = new SmsVerification(phoneNumber, code);
        smsVerificationRepository.save(verification);
        
        // Send SMS
        boolean smsSent = twilioSmsService.sendVerificationSms(phoneNumber, code);
        
        if (!smsSent) {
            smsVerificationRepository.delete(verification);
            throw new RuntimeException("Falha para enviar o código SMS");
        }
        
        logger.info("Código de verificação SMS enviado com sucesso para: {}", phoneNumber);
        
    } catch (RuntimeException e) {
        logger.error("Erro ao enviar SMS para {}: {}", phoneNumber, e.getMessage());
        throw new RuntimeException("Falha para enviar código de verificação SMS: " + e.getMessage());
    }
    }
    
    public boolean verifyCode(String phoneNumber, String code) {
        Optional<SmsVerification> verificationOpt = 
            smsVerificationRepository.findByPhoneNumberAndCodeAndIsUsedFalse(phoneNumber, code);
        
        if (verificationOpt.isEmpty()) {
            logger.warn("Tentativa de verificação de código inválida para o número: {}", phoneNumber);
            return false;
        }
        
        SmsVerification verification = verificationOpt.get();
        
        if (verification.isExpired()) {
            logger.warn("Tentativa de verificação de código expirada para o número: {}", phoneNumber);
            return false;
        }
        
        // Mark as used
        verification.setUsed(true);
        smsVerificationRepository.save(verification);
        
        logger.info("Verificação SMS bem sucedida para o número: {}", phoneNumber);
        return true;
    }
}