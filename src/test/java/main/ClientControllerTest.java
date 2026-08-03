package main;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @Test
    void getAllClients_emptyList_shouldReturnOkAndEmptyJson() throws Exception{

            when(clientService.getAllClients())
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/clients"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"));
    }

    @Test
    void getAllClients_existingClient_shouldReturnClientJson() throws Exception{

        Client client = new Client.ClientBuilder()
                .SId(1)
                .SName("Lev")
                .SPhone("123")
                .SEmail("lev@gmail.com")
                .build();

        when(clientService.getAllClients())
                .thenReturn(List.of(client));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Lev"))
                .andExpect(jsonPath("$[0].phone").value("123"))
                .andExpect(jsonPath("$[0].email").value("lev@gmail.com"));
    }

    @Test
    void getClientById_existingClient_shouldReturnClientJson() throws Exception{

        Client client = new Client.ClientBuilder()
                .SId(1)
                .SName("Lev")
                .SPhone("123")
                .SEmail("lev@gmail.com")
                .build();

        when(clientService.findById(1))
                .thenReturn(client);

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Lev"))
                .andExpect(jsonPath("$.phone").value("123"))
                .andExpect(jsonPath("$.email").value("lev@gmail.com"));
    }

    @Test
    void getClientById_nonExistingClient_shouldReturnNotFound() throws Exception{

        when(clientService.findById(1))
                .thenReturn(null);

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void createClient_shouldReturnCreated() throws Exception {

        String json = """
            {
              "name": "Lev",
              "phone": "123",
              "email": "lev@gmail.com"
            }
            """;

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));

        verify(clientService).addClient(
                "Lev",
                "123",
                "lev@gmail.com"
        );
    }

    @Test
    void createClient_shouldReturnBadRequest() throws Exception {

        String json = """
            {
              "name": "",
              "phone": "123",
              "email": "lev@gmail.com"
            }
            """;

        doThrow(new IllegalArgumentException("Name cannot be empty"))
                .when(clientService)
                .addClient("", "123", "lev@gmail.com");

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Name cannot be empty"));
    }

    @Test
    void updateClient_existingClient_shouldReturnNoContent() throws Exception {

        String json = """
            {
              "name": "Updated Lev",
              "phone": "456",
              "email": "updated@gmail.com"
            }
            """;

        when(clientService.updateClient(
                1,
                "Updated Lev",
                "456",
                "updated@gmail.com"
        )).thenReturn(true);

        mockMvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(clientService).updateClient(
                1,
                "Updated Lev",
                "456",
                "updated@gmail.com"
        );
    }

    @Test
    void updateClient_nonExistingClient_shouldReturnNotFound() throws Exception {

        String json = """
            {
              "name": "Updated Lev",
              "phone": "456",
              "email": "updated@gmail.com"
            }
            """;

        when(clientService.updateClient(
                999,
                "Updated Lev",
                "456",
                "updated@gmail.com"
        )).thenReturn(false);

        mockMvc.perform(put("/api/clients/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void deleteClient_existingClient_shouldReturnNoContent() throws Exception {

        when(clientService.deleteById(1))
                .thenReturn(true);

        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(clientService).deleteById(1);
    }

    @Test
    void deleteClient_nonExistingClient_shouldReturnNotFound() throws Exception {

        when(clientService.deleteById(999))
                .thenReturn(false);

        mockMvc.perform(delete("/api/clients/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }
}

