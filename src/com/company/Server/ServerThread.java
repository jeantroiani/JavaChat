package com.company.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Objects;

import static com.company.Helpers.Constants.AppValues.LOGOUT_KEYWORD;
import static com.company.Helpers.Constants.Messages.Messages.*;
import static com.company.Helpers.Format.*;

public class ServerThread extends Thread {
    private String clientId;
    private Socket socket;
    private BufferedReader readFrom;
    private PrintWriter sendTo;
    private Boolean clientLogged;

    public ServerThread(Socket socket, BufferedReader readFrom, PrintWriter sendTo, String clientId) throws IOException {
        setSocket(socket);
        setReadFrom(readFrom);
        setSendTo(sendTo);
        setClientId(clientId);
        setClientLogged(true);
    }

    private Boolean isPrivateMessage(String string) {
        return string.charAt(0) == '*';
    }

    private void sendPrivateMessage(String message) {
        ArrayList<String> recipientAndMessage = getRecipientAndMessage(message);
        ServerThread recipientId = Server.clientsConnected.get(recipientAndMessage.get(0));
        if (Objects.nonNull(recipientId)) {
            recipientId.sendTo.println(clientId + ": " + recipientAndMessage.get(1));
        } else {
            sendTo.println(RECIPIENT_NOT_AVAILABLE);
        }
    }

    private void sendPublicMessage(String message) {
        Server.clientsConnected.forEach((clientConnected, serverThread) -> {
            if (!clientConnected.equals(clientId)) {
                serverThread.sendTo.println(clientId + ": " + message);
            }
        });
    }

    private void broadcastMessage(String message) {
        if (Server.clientsConnected.isEmpty() || message.isEmpty()) return;
        if (isPrivateMessage(message)) {
            sendPrivateMessage(message);
        } else {
            sendPublicMessage(message);
        }
    }

    private void removeClientFromList() {
        Server.clientsConnected.remove(clientId, this);
    }

    public void logOff() {
        try {
            setClientLogged(false);
            socket.close();
        } catch (IOException ioException) {
            logError(SOCKET_CONNECTION_FAILED);
        }
    }

    @Override
    public void run() {
        try {
            String inputLine = readFrom.readLine();
            clientLogged = true;
            while (getClientLogged()) {
                if (inputLine == null) continue;
                if (inputLine.equalsIgnoreCase(LOGOUT_KEYWORD)) {
                    logOff();
                    logInformation(SOCKET_CLOSED + " Client ID: " + clientId);
                    removeClientFromList();
                    break;
                }
                broadcastMessage(inputLine);
                inputLine = readFrom.readLine();
            }
        } catch (SocketException socketException) {
            removeClientFromList();
            logInformation(SOCKET_CLOSED + " Client ID: " + clientId);
        } catch (IOException ioException) {
            logError(SOCKET_CONNECTION_FAILED);
        }
    }

    public void setReadFrom(BufferedReader readFrom) {
        this.readFrom = readFrom;
    }

    public void setSendTo(PrintWriter sendTo) {
        this.sendTo = sendTo;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Boolean getClientLogged() {
        return clientLogged;
    }

    public void setClientLogged(Boolean clientLogged) {
        this.clientLogged = clientLogged;
    }
}
