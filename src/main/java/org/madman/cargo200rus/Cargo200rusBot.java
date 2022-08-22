package org.madman.cargo200rus;

import org.madman.cargo200rus.services.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class Cargo200rusBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.botUsername}")
    private String botUsername;
    @Value("${telegram.bot.botToken}")
    private String botToken;
    private SendMessageService sendMessageService;

    /**
     * @return bot username
     */
    @Override
    public String getBotUsername() {
        return botUsername;
    }

    /**
     * @return bot token
     */
    @Override
    public String getBotToken() {
        return botToken;
    }

    /**
     * @param update
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                sendMessageService.lossesCounter(message);
            }
        }
    }

    /**
     * @param sendMessageServiceObject
     */
    @Autowired
    public void setSendMessageService(SendMessageService sendMessageServiceObject) {
        this.sendMessageService = sendMessageServiceObject;
    }
}
