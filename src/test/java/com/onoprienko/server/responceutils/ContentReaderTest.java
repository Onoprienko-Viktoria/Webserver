package com.onoprienko.server.responceutils;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class ContentReaderTest {
    private final String CSS_CONTENT = """
            body{
                padding-top: 100px;
                background-color: dimgray;
                text-align: center;
            }

            h1{
                font-size: 76px;
                color: lightpink;
            }""";

    @Test
    void readContentReadContentCorrect() throws IOException {
        String uri = "/css/styles.css";
        String path = "./src/main/resources/";
        InputStream inputStream = ContentReader.readContent(uri, path);
        byte[] bytes = inputStream.readAllBytes();
        assertNotNull(bytes);
        assertEquals(CSS_CONTENT, new String(bytes));
    }

    @Test
    void readContentReturnFileNotFoundExceptionIfWrongUri() {
        String uri = "/cadfasf";
        String path = "./src/main/resources/";
        assertThrows(FileNotFoundException.class, () -> ContentReader.readContent(uri, path));
    }

    @Test
    void readContentReturnFileNotFoundExceptionIfWrongPath() {
        String uri = "/css/styles.css";
        String path = "./src/main/resources/a/";
        assertThrows(FileNotFoundException.class, () -> ContentReader.readContent(uri, path));
    }
}