package com.company.Helpers.Constants.Messages;

import java.util.HashMap;
import java.util.Map;

public class ChatBotResponse {

    public static Map<String, String> getMessageMap() {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("Hello", "Hiya!");
        messageMap.put("Good bye", "See ya!");
        return messageMap;
    }
}
