package com.company;

import com.company.Helpers.Arguments.Arguments;
import com.company.Helpers.Constants.AppValues;
import com.company.Helpers.Exceptions.InvalidArgumentException;
import com.company.Server.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.company.Helpers.Constants.AppValues.SERVER_PORT_NUMBER_FLAG;
import static com.company.Helpers.Constants.Messages.Messages.VALID_PORT_NUMBERS;
import static com.company.Helpers.Format.logInformation;
import static com.company.Helpers.Validation.ChatServerValidation.isValidPort;

public class ChatServer {

    public static void main(String[] args) throws InvalidArgumentException, IOException {
        Integer port = AppValues.DEFAULT_SERVER_PORT;

        if (args.length > 1) {
            List<String> listOfValidArguments = new ArrayList<>(Collections.singletonList(SERVER_PORT_NUMBER_FLAG));
            Map<String, Object> map = Arguments.getArgumentsMap(args, listOfValidArguments);
            try {
                Integer parsedPort = Integer.parseInt(map.get(SERVER_PORT_NUMBER_FLAG).toString());
                isValidPort(parsedPort);
                port = parsedPort;
            } catch (NumberFormatException numberFormatException) {
                throw new InvalidArgumentException(VALID_PORT_NUMBERS, numberFormatException);
            }
        }

        logInformation("Server started running on port number: " + port);
        new Server(port);
    }
}
