package com.company.Helpers;

import java.util.ArrayList;
import java.util.Arrays;

import static com.company.Helpers.Constants.ConsoleColours.*;

public class Format {

    static public void log(String message, String colour) {
        System.out.println(colour + message + ANSI_RESET);
    }

    static public void logError(String message) {
        log(message, ANSI_RED);
    }

    static public void logWarning(String message) {
        log(message, ANSI_YELLOW);
    }

    static public void logUserMessage(String message) {
        log(message, ANSI_PURPLE);
    }

    static public void logInformation(String message) {
        log(message, ANSI_CYAN);
    }

    static public ArrayList<String> getRecipientAndMessage(String string) {
        int idx = 2;
        while (idx < string.length()) {
            if (Character.isWhitespace(string.charAt(idx))) {
                break;
            }
            idx++;
        }
        String recipientName = string.substring(1, idx);
        String message = string.substring(idx + 1);

        return new ArrayList<>(Arrays.asList(recipientName, message));
    }
}
