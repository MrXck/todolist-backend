package com.todo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailUtils {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        //主题
        message.setSubject(subject);
        //正文
        message.setText(body);
        //接受邮箱
        message.setTo(to);
        //发送者邮箱
        message.setFrom("待办事项<" + fromEmail + ">");
        //发送
        javaMailSender.send(message);
    }
}
