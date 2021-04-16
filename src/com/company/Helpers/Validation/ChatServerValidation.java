package com.company.Helpers.Validation;

import com.company.Helpers.Exceptions.InvalidArgumentException;

import java.util.Objects;

import static com.company.Helpers.Constants.Messages.Messages.VALID_PORT_NUMBERS;

public class ChatServerValidation {
    public static void isValidPort (Integer portNumber) throws InvalidArgumentException {
        if (Objects.isNull(portNumber)) {
            throw new InvalidArgumentException("Port cannot be null");
        }

        if (portNumber < 1024 || portNumber > 49151) {
            throw new InvalidArgumentException(VALID_PORT_NUMBERS);
        }
    }
}
