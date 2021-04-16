package com.company.Helpers;

import static com.company.Helpers.Constants.ConsoleColours.*;

public class Format {

    static public void log(String message, String colour) {
        System.out.println(colour + message + ANSI_RESET);
    }

    static public void logError (String message) {
        log(message, ANSI_RED);
    }

    static public void logWarning (String message) { log(message, ANSI_YELLOW); }

    static public void logUserMessage(String message) {
        log(message, ANSI_PURPLE);
    }

    static public void logInformation(String message) { log(message, ANSI_CYAN); }

}
