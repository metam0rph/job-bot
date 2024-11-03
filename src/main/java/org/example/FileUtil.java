package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static String getOrCreate(String path1){
        Path path = Paths.get(path1);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("Data folder created at: " + path);
            } catch (IOException e) {
                System.out.println("Failed to create data folder. Error: " + e.getMessage());
            }
        } else {
            System.out.println("Data folder already exists at: " + path);
        }
        return path1;
    }
}
