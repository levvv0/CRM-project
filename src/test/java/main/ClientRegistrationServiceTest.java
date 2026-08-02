package main;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class ClientRegistrationServiceTest {

    @Autowired
    private JdbcClientRepository clientRepository;

    @Autowired
    private JdbcClientNoteRepository noteRepository;

    @Autowired
    private ClientRegistrationService regService;

    @Autowired
    DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception{

        String sql = """
                TRUNCATE TABLE client_notes, clients
                RESTART IDENTITY CASCADE
                """;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.executeUpdate();
        }
    }

    @Test
    void createClientWithFirstNote_shouldSaveClientAndNote(){

        regService.createClientWithFirstNote("Lev", "090990", "lev@gmail.com", "Call later");

        List<Client> clients = clientRepository.findAll();

        assertEquals(1, clients.size());

        Client client = clients.get(0);

        List<ClientNote> notes =noteRepository.findByClientId(client.getId());

        assertEquals(1, notes.size());

        ClientNote note = notes.get(0);
        assertNotNull(client);
        assertNotNull(note);
        assertEquals("Lev", client.getName());
        assertEquals("090990", client.getPhone());
        assertEquals("lev@gmail.com", client.getEmail());
        assertEquals("Call later", note.getNoteText());
        assertEquals(client.getId(), note.getClientId());
    }

    @Test
    void rolbackt(){

        assertThrows(DatabaseException.class, () -> regService.createClientWithFirstNote(
                        "Pen",
                        "555",
                        "pp",
                        null
                )
        );
        List<Client> clients = clientRepository.findAll();
        List<ClientNote> notes = noteRepository.findAll();

        assertEquals(0, clients.size());
        assertEquals(0, notes.size());
    }
}
