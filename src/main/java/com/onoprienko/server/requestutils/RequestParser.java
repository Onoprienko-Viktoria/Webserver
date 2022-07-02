package com.onoprienko.server.requestutils;


import com.onoprienko.server.entity.HttpMethod;
import com.onoprienko.server.entity.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class RequestParser {
    private final static String HEADER_REGEX = ": ";

    public static Request parse(BufferedReader reader) throws IOException {
        Request request = new Request();
        injectUriAndMethod(reader, request);
        injectHeaders(reader, request);
        return request;
    }


    static void injectUriAndMethod(BufferedReader reader, Request request) throws IOException {
        String line = reader.readLine();
        if (line == null || line.isEmpty()) {
            throw new IllegalArgumentException("Exception while parse request: request is empty");
        }
        String[] splitLine = line.split(" ");

        if (splitLine.length < 3) {
            throw new IllegalArgumentException("Exception while parse request: wrong request signature");
        }

        if (splitLine[0].equals("GET")) {
            request.setMethod(HttpMethod.GET);
            request.setUri(splitLine[1]);
        } else {
            throw new IllegalArgumentException("Exception while parse request: method not allowed");
        }


    }

    static void injectHeaders(BufferedReader reader, Request request) {
        try {
            Map<String, String> headers = new HashMap<>();
            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
                String[] splitLine = line.split(HEADER_REGEX);
                headers.put(splitLine[0], splitLine[1]);
                line = reader.readLine();
            }
            request.setHeaders(headers);
        } catch (Exception e) {
            throw new IllegalArgumentException("Exception while inject headers: " + e.getMessage());
        }
    }
}
