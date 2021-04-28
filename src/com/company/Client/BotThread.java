package com.company.Client;

import com.company.Helpers.Constants.Messages.ChatBotResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static com.company.Helpers.Format.getRecipientAndMessage;

public class BotThread extends Thread {
    private final Map<String, String> responseList = ChatBotResponse.getMessageMap();
    BufferedReader readFrom;
    PrintWriter writeTo;

    public BotThread(BufferedReader readFrom, PrintWriter writeTo) throws IOException {
        this.readFrom = readFrom;
        this.writeTo = writeTo;
    }

    public void run() {
        while (true) {
            try {
                String message = readFrom.readLine();
                ArrayList<String> recipientAndMessage = getRecipientAndMessage(message);
                String response = responseList.get(recipientAndMessage.get(1));
                if (Objects.isNull(response)) {
                    response = "Sorry I don't understand you question";
                }
                writeTo.println(response);
            } catch (IOException ioException) {
                ioException.printStackTrace();
                break;
            }
        }
    }
}
