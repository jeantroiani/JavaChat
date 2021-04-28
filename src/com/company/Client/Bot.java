package com.company.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Objects;

import static com.company.Helpers.Constants.Messages.Messages.CONNECTION_SUCCESSFUL;
import static com.company.Helpers.Constants.Messages.Messages.SERVER_NOT_RUNNING_ON_ADDRESS;
import static com.company.Helpers.Format.logError;
import static com.company.Helpers.Format.logInformation;

public class Bot {
    private final String address;
    private final Integer port;
    private String name;

    public Bot(String address, Integer port) {
        this.address = address;
        this.port = port;
        this.name = "BOT";
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
            PrintWriter writeTo = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader readFrom = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BotThread botThread = new BotThread(readFrom, writeTo);
            submitName(socket, writeTo);
            botThread.start();
        } catch (ConnectException connectException) {
            logError(SERVER_NOT_RUNNING_ON_ADDRESS);
        } catch (IOException ioException) {
            System.out.println(ioException);
            ioException.printStackTrace();
        }
    }

    public void setName(String name) {
        this.name = name;
    }
}
