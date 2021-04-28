package com.company;


import com.company.Client.Bot;
import com.company.Helpers.Arguments.Arguments;
import com.company.Helpers.Exceptions.InvalidArgumentException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.company.Helpers.Constants.AppValues.*;
import static com.company.Helpers.Constants.Messages.Messages.VALID_PORT_NUMBERS;
import static com.company.Helpers.Format.logInformation;

public class ChatBot {

    public static void main(String[] args) throws InvalidArgumentException {
        Integer port = DEFAULT_SERVER_PORT;
        String address = DEFAULT_IP_ADDRESS;

        if (args.length > 1) {
            List<String> listOfValidArguments = new ArrayList<>(Arrays.asList(CLIENT_PORT_NUMBER_FLAG, CLIENT_ADDRESS_NUMBER_FLAG));
            Map<String, Object> map = Arguments.getArgumentsMap(args, listOfValidArguments);

            if (map.containsKey(CLIENT_PORT_NUMBER_FLAG)) {
                port = Integer.parseInt(map.get(CLIENT_PORT_NUMBER_FLAG).toString());
            }

            if (map.containsKey(CLIENT_ADDRESS_NUMBER_FLAG)) {
                address = String.valueOf(map.get(CLIENT_ADDRESS_NUMBER_FLAG).toString());
            }
        }

        try {
            logInformation("Connecting to " + address + ":" + port);
            new Bot(address, port);
        } catch (NumberFormatException numberFormatException) {
            throw new InvalidArgumentException(VALID_PORT_NUMBERS, numberFormatException);
        }
    }
}
