package com.example.sautiyangu.Utills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text, List<String> List){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("omaromarbcs@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if (List!=null && List.size()>0)
        message.setCc(getCcArray(List));

        emailSender.send(message);
    }

    private String[] getCcArray(List<String> ccList){
        String[] cc = new String[ccList.size()];

        for (int i = 0;i<ccList.size();i++){
            cc[i] = ccList.get(i);
        }
        return cc;
    }
}
