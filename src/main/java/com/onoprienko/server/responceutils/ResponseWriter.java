package com.onoprienko.server.responceutils;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.onoprienko.server.entity.StatusCode.*;


public class ResponseWriter {
    private static final String HTTP_VERSION = "HTTP/1.1 ";


    public static void writeResponseToClient(BufferedWriter writer, InputStream inputStream) {
        try {
            writer.write(HTTP_VERSION + OK);

            byte[] buffer = new byte[4800];
            while ((inputStream.read(buffer)) > 0) {
                String content = new String(buffer, StandardCharsets.UTF_8);
                writer.write(content);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeErrorResponseToClient(String exceptionType, BufferedWriter writer) {
        try {
            switch (exceptionType) {
                case "IllegalArgumentException" -> writer.write(HTTP_VERSION + BAD_REQUEST);
                case "IllegalStateException" -> writer.write(HTTP_VERSION + METHOD_NOT_ALLOWED);
                case "FileNotFoundException" -> writer.write(HTTP_VERSION + NOT_FOUND);
                default -> writer.write(HTTP_VERSION + INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
