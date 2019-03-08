import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.stanczyk.controller.Controller;
import pl.stanczyk.service.FileManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// UNFINISHED //

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = {Controller.class, FileManager.class})
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldWork() throws Exception {
        mockMvc.perform(get("/files"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
