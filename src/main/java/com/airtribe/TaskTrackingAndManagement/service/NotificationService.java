package com.airtribe.TaskTrackingAndManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String username, String message){
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", message);
    }
}
