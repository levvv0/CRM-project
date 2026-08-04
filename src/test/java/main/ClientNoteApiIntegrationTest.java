package main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientNoteApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        String sql = """
                TRUNCATE TABLE client_notes, clients
                RESTART IDENTITY CASCADE
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.executeUpdate();
        }
    }

    @Test
    void createNote_thenGetNotes_shouldReturnSavedNote() throws Exception {

        createClient("Lev","123456","lev@gmail.com");

        createNote(1, "Call client tomorrow");

        mockMvc.perform(get("/api/clients/1/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].clientId").value(1))
                .andExpect(jsonPath("$[0].noteText").value("Call client tomorrow"));
    }

    @Test
    void getNotes_clientWithoutNotes_shouldReturnEmptyList() throws Exception {

        createClient("Lev","123456","lev@gmail.com");

        mockMvc.perform(get("/api/clients/1/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void createNote_emptyText_shouldReturnBadRequest() throws Exception {

        createClient("Lev","123456","lev@gmail.com");

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

        mockMvc.perform(get("/api/clients/1/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void createNote_nonExistingClient_shouldReturnBadRequest() throws Exception {

        String json = """
                {
                  "noteText": "Call client tomorrow"
                }
                """;

        mockMvc.perform(post("/api/clients/999/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Client with this ID not found"));
    }

    @Test
    void deleteNote_existingNote_shouldDeleteNote() throws Exception {

        createClient("Lev","123456","lev@gmail.com");

        createNote(1, "Call client tomorrow");

        mockMvc.perform(delete("/api/notes/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/clients/1/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deleteNote_nonExistingNote_shouldReturnNotFound() throws Exception {

        mockMvc.perform(delete("/api/notes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getNotes_shouldReturnNotesOnlyForSpecifiedClient() throws Exception {

        createClient("Lev","111","lev@gmail.com");

        createClient("Bob","222","bob@gmail.com");

        createNote(1, "Lev note");
        createNote(2, "Bob note");

        mockMvc.perform(get("/api/clients/1/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].clientId").value(1))
                .andExpect(jsonPath("$[0].noteText")
                        .value("Lev note"));

        mockMvc.perform(get("/api/clients/2/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].clientId").value(2))
                .andExpect(jsonPath("$[0].noteText")
                        .value("Bob note"));
    }

    private void createClient(String name, String phone, String email) throws Exception {

        String json = """
                {
                  "name": "%s",
                  "phone": "%s",
                  "email": "%s"
                }
                """.formatted(name, phone, email);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    private void createNote(int clientId, String noteText) throws Exception {

        String json = """
                {
                  "noteText": "%s"
                }
                """.formatted(noteText);

        mockMvc.perform(post("/api/clients/{clientId}/notes",clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }
}