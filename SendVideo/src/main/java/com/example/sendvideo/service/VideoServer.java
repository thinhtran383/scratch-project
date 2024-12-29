package com.example.sendvideo.service;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VideoServer {

    private static final int PORT = 8889;
    private static final String DIR = "src/main/java/com/example/sendvideo/service/logs/";
    private static final String SERVER_FILES_DIRECTORY = "src/main/java/com/example/sendvideo/service/receive/";
    private static final String SERVER_URL = "http://localhost:8080/";

    private static final List<ClientInfo> connectedClients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                InetAddress clientAddress = clientSocket.getInetAddress();
                int clientPort = clientSocket.getPort();
                String clientKey = clientAddress.toString() + ":" + clientPort;

                if (!isClientConnected(clientKey)) {
                    connectedClients.add(new ClientInfo(clientAddress, clientPort, clientSocket));
                    System.out.println("New client connected: " + clientKey);
                }

                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            String fileName = inputStream.readUTF();
            long fileSize = inputStream.readLong();

            if (fileName.equals("Connected")) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
                String formatDateTime = now.format(formatter);
                FileOutputStream fos = new FileOutputStream(DIR + fileName + formatDateTime);
                fos.close();
            } else {
                String uuid = UUID.randomUUID().toString();
                String uuidFileName = uuid + "_" + fileName;
                BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(uuidFileName));

                byte[] buffer = new byte[10000];
                long totalBytesRead = 0;

                while (totalBytesRead < fileSize) {
                    int bytesRead = inputStream.read(buffer);
                    fileOutputStream.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                }

                fileOutputStream.close();
                System.out.println("File " + fileName + " received and saved as " + uuidFileName);

                String fileUrl = SERVER_URL + uuidFileName;

                List<ClientInfo> disconnectedClients = new ArrayList<>();
                for (ClientInfo client : connectedClients) {
                    try {
                        if (client.getSocket().isClosed()) {
                            disconnectedClients.add(client);
                            continue;
                        }
                        DataOutputStream outputStream = new DataOutputStream(client.getSocket().getOutputStream());
                        outputStream.writeUTF(fileUrl);
                        System.out.println("Sent video name to client " + client.getAddress() + ":" + client.getPort() + ": " + fileUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                connectedClients.removeAll(disconnectedClients);
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isClientConnected(String clientKey) {
        for (ClientInfo client : connectedClients) {
            if (client.getKey().equals(clientKey)) {
                return true;
            }
        }
        return false;
    }

    private static class ClientInfo {
        private InetAddress address;
        private int port;
        private Socket socket;

        public ClientInfo(InetAddress address, int port, Socket socket) {
            this.address = address;
            this.port = port;
            this.socket = socket;
        }

        public InetAddress getAddress() {
            return address;
        }

        public int getPort() {
            return port;
        }

        public Socket getSocket() {
            return socket;
        }

        public String getKey() {
            return address.toString() + ":" + port;
        }
    }

}
