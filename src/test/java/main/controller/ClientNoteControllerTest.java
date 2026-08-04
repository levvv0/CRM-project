package main.controller;

import main.model.ClientNote;
import main.service.ClientNoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientNoteController.class)
public class ClientNoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    ClientNoteService noteService;

    @Test
    void getClientNotes_emptyList_shouldReturnOkAndEmptyJsonArray() throws Exception{

        when(noteService.findByClientId(1))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/clients/1/notes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(noteService).findByClientId(1);
    }

    @Test
    void getClientNotes_existingNote_shouldReturnNoteJson() throws Exception{

        ClientNote note = new ClientNote.ClientNoteBuilder()
                .SId(10)
                .SClientId(1)
                .SNoteText("Call client tomorrow")
                .build();

        when(noteService.findByClientId(1))
                .thenReturn(List.of(note));

        mockMvc.perform(get("/api/clients/1/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].clientId").value(1))
                .andExpect(jsonPath("$[0].noteText").value("Call client tomorrow"));

        verify(noteService).findByClientId(1);
    }

    @Test
    void createNote_shouldReturnCreated() throws Exception{

        String json = """
                {
                  "noteText": "Call client tomorrow"
                }
                """;

        mockMvc.perform(post("/api/clients/1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));

        verify(noteService).addNote(1,"Call client tomorrow");
    }

    @Test
    void createNote_shouldReturnBadRequest() throws Exception {

        String json = """
                {
                  "noteText": ""
                }
                """;

        mockMvc.perform(post("/api/clients/1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Note text cannot be empty"));

        verifyNoInteractions(noteService);

    }

    @Test
    void deleteNote_shouldReturnNoContent() throws Exception {

        when(noteService.deleteById(1))
                .thenReturn(true);

        mockMvc.perform(delete("/api/notes/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(noteService).deleteById(1);
    }

    @Test
    void deleteNote_shouldReturnNotFound() throws Exception {

        when(noteService.deleteById(1))
                .thenReturn(false);

        mockMvc.perform(delete("/api/notes/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

        verify(noteService).deleteById(1);
    }
}
