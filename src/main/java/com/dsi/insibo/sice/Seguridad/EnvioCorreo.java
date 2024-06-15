package com.dsi.insibo.sice.Seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EnvioCorreo {

    //Importante hacer la inyección de dependencia de JavaMailSender:
    @Autowired
    private JavaMailSender mailSender;

    //Pasamos por parametro: destinatario, asunto y el mensaje
    public void sendEmail(String to, String subject, String content) {

        SimpleMailMessage email = new SimpleMailMessage();
        //email.setFrom("insibo.sice@gmail.com");
        email.setTo(to);
        email.setSubject(subject);
        email.setText(content);
        mailSender.send(email);
        System.err.println("Mensaje enviado");
    }
}