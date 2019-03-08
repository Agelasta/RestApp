package pl.stanczyk.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.stanczyk.data.FileObj;
import pl.stanczyk.data.Message;
import pl.stanczyk.data.Response;
import pl.stanczyk.interfaces.FileManagement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileManager implements FileManagement {

    public final static String ERROR = "Error occured. Try again";
    public final static String ERROR_ACTION = "No action found. Choose action to perform and try again";
    public final static String ERROR_DATA = "Incomplete data. Enter all requested data and try again";
    public final static String ERROR_READ = "File does not exist or error occured";
    public final static String ERROR_NAME = "No name found. Enter file name and try again";
    public final static String ERROR_NOT_EXIST = "File does not exist";
    public final static String SUCCESS_ADD = "Text successfully added";
    public final static String SUCCESS_CREATE = "Text successfully created";
    private final static String APPEND = "append";
    private final static String CREATE = "create";
    private final static String EMPTY = "";

    @Value("${filepath}")
    private String path;

    private String createFilePath(String fileName) {
        return path + fileName + ".txt";
    }

    public Response createText(FileObj file) {

        String action = file.getInfo();
        Response response;

        if(action == null) {
            response = new Message(ERROR_ACTION);
        }
        else {
            if (CREATE.equals(action)) {
                response = createNewFile(file);
            }
            else if (APPEND.equals(action)) {
                response = appendText(file);
            }
            else {
                response = new Message(ERROR);
            }
        }
        return response;
    }

    private Response createNewFile(FileObj fileObj) {

        String name = fileObj.getName();
        String text = fileObj.getContent();
        String path = createFilePath(name);
        File file = new File(path);
        Message message;

        if (name == null || text == null) {
            message = new Message(ERROR_DATA);
        }
        else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(text);
                message = new Message(SUCCESS_CREATE);
            }
            catch (IOException e) {
                message = new Message(ERROR);
            }
        }
        return message;
    }

    private Response appendText(FileObj fileObj) {

        String name = fileObj.getName();
        String text = fileObj.getContent();
        String path = createFilePath(name);
        File file = new File(path);
        Message message;

        if (name == null || text == null) {
            message = new Message(ERROR_DATA);
        }
        else {
            if (!file.exists()) {
                message = new Message(ERROR_NOT_EXIST);
            }
            else {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                    writer.newLine();
                    writer.write(text);
                    message = new Message(SUCCESS_ADD);
                } catch (IOException e) {
                    message = new Message(ERROR);
                }
            }
        }
        return message;
    }

    public Response readText(String fileName) {

        StringBuilder builder = new StringBuilder();
        Response response;

        if (EMPTY.equals(fileName)) {
            return new Message(ERROR_NAME);
        }
        else {
            try {
                String filePath = createFilePath(fileName);
                List<String> content = Files.readAllLines(Paths.get(filePath), Charset.forName("UTF-8"));

                for (int i = 0; i < content.size() - 1; i++) {
                    builder.append(content.get(i)).append(" ");
                }
                builder.append(content.get(content.size() - 1));

                String fileContent = builder.toString();

                response = new FileObj(fileName, filePath, fileContent);
            }
            catch (IOException e) {
                response = new Message(ERROR_READ);
            }
        }
        return response;
    }
}
