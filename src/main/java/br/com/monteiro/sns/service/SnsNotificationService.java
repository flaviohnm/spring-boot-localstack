package br.com.monteiro.sns.service;

import io.awspring.cloud.sns.core.SnsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SnsNotificationService {

    @Value("${snsNotificationName}")
    private String notificationName;

    private final SnsTemplate snsTemplate;

    @Autowired
    public SnsNotificationService(SnsTemplate snsTemplate) {
        this.snsTemplate = snsTemplate;
    }

    public void sendNotification(String subject, Object message, Map<String, Object> headers) {
        this.snsTemplate.convertAndSend(notificationName, message, headers);
    }

}