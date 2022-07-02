package com.onoprienko.server.requestutils;

import com.onoprienko.server.entity.Request;
import com.onoprienko.server.responceutils.ContentReader;
import com.onoprienko.server.responceutils.ResponseWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;


public class RequestHandler {
    private final BufferedReader socketReader;
    private final BufferedWriter socketWriter;
    private final String webappPath;

    public RequestHandler(BufferedReader socketReader, BufferedWriter socketWriter, String webappPath) {
        this.socketReader = socketReader;
        this.socketWriter = socketWriter;
        this.webappPath = webappPath;
    }

    public void handle() {
        try {
            Request request = RequestParser.parse(socketReader);
            InputStream inputStream = ContentReader.readContent(request.getUri(), webappPath);
            ResponseWriter.writeResponseToClient(socketWriter, inputStream);
        } catch (RuntimeException e) {
            System.out.println("Error while writing response to client: ");
            e.printStackTrace();
        } catch (Exception e) {
            ResponseWriter.writeErrorResponseToClient(e.getClass().getSimpleName(), socketWriter);
        }

    }
}
