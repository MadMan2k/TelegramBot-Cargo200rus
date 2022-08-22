package org.madman.cargo200rus.services;

import org.madman.cargo200rus.messagesender.MessageSender;
import org.madman.cargo200rus.pojo.EquipmentLosses;
import org.madman.cargo200rus.pojo.KeyboardMaker;
import org.madman.cargo200rus.pojo.PersonnelLosses;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

@Service
public class SendMessageService {

    private final MessageSender messageSender;

    public SendMessageService(MessageSender messageSenderObject) {
        this.messageSender = messageSenderObject;
    }

    /**
     * @param message
     */
    public void lossesCounter(Message message) {
        boolean detailedReport = message.getText().equals("DETAILED");

        List<String> keyboardButtons = new ArrayList<>();
        keyboardButtons.add("SIMPLE");
        keyboardButtons.add("DETAILED");
        ReplyKeyboardMarkup markup = KeyboardMaker.make(keyboardButtons);

        PersonnelLosses personnelLosses = new PersonnelLosses();
        EquipmentLosses equipmentLosses = new EquipmentLosses();
        String personnelLossesString = null;
        String equipmentLossesString = null;
        String responseMessage = null;

        switch (message.getText()) {
            case "DETAILED":
            case "SIMPLE":
                personnelLossesString = personnelLosses.getPersonnelLosses(detailedReport);
                equipmentLossesString = equipmentLosses.getEquipmentLosses(detailedReport);
                responseMessage = personnelLossesString + equipmentLossesString;
                break;
            default:
                responseMessage = "Please tap one of the button below to choose simple or detailed report";
                break;
        }

        SendMessage outputMessage = SendMessage.builder()
                .text(responseMessage)
                .parseMode("HTML")
                .chatId(message.getChatId())
                .replyMarkup(markup)
                .build();

        messageSender.sendMessage(outputMessage);
    }
}
