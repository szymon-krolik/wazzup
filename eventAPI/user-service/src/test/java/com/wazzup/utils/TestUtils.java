package com.wazzup.utils;

import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestUtils {
    public static String readJsonFromFile(String path, ResourceLoader resourceLoader) throws IOException {
        return new String(Files.readAllBytes(Path.of(resourceLoader.getResource(path).getURI())));
    }
}
