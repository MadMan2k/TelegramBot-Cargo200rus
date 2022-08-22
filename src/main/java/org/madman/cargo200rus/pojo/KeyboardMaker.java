package org.madman.cargo200rus.pojo;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardMaker {

    public static ReplyKeyboardMarkup make(List<String> keyboardButtons) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        for (String s : keyboardButtons) {
            row.add(s);
        }
        keyboardRows.add(row);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);
        return markup;
    }
}
