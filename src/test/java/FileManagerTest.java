import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.stanczyk.data.FileObj;
import pl.stanczyk.interfaces.FileManagement;
import pl.stanczyk.service.FileManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = FileManager.class)
@TestPropertySource("file:properties/path.properties")
public class FileManagerTest {

    @Value("${filepath}")
    private String path;

    @Autowired
    private FileManagement manager;

    @Test
    public void shouldReturnErrorActionMessage() {
        FileObj actionNull = new FileObj("file", null, "text");
        String response = manager.createText(actionNull).getInfo();

        assertThat(response).isEqualTo(FileManager.ERROR_ACTION);
    }

    @Test
    public void shouldReturnErrorDataNameMessageWhileCreate() {
        FileObj nameNull = new FileObj(null, "create", "text");
        String response = manager.createText(nameNull).getInfo();

        assertThat(response).isEqualTo(FileManager.ERROR_DATA);
    }

    @Test
    public void shouldReturnErrorDataContentMessageWhileCreate() {
        FileObj contentNull = new FileObj("file", "create", null);
        String response = manager.createText(contentNull).getInfo();

        assertThat(response).isEqualTo(FileManager.ERROR_DATA);
    }

    @Test
    public void shouldReturnSuccessCreateMessage() {
        FileObj object = new FileObj("file", "create", "text");
        String response = manager.createText(object).getInfo();

        assertThat(response).isEqualTo(FileManager.SUCCESS_CREATE);
    }

    @Test
    public void shouldCreateFile() throws IOException {
        FileObj object = new FileObj("file", "create", "text");
        String filePath = path + object.getName() + ".txt";
        File file = new File(filePath);

        manager.createText(object);

        assertThat(file).exists();
        Files.delete(Paths.get(filePath));
    }

    @Test
    public void shouldCreateText() throws IOException {
        FileObj object = new FileObj("file", "create", "createdText");
        String filePath = path + object.getName() + ".txt";
        File file = new File(filePath);

        manager.createText(object);

        assertThat(file).hasContent(object.getContent());
        Files.delete(Paths.get(filePath));
    }


    @Test
    public void shouldReturnErrorDataNameMessageWhileAppend() {
        FileObj nameNull = new FileObj(null, "append", "appendedText");
        String response = manager.createText(nameNull).getInfo();

        assertThat(response).isEqualTo(FileManager.ERROR_DATA);
    }

    @Test
    public void shouldReturnErrorDataContentMessageWhileAppend() {
        FileObj contentNull = new FileObj("file", "append", null);
        String response = manager.createText(contentNull).getInfo();

        assertThat(response).isEqualTo(FileManager.ERROR_DATA);
    }

    @Test
    public void shouldReturnErrorNotExistWhileAppend() {

        File file = new File(path);
        String[] list = file.list();
        String nonExistingFileName;

        if(list.length == 0) {
            nonExistingFileName = "anyName";
        } else if(list.length == 1) {
            nonExistingFileName = list[0];
        } else {
            StringBuilder builder = new StringBuilder();

            for (String s : list) {
                builder.append(s);
            }
            nonExistingFileName = builder.toString();
        }
        FileObj object = new FileObj(nonExistingFileName, "append", "appendedText");
        String response = manager.createText(object).getInfo();

        assertThat(response).isEqualTo(FileManager.ERROR_NOT_EXIST);
    }

    @Test
    public void shouldReturnSuccessAddMessage() throws IOException {
        FileObj fileCreated = new FileObj("file", "create", "text");
        manager.createText(fileCreated);

        FileObj textAppended = new FileObj("file", "append", "appendedText");
        String response = manager.createText(textAppended).getInfo();

        assertThat(response).isEqualTo(FileManager.SUCCESS_ADD);
        Files.delete(Paths.get(manager.readText(textAppended.getName()).getInfo()));
    }

    @Test
    public void shouldAppendTextToFile() throws IOException {

        FileObj fileCreated = new FileObj("file", "create", "text");
        manager.createText(fileCreated);

        FileObj textAppended = new FileObj("file", "append", "appendedText");
        String filePath = path + textAppended.getName() + ".txt";
        File file = new File(filePath);

        String currentText = fileCreated.getContent();
        manager.createText(textAppended);

        assertThat(file).hasContent(currentText + System.lineSeparator() + textAppended.getContent());
        Files.delete(Paths.get(filePath));
    }

    @Test
    public void shouldNotCreateFileIfFileDoesNotExistWhileAppend() {

        File file = new File(path);
        String[] list = file.list();
        String nonExistingFileName;

        if(list.length == 0) {
            nonExistingFileName = "anyName";
        } else if(list.length == 1) {
            nonExistingFileName = list[0];
        } else {
            StringBuilder builder = new StringBuilder();

            for (String s : list) {
                builder.append(s);
            }
            nonExistingFileName = builder.toString();
        }
        FileObj object = new FileObj(nonExistingFileName, "append", "appendedText");
        String filePath = path + nonExistingFileName + ".txt";
        File newFile = new File(filePath);

        manager.createText(object);

        assertThat(newFile).doesNotExist();
    }

    @Test
    public void shouldReturnErrorNameMessageWhenNameIsEmptyWhileRead() {
        String response = manager.readText("").getInfo();

        assertThat(response).isEqualTo(FileManager.ERROR_NAME);
    }

    @Test
    public void shouldReadText() throws IOException {
        FileObj object = new FileObj("file", "create", "expectedText");
        manager.createText(object);

        String text = ((FileObj) manager.readText(object.getName())).getContent();

        assertThat(text).isEqualTo(object.getContent());
        Files.delete(Paths.get(manager.readText(object.getName()).getInfo()));
    }

    @Test
    public void shouldReadFileNameAndFilePath() throws IOException {

        FileObj object = new FileObj("expectedFileName", "create", "text");
        String filePath = path + object.getName() + ".txt";
        manager.createText(object);

        String name = ((FileObj) manager.readText(object.getName())).getName();
        String path = manager.readText(object.getName()).getInfo();

        assertThat(name).isEqualTo(object.getName());
        assertThat(path).isEqualTo(filePath);
        Files.delete(Paths.get(filePath));
    }

    @Test
    public void shouldReturnErrorReadMessageWhenFileDoesNotExistWhileRead() {

        File file = new File(path);
        String[] list = file.list();
        String nonExistingFileName;

        if(list.length == 0) {
            nonExistingFileName = "anyName";
        } else if(list.length == 1) {
            nonExistingFileName = list[0];
        } else {
            StringBuilder builder = new StringBuilder();

            for (String s : list) {
                builder.append(s);
            }
            nonExistingFileName = builder.toString();
        }
        String response = manager.readText(nonExistingFileName).getInfo();

        assertThat(response).isEqualTo(FileManager.ERROR_READ);
    }

    @Test
    public void shouldReturnErrorWhenPathDoesNotExistWhileCreate() {
        FileObj object = new FileObj("D:/Java/file", "create", "text");
        String response = manager.createText(object).getInfo();

        assertThat(response).isEqualTo(FileManager.ERROR);
    }

    @Test
    public void shouldReturnErrorWhenPathDoesNotExistWhileAppend() {
        FileObj object = new FileObj("D:/Java/file", "append", "text");
        String response = manager.createText(object).getInfo();

        assertThat(response).isEqualTo(FileManager.ERROR_NOT_EXIST);
    }
}

