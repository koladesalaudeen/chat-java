package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;

    private String clientName;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public ChatClient(String clientName) {
        this.clientName = clientName;
    }

    public void start() throws IOException {
        socket = new Socket(HOSTNAME, PORT);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);

        output.println(clientName);
        System.out.println("Connected to server. Start typing messages.");

        new Thread(new ConsoleReader()).start();

        String message;
        while ((message = input.readLine()) != null) {
            System.out.println(message);
        }

        input.close();
        output.close();
        socket.close();
        System.out.println("Disconnected from server.");
    }

    private class ConsoleReader implements Runnable {
        @Override
        public void run() {
            //Input from clients' keyboard
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            try {
                while (true) {
                    String message = consoleInput.readLine();
                    output.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.print("Enter your name: ");
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
        String clientName = consoleInput.readLine();

        ChatClient client = new ChatClient(clientName);
        client.start();
    }
}
