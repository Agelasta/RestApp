package pl.stanczyk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    private FileManager manager;

    @Autowired
    public Controller(FileManager manager) {
        this.manager = manager;
    }

    @RequestMapping(value = "/files", method = RequestMethod.POST)
    public Response createText(@RequestBody FileObj file) {

            String action = file.getInfo();
            Response response = null;

            if(action == null) {
                response = new Message("Incomplete data. Enter action to perform and try again");
            }
            else {
                if (action.equals("create")) {
                    response = manager.createText(file);
                } else if (action.equals("append")) {
                    response = manager.appendText(file);
                }
            }
        return response;
    }

    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public Response readFile(@RequestParam(value = "name", defaultValue = "") String fileName) {
        if (fileName.equals("")) {
            return new Message("Incomplete data. Enter file name and try again");
        }
        else
        return manager.readText(fileName);
    }
}
