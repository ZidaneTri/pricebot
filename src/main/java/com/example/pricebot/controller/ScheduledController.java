package com.example.pricebot.controller;

import com.example.pricebot.OnlinerBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Controller
public class ScheduledController {


    @Autowired
    private OnlinerBot onlinerBot;

    @Scheduled(fixedRate = 5000)
    private void scheduledMessage(){
        try {
            onlinerBot.execute(new SendMessage().setChatId(231112232L).setText("Test"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}