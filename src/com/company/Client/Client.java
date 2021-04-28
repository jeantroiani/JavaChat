package com.company.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Objects;

import static com.company.Helpers.Constants.AppValues.LOGOUT_KEYWORD;
import static com.company.Helpers.Constants.Messages.Messages.*;
import static com.company.Helpers.Format.*;

public class Client {
    private final String address;
    private final Integer port;
    private Boolean userLogged = false;
    private String name;

    public Client(String address, Integer port, String name) {
        this.address = address;
        this.port = port;
        this.name = name;
        start();
    }

    private void submitName(Socket socket, PrintWriter writeTo) {
        if (Objects.isNull(name)) {
            setName(socket.getLocalSocketAddress().toString());
        }
        writeTo.println(name);
    }

    private void start() {

        try {
            Socket socket = new Socket(address, port);
            logInformation(CONNECTION_SUCCESSFUL);
            BufferedReader commandLineReader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter writeTo = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader readFrom = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            setUserLogged(true);

            submitName(socket, writeTo);

            Thread send = new Thread(() -> {
                while (isUserLogged()) {
                    String userInput;
                    try {
                        userInput = commandLineReader.readLine();
                        if (userInput == null) continue;
                        if (userInput.equalsIgnoreCase(LOGOUT_KEYWORD)) {
                            setUserLogged(false);
                            writeTo.println(LOGOUT_KEYWORD);
                            continue;
                        }
                        writeTo.println(userInput);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        break;
                    }
                }
            });

            Thread receive = new Thread(() -> {
                while (isUserLogged()) {
                    try {
                        String message = readFrom.readLine();
                        if (message == null) {
                            setUserLogged(false);
                            socket.close();
                            logWarning(CONNECTION_CLOSED);
                            break;
                        }
                        logUserMessage(message);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        break;
                    }
                }
            });

            send.start();
            receive.start();
        } catch (ConnectException connectException) {
            logError(SERVER_NOT_RUNNING_ON_ADDRESS);
        } catch (IOException ioException) {
            System.out.println(ioException);
            ioException.printStackTrace();
        }
    }

    public Boolean isUserLogged() {
        return userLogged;
    }

    public void setUserLogged(Boolean userLogged) {
        this.userLogged = userLogged;
    }

    public void setName(String name) {
        this.name = name;
    }
}
