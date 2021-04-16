package com.company.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import static com.company.Helpers.Constants.AppValues.SHUT_SERVER;
import static com.company.Helpers.Constants.Messages.Messages.SERVER_OFFLINE;
import static com.company.Helpers.Constants.Messages.Messages.SOCKET_CLOSED;
import static com.company.Helpers.Format.logInformation;

public class Server {
    private Integer port;
    private Boolean serviceOn;
    private ServerSocket serverSocket;
    public static Map<String, ServerThread> clientsConnected;
    private BufferedReader commandLineReader = new BufferedReader(new InputStreamReader(System.in));

    public Server(Integer port) {
        setPort(port);
        clientsConnected = new HashMap<>();
        try {
            setServerSocket(new ServerSocket(this.port));
            setServiceOn(true);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        start();
    }

    public void closeServerSocket() throws IOException {
        if (!clientsConnected.isEmpty()) {
            clientsConnected.forEach((clientId, serverThread) -> {
                serverThread.logOff();
            });
        }
        serverSocket.close();
    }

    Thread serverConsole = new Thread(() -> {
        while (getServiceOn()) {
            String userInput;
            try {
                userInput = commandLineReader.readLine();
                if (userInput == null) {
                    continue;
                }
                if (userInput.equalsIgnoreCase(SHUT_SERVER)) {
                    setServiceOn(false);
                    closeServerSocket();
                    break;
                }
            }  catch (IOException ioException) {
                ioException.printStackTrace();
                break;
            }
        }
    });

    private void start () {
        serverConsole.start();

        while (getServiceOn()) {
            try {
                Socket clientSocket = serverSocket.accept();
                BufferedReader readFrom = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter sendTo = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
                String clientId = readFrom.readLine();
                ServerThread serverThread = new ServerThread(clientSocket, readFrom, sendTo, clientId);
                clientsConnected.put(clientId, serverThread);
                serverThread.start();
            } catch (SocketException socketException) {
                logInformation(SOCKET_CLOSED + ": " + serverSocket.getLocalSocketAddress());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        logInformation(SERVER_OFFLINE);

    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public static Map<String, ServerThread> getClientsConnected() {
        return clientsConnected;
    }

    public static void setClientsConnected(Map<String, ServerThread> clientsConnected) {
        Server.clientsConnected = clientsConnected;
    }

    public Boolean getServiceOn() {
        return serviceOn;
    }

    public void setServiceOn(Boolean serviceOn) {
        this.serviceOn = serviceOn;
    }
}
