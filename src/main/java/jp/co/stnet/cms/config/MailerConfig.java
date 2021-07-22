package jp.co.stnet.cms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
public class MailerConfig {

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private int port;

    @Value("${mail.from}")
    private String from;

    @Value("${mail.subject}")
    private String subject;

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl mailer = new JavaMailSenderImpl();
        mailer.setHost(host);
        mailer.setPort(port);
        return mailer;
    }

    @Bean
    public SimpleMailMessage passwordReissueMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject(subject);
        return message;
    }

}
