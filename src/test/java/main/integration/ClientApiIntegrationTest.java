package main.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = "ADMIN")
public class ClientApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {

        String sql = """
                TRUNCATE TABLE client_notes, clients
                RESTART IDENTITY CASCADE
                """;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.execute();
        }
    }

    @Test
    void createClient_shouldReturnSavedClient() throws Exception {

        String json = """
                {
                  "name": "Lev",
                  "phone": "123456",
                  "email": "lev@gmail.com"
                }
                """;

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Lev"))
                .andExpect(jsonPath("$[0].phone").value("123456"))
                .andExpect(jsonPath("$[0].email").value("lev@gmail.com"));
    }

    @Test
    void getClientById_shouldReturnClient() throws Exception {

        createClient("Lev", "1234", "lev@gmail.com");

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Lev"))
                .andExpect(jsonPath("$.phone").value("1234"))
                .andExpect(jsonPath("$.email").value("lev@gmail.com"));
    }

    @Test
    void getClientById_shouldReturnNotFound() throws Exception {

        mockMvc.perform(get("/api/clients/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createClient_emptyName_shouldReturnBadRequest()
            throws Exception {

        String json = """
            {
              "name": "",
              "phone": "123456",
              "email": "lev@gmail.com"
            }
            """;

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Name cannot be empty"));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void updateClient_existingClient_shouldUpdateClient()
            throws Exception {

        createClient("Lev", "123456", "lev@gmail.com");

        String updateJson = """
            {
              "name": "Updated Lev",
              "phone": "999999",
              "email": "updated@gmail.com"
            }
            """;

        mockMvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Lev"))
                .andExpect(jsonPath("$.phone").value("999999"))
                .andExpect(jsonPath("$.email").value("updated@gmail.com"));

    }

    @Test
    void updateClient_nonExistingClient_shouldReturnNotFound()
            throws Exception {

        String updateJson = """
            {
              "name": "Lev",
              "phone": "123456",
              "email": "lev@gmail.com"
            }
            """;

        mockMvc.perform(put("/api/clients/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteClient_existingClient_shouldDeleteClient()
            throws Exception {

        createClient( "Lev","123456","lev@gmail.com");

        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deleteClient_nonExistingClient_shouldReturnNotFound()
            throws Exception {

        mockMvc.perform(delete("/api/clients/999"))
                .andExpect(status().isNotFound());
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
}
