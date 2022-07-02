package com.onoprienko.server.responceutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ContentReader {

    public static InputStream readContent(String uri, String path) throws FileNotFoundException {
        File resource = new File(path, uri);
        return new FileInputStream(resource);
    }
}
