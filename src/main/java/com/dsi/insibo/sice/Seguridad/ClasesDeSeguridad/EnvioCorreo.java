package com.dsi.insibo.sice.Seguridad.ClasesDeSeguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
public class EnvioCorreo {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine; // Thymeleaf template engine

    public void sendEmail(String subject, String usuarioCorreo, String nuevaContrasena) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            // Procesar la plantilla Thymeleaf
            Context context = new Context();
            context.setVariable("titulo", subject);
            context.setVariable("usuarioCorreo", usuarioCorreo);
            context.setVariable("nuevaContrasena", nuevaContrasena);
            String htmlContent = templateEngine.process("Seguridad/email-template-r.html", context);

            // Configurar el correo
            helper.setTo(usuarioCorreo);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indica que el contenido es HTML

            // Adjuntar el logo como un recurso embebido con nombre de archivo específico
            helper.addInline("logoINSIBO", new ClassPathResource("/static/Imagenes/LogoINSIBO.jpg"));

            mailSender.send(message);
            System.err.println("Mensaje enviado");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
