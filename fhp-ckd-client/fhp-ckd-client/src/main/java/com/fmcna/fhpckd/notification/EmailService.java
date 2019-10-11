package com.fmcna.fhpckd.notification;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String mailUserName;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${spring.mail.fromaddress}")
    private String fromMail;

    @Value("${spring.mail.defaulttolist}")
    private String defaultMailingList;

    @Value("${spring.mail.errortolist}")
    private String errorMailingList;

    public void sendEmail(String subject, String body) {
        this.sendEmail(subject, body, defaultMailingList);
    }
    
    public void sendErrorEmail(String subject, String body) {
        this.sendEmail(subject, body, errorMailingList);
    }
    
   public void sendEmail(String subject, String body, String toList) {
        logger.info("Sending Email to the group:: " + toList);
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setTo(InternetAddress.parse(toList));
            helper.setFrom(fromMail);
 
            emailSender.send(message);
            logger.info("Email Successfully Sent");

        } catch (Exception e) {
            logger.error("Error sending email notification. Subject: " + subject, e);
        }
    }
     
}
