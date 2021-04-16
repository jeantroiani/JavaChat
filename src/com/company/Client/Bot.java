package com.company.Client;

import com.company.Helpers.Constants.Messages.ChatBotResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.company.Helpers.Constants.Messages.Messages.*;
import static com.company.Helpers.Format.*;
import static com.company.Helpers.Format.logError;

public class Bot {
    private final String address;
    private final Integer port;
    private Boolean userLogged = false;
    private final List<String> messagesReceived = new ArrayList();
    private final Map<String, String> responseList = ChatBotResponse.getMessageMap();

    public Bot(String address, Integer port) {
        this.address = address;
        this.port = port;
        start();
    }

    private void closeConnection (Socket socket) {
        try {
            setUserLogged(false);
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
            logWarning(CONNECTION_CLOSED);
        } catch (SocketException socketException) {
            System.out.println(socketException);
        } catch (IOException ioException) {
            System.out.println(ioException);
        }
    }

    private void start () {

        try  {
            Socket socket = new Socket(address, port);
            logInformation(CONNECTION_SUCCESSFUL);
            PrintWriter writeTo = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader readFrom = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            setUserLogged(true);

            Thread send = new Thread(() -> {
                while (isUserLogged()) {
                    String userInput;
                    if (!getMessagesReceived().isEmpty()) {
                        System.out.println(messagesReceived.size());
                        userInput = messagesReceived.get(0);
                        if (userInput == null) continue;
                        System.out.println(userInput);
                        if (responseList.containsKey(userInput)) {
                            writeTo.println(responseList.get(userInput));
                        }
                        messagesReceived.remove(0);
                    }
                }
                System.out.println("thread over");
            });

            Thread receive = new Thread(() -> {
                while (isUserLogged()) {
                    try {
                        String message = readFrom.readLine();
                        if (message == null) {
                            closeConnection(socket);
                            continue;
                        }
                        messagesReceived.add(message);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        break;
                    }
                }
            });

            send.start();
            receive.start();
        } catch(ConnectException connectException) {
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

    public List<String> getMessagesReceived() {
        return messagesReceived;
    }

}
