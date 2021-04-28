package com.company;


import com.company.Client.Client;
import com.company.Helpers.Arguments.Arguments;
import com.company.Helpers.Exceptions.InvalidArgumentException;

import java.util.*;

import static com.company.Helpers.Constants.AppValues.*;
import static com.company.Helpers.Constants.Messages.Messages.VALID_PORT_NUMBERS;
import static com.company.Helpers.Format.logInformation;

public class ChatClient {

    public static void main(String[] args) throws InvalidArgumentException {
        Integer port = DEFAULT_SERVER_PORT;
        String address = DEFAULT_IP_ADDRESS;
        String name = null;

        if (args.length > 1) {
            List<String> listOfValidArguments = new ArrayList<>(Arrays.asList(CLIENT_PORT_NUMBER_FLAG, CLIENT_ADDRESS_NUMBER_FLAG, CLIENT_NAME_FLAG));
            Map<String, Object> map = Arguments.getArgumentsMap(args, listOfValidArguments);

            if (map.containsKey(CLIENT_PORT_NUMBER_FLAG)) {
                port = Integer.parseInt(map.get(CLIENT_PORT_NUMBER_FLAG).toString());
            }

            if (map.containsKey(CLIENT_ADDRESS_NUMBER_FLAG)) {
                address = String.valueOf(map.get(CLIENT_ADDRESS_NUMBER_FLAG).toString());
            }

            if (map.containsKey(CLIENT_NAME_FLAG)) {
                name = String.valueOf(map.get(CLIENT_NAME_FLAG).toString());
            }
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Connecting to ")
                    .append(address)
                    .append(": ")
                    .append(port);

            if (Objects.nonNull(name)) {
                sb.append(" as ")
                        .append(name);
            }

            logInformation(sb.toString());
            new Client(address, port, name);
        } catch (NumberFormatException numberFormatException) {
            throw new InvalidArgumentException(VALID_PORT_NUMBERS, numberFormatException);
        }
    }
}
