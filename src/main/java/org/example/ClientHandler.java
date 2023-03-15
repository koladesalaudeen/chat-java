package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private String clientName;
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.output = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            clientName = input.readLine();
            System.out.println("Client " + clientName + " connected.");
            ChatServer.broadcast(clientName + " joined the chat.", this);
            String message;
            while ((message = input.readLine()) != null) {
                ChatServer.broadcast(message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ChatServer.removeClient(this);
            try {
                input.close();
                output.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        output.println(message);
    }

    public String getClientName() {
        return clientName;
    }
}
