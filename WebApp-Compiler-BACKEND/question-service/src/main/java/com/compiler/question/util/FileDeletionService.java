package com.compiler.question.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileDeletionService {

	private static Logger logger = LogManager.getLogger(FileDeletionService.class);
    public void deleteFilesWithPrefix(String directoryPath, String filePrefix) {
        Path directory = Paths.get(directoryPath);

        try {
            Files.walk(directory)
                    .filter(path -> path.getFileName().toString().startsWith(filePrefix))
                    .forEach(this::deleteFile);

            logger.info("Files starting with '" + filePrefix + "' deleted successfully.");
        } catch (IOException e) {
            System.err.println("Error deleting files: " + e.getMessage());
        }
    }

    private void deleteFile(Path file) {
        try {
            Files.delete(file);
         logger.info("Deleted file: " + file.getFileName());
        } catch (IOException e) {
            logger.error("Failed to delete file: " + file.getFileName());
        }
    }

}
