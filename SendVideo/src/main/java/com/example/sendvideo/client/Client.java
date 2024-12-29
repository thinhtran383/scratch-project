package com.example.sendvideo.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.*;

public class Client extends JFrame {
    private JButton btnChoose;
    private JButton btnSend;
    private JLabel lbLink;
    private File selectedFile;
    private Socket clientSocket;

    public Client() {
        initComponents();
        connectToServer();
        setTitle("Client 1");
    }

    private void initComponents() {
        btnChoose = new JButton();
        btnSend = new JButton();
        lbLink = new JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnChoose.setText("Chọn tập tin");
        btnChoose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnChooseActionPerformed(evt);
            }
        });

        btnSend.setText("Gửi");
        btnSend.setEnabled(false);
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        lbLink.setText("Chọn một tập tin để gửi.");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(138, 138, 138)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnSend, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnChoose, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(153, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbLink)
                                .addGap(127, 127, 127))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(lbLink)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                                .addComponent(btnChoose)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSend)
                                .addGap(70, 70, 70))
        );

        pack();
    }

    private void btnChooseActionPerformed(ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            if (selectedFile != null) {
                lbLink.setText("Tập tin đã chọn: " + selectedFile.getName());
                btnSend.setEnabled(true);
            }
        }
    }

    private void connectToServer() {
        try {
            clientSocket = new Socket("localhost", 8889);
            startListening();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startListening() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                        String message = inputStream.readUTF();
                        System.out.println("Received video name from server: " + message);
                        lbLink.setText("");
                        lbLink.setText(message);
                        hyperLink(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendFile(File file) {
        try {
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            // Gửi tên file đến server
            outputStream.writeUTF(file.getName());
            outputStream.writeLong(file.length());

            int bufferSize = 8192;
            byte[] buffer = new byte[bufferSize];

            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                int bytesRead;
                // Đọc dữ liệu từ file vào buffer và gửi đi
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }


            connectToServer();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hyperLink(String path) {
        lbLink.setForeground(Color.BLUE.darker());
        lbLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lbLink.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(path));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    private void btnSendActionPerformed(ActionEvent evt) {
        if (selectedFile != null) {
            sendFile(selectedFile);
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
    }
}
