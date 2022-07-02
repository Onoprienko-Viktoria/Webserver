package com.onoprienko.server;


import com.onoprienko.server.requestutils.RequestHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;
    private String webappPath;

    public Server() {
        this.port = 3000;
        this.webappPath = "./src/main/resources";
    }

    public Server(int port, String webappPath) {
        this.port = port;
        this.webappPath = webappPath;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setWebappPath(String webappPath) {
        this.webappPath = webappPath;
    }

    public void start() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server start work on port: " + port);

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                    RequestHandler requestHandler = new RequestHandler(socketReader, socketWriter, webappPath);
                    requestHandler.handle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
