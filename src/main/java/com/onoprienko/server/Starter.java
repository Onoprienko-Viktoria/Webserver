package com.onoprienko.server;

public class Starter {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.setPort(5000);
        server.setWebappPath("./src/main/resources/");
        server.start();
    }
}
