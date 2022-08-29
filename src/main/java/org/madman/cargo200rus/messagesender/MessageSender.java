package org.madman.cargo200rus.messagesender;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public interface MessageSender {

    void sendMessage(SendMessage sendMessage);
    void sendPhoto(SendPhoto sendPhoto);
}
