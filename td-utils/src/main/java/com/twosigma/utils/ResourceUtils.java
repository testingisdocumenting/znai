package com.twosigma.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author mykola
 */
public class ResourceUtils {
    private ResourceUtils() {
    }

    /**
     * textual content from the classpath by resource path
     * @param resourcePath resource path like path/to/meta.json
     * @return text content of the resource
     */
    public static String textContent(String resourcePath) {
        InputStream stream = ResourceUtils.class.getClassLoader().getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new IllegalArgumentException("can't find resource: " + resourcePath);
        }

        try {
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * list of text contents from the classpath by resource path. If you have multiple jars/zips and each of them have
     * file-name.txt, this method will give you content of each of the file
     *
     * @param resourcePath resource path like path/to/bundle.txt
     * @return list of text contents
     */
    public static List<String> textContents(String resourcePath) {
        List<String> contents = new ArrayList<>();

        try {
            Enumeration<URL> resources = ResourceUtils.class.getClassLoader().getResources(resourcePath);
            while (resources.hasMoreElements()) {
                contents.add(IOUtils.toString(resources.nextElement(), StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (contents.isEmpty()) {
            throw new IllegalArgumentException("can't find resource: " + resourcePath);
        }

        return contents;
    }
}
