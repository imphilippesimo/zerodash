package com.zerofiltre.zerodash.service;

import com.zerofiltre.zerodash.model.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.mail.javamail.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;
import org.thymeleaf.context.*;
import org.thymeleaf.spring5.*;

import javax.mail.internet.*;
import java.nio.charset.*;
import java.util.*;

@Service
public class EmailService {

    Logger logger = LoggerFactory.getLogger(EmailService.class);

    private static final String USER = "user";
    private static final String BASE_URL = "baseUrl";

    @Value("${zerodash.email.base-url}")
    private String baseUrl;

    private SpringTemplateEngine templateEngine;

    private MessageSource messageSource;

    private JavaMailSender emailSender;

    public EmailService(SpringTemplateEngine templateEngine, MessageSource messageSource, JavaMailSender emailSender) {
        this.templateEngine = templateEngine;
        this.messageSource = messageSource;
        this.emailSender = emailSender;
    }

    @Async
    public void sendSimpleMessage(String to, String subject, String content, Boolean isMultipart, Boolean isHtml) {
        logger.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            emailSender.send(mimeMessage);
            logger.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.warn("Email could not be sent to user '{}'", to, e);
            } else {
                logger.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }

    @Async
    public void sendEmailFromTemplate(ZDUser user, String templateName, String subjectKey) {
        Locale locale = Locale.FRANCE;
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(subjectKey, null, locale);
        sendSimpleMessage(user.getEmail(), subject, content, false, true);

    }

    @Async
    public void sendActivationEmail(ZDUser user) {
        logger.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "activationEmail", "email.activation.title");
    }

}
