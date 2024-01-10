
package com.example.pricetag.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.example.pricetag.dto.EmailDto;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  public void sendOtpEmail(EmailDto emailDto) throws MessagingException {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
    mimeMessageHelper.setTo(emailDto.getRecipients().stream().collect(Collectors.joining(",")));
    mimeMessageHelper.setSubject(emailDto.getSubject());
    mimeMessageHelper.setText(emailDto.getBody(), true);

    javaMailSender.send(mimeMessage);
  }
}