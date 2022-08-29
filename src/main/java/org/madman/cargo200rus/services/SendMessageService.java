package org.madman.cargo200rus.services;

import org.apache.commons.io.FileUtils;
import org.madman.cargo200rus.messagesender.MessageSender;
import org.madman.cargo200rus.pojo.EquipmentLosses;
import org.madman.cargo200rus.pojo.KeyboardMaker;
import org.madman.cargo200rus.pojo.PersonnelLosses;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SendMessageService {

    private static final String IMAGE_PATH = "static.images/cargo200_info_1200x900.jpg";

    private final MessageSender messageSender;

    public SendMessageService(@Lazy MessageSender messageSenderObject) {
        this.messageSender = messageSenderObject;
    }

    /**
     * @param message input message
     */
    public void lossesCounter(Message message) {
        boolean detailedReport = message.getText().equals("DETAILED");

        List<String> keyboardButtons = new ArrayList<>();
        keyboardButtons.add("SIMPLE");
        keyboardButtons.add("DETAILED");
        keyboardButtons.add("INFO");
        ReplyKeyboardMarkup markup = KeyboardMaker.make(keyboardButtons);

        PersonnelLosses personnelLosses = new PersonnelLosses();
        EquipmentLosses equipmentLosses = new EquipmentLosses();
        String personnelLossesString = null;
        String equipmentLossesString = null;
        String textMessage = null;
        File image = null;

        switch (message.getText()) {
            case "DETAILED":
            case "SIMPLE":
                personnelLossesString = personnelLosses.getPersonnelLosses(detailedReport);
                equipmentLossesString = equipmentLosses.getEquipmentLosses(detailedReport);
                textMessage = personnelLossesString + equipmentLossesString;

                textMessage = textMessage + "<i>Data sources:\n"
                        + "<a href=\"https://www.zsu.gov.ua/en\">Armed Forces of Ukraine</a>\n"
                        + "<a href=\"https://www.mil.gov.ua/en/\">Ministry of Defence of Ukraine</a></i>";
                setAttributesAndCallSendMessage(message.getChatId(), markup, textMessage);
                break;

            case "INFO":
//                try {
//                    image = ResourceUtils.getFile("classpath:" + IMAGE_PATH);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                if (image != null) {
//                    setAttributesAndCallSendPhoto(message.getChatId(), markup, image);
//                }

                ClassPathResource cpr = new ClassPathResource(IMAGE_PATH);

                InputStream inputStream = null;
                try {
                    inputStream = cpr.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                testMethod(message.getChatId(), markup, inputStream);



                break;

            default:
                textMessage = "Please tap one of the button below to choose simple or detailed report";
                setAttributesAndCallSendMessage(message.getChatId(), markup, textMessage);
                break;
        }
    }

    private void testMethod(long chatId, ReplyKeyboardMarkup markup, InputStream inputStream) {
        SendPhoto sendPhoto = SendPhoto.builder()
                .photo(new InputFile(inputStream, "inputStreamImage"))
                .chatId(chatId)
                .replyMarkup(markup)
                .build();
        messageSender.sendPhoto(sendPhoto);
    }

    private void setAttributesAndCallSendPhoto(long chatId, ReplyKeyboardMarkup markup, File image) {
        SendPhoto sendPhoto = SendPhoto.builder()
                .photo(new InputFile(image))
                .chatId(chatId)
                .replyMarkup(markup)
                .build();
        messageSender.sendPhoto(sendPhoto);
    }

    private void setAttributesAndCallSendMessage(long chatId, ReplyKeyboardMarkup markup, String responseTextMessage) {
        SendMessage outputTextMessage = SendMessage.builder()
                .text(responseTextMessage)
                .parseMode("HTML")
                .chatId(chatId)
                .replyMarkup(markup)
                .build();
        messageSender.sendMessage(outputTextMessage);
    }
}
