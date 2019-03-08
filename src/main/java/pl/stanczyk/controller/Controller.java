package pl.stanczyk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.stanczyk.data.FileObj;
import pl.stanczyk.data.Response;
import pl.stanczyk.interfaces.FileManagement;

@RestController
public class Controller {

    private FileManagement manager;

    @Autowired
    public Controller(FileManagement manager) {
        this.manager = manager;
    }

    @PostMapping("/files")
    public Response createText(@RequestBody FileObj file) {

        return manager.createText(file);
    }

    @GetMapping("/files")
    public Response readFile(@RequestParam(value = "name", defaultValue = "") String fileName) {

        return manager.readText(fileName);
    }
}
