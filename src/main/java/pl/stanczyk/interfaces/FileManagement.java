package pl.stanczyk.interfaces;

import pl.stanczyk.data.FileObj;
import pl.stanczyk.data.Response;

public interface FileManagement {

    Response createText(FileObj obj);
    Response readText(String name);
}
