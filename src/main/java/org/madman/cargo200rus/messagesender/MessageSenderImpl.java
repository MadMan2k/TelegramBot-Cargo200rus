package org.madman.cargo200rus.messagesender;

import org.madman.cargo200rus.Cargo200rusBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class MessageSenderImpl implements MessageSender {
    private Cargo200rusBot cargo200rusBot;

    /**
     * @param sendMessage
     */
    @Override
    public void sendMessage(SendMessage sendMessage) {
        try {
            cargo200rusBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param cargo200rusBotObject
     */
    @Autowired
    public void setCargo200rusBot(Cargo200rusBot cargo200rusBotObject) {
        this.cargo200rusBot = cargo200rusBotObject;
    }
}
