package pl.stanczyk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileManager {

    @Value("${filepath}")
    private String path;

    private String createFilePath(String fileName) {
        return path + fileName + ".txt";
    }

    public Response createText(FileObj fileObj) {

        String name = fileObj.getName();
        String text = fileObj.getContent();
        BufferedWriter writer;
        Message message = null;

        try {
            if (name == null || text == null) {
                message = new Message("Incomplete data. Enter all requested data and try again");
            } else {
                String path = createFilePath(name);
                File file = new File(path);

                writer = new BufferedWriter(new FileWriter(file));
                writer.write(text);
                writer.close();

                message = new Message("Text successfully created");
            }
        } catch (IOException e) {
            message = new Message("Error occured. Try again");
        } finally {
            return message;
        }
    }

    public Response appendText(FileObj fileObj) {

        String name = fileObj.getName();
        String text = fileObj.getContent();
        BufferedWriter writer;
        Message message = null;

        try {
        if (name == null|| text == null) {
            message = new Message("Incomplete data. Enter all requested data and try again");
        } else {

            String path = createFilePath(name);
            File file = new File(path);

            if (!file.exists()) {
                message = new Message("File does not exist");
            } else {
                writer = new BufferedWriter(new FileWriter(file, true));
                writer.newLine();
                writer.write(text);
                writer.close();
                message = new Message("Text successfully added");
            }
        }
        } catch (IOException e) {
            message = new Message("Error occured. Try again");
        } finally {
            return message;
        }
    }

    public Response readText(String fileName) {

        String errorMessage = "File does not exist or error occured";
        List<String> content;
        StringBuilder builder = new StringBuilder();
        Response response = null;

        try {
                String filePath = createFilePath(fileName);
                content = Files.readAllLines(Paths.get(filePath), Charset.forName("UTF-8"));

                for (int i = 0; i < content.size() - 1; i++) {
                    builder.append(content.get(i)).append(" ");
                }
                builder.append(content.get(content.size() - 1));

                String fileContent = builder.toString();
                response = new FileObj(fileName, filePath, fileContent);
        }
        catch (IOException e) {
            response = new Message(errorMessage);
        } finally {
            return response;
        }
    }
}
