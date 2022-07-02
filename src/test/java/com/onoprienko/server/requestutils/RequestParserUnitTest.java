package com.onoprienko.server.requestutils;

import com.onoprienko.server.entity.HttpMethod;
import com.onoprienko.server.entity.Request;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequestParserUnitTest {
    private final String testRequest = """
            GET /hello.html HTTP/1.1
            User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)
            Host: www.image4.io
            Accept-Language: en-us
            Accept-Encoding: gzip, deflate
            Connection: Keep-Alive""";

    @Test
    public void testParseRequestReturnCorrectMethodAndUriAndHeaders() throws IOException {
        try (BufferedReader bufferReader = new BufferedReader(new StringReader(testRequest))) {
            Request request = RequestParser.parse(bufferReader);
            assertEquals(HttpMethod.GET, request.getMethod());
            assertEquals("/hello.html", request.getUri());

            Map<String, String> headers = request.getHeaders();
            assertNotNull(headers);
            assertTrue(headers.containsKey("User-Agent"));
            assertTrue(headers.containsKey("Host"));
            assertTrue(headers.containsKey("Accept-Language"));
            assertTrue(headers.containsKey("Accept-Encoding"));
            assertTrue(headers.containsKey("Connection"));
        }
    }

    @Test
    void injectUriAndMethodWorkCorrect() throws IOException {
        try (BufferedReader bufferReader = new BufferedReader(new StringReader(testRequest))) {
            Request request = new Request();
            RequestParser.injectUriAndMethod(bufferReader, request);
            assertEquals(HttpMethod.GET, request.getMethod());
            assertEquals("/hello.html", request.getUri());
        }
    }

    @Test
    void injectUriAndMethodReturnExceptionIfRequestIsEmpty() throws IOException {
        try (BufferedReader bufferReader = new BufferedReader(new StringReader(""))) {
            Request request = new Request();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> RequestParser.injectUriAndMethod(bufferReader, request));
            assertEquals("Exception while parse request: request is empty", exception.getMessage());
        }
    }

    @Test
    void injectUriAndMethodReturnExceptionIfBadRequest() throws IOException {
        try (BufferedReader bufferReader = new BufferedReader(new StringReader("GET ht"))) {
            Request request = new Request();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> RequestParser.injectUriAndMethod(bufferReader, request));
            assertEquals("Exception while parse request: wrong request signature", exception.getMessage());
        }
    }

    @Test
    void injectUriAndMethodReturnExceptionIfMethodNotAllowed() throws IOException {
        try (BufferedReader bufferReader = new BufferedReader(new StringReader("POST /index.html HTTP/1.1"))) {
            Request request = new Request();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> RequestParser.injectUriAndMethod(bufferReader, request));
            assertEquals("Exception while parse request: method not allowed", exception.getMessage());
        }
    }


    @Test
    void injectHeaders() throws IOException {
        String headersRequest = testRequest.substring(testRequest.indexOf("User-Agent"));
        try (BufferedReader bufferReader = new BufferedReader(new StringReader(headersRequest))) {
            Request request = new Request();
            RequestParser.injectHeaders(bufferReader, request);
            Map<String, String> headers = request.getHeaders();
            assertNotNull(headers);
            assertEquals(5, headers.size());

            assertTrue(headers.containsKey("User-Agent"));
            assertTrue(headers.containsKey("Host"));
            assertTrue(headers.containsKey("Accept-Language"));
            assertTrue(headers.containsKey("Accept-Encoding"));
            assertTrue(headers.containsKey("Connection"));

            assertEquals("Mozilla/4.0 (compatible; MSIE5.01; Windows NT)",
                    headers.get("User-Agent"));
            assertEquals("www.image4.io",
                    headers.get("Host"));
            assertEquals("en-us",
                    headers.get("Accept-Language"));
            assertEquals("gzip, deflate",
                    headers.get("Accept-Encoding"));
            assertEquals("Keep-Alive",
                    headers.get("Connection"));
        }
    }

    @Test
    void injectHeadersReturnExceptionIfReaderIsEmpty() throws IOException {
        String headersRequest = testRequest.substring(testRequest.indexOf(""));
        try (BufferedReader bufferReader = new BufferedReader(new StringReader(headersRequest))) {
            Request request = new Request();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> RequestParser.injectHeaders(bufferReader, request));
            assertEquals("Exception while inject headers: Index 1 out of bounds for length 1", exception.getMessage());
        }
    }
}