package main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClientRegistrationServiceTest {

    private JdbcClientRepository clientRepository;
    private JdbcClientNoteRepository noteRepository;
    private ClientRegistrationService regService;

    @BeforeAll
    static void configureTestDatabase() {
        System.setProperty(
                "CRM_DB_URL",
                "jdbc:postgresql://localhost:5432/crm_test_db"
        );

        DatabaseMigration.migrate();
    }

    @BeforeEach
    void setUp() throws Exception{

        clientRepository = new JdbcClientRepository();
        noteRepository = new JdbcClientNoteRepository();

        regService = new ClientRegistrationService(clientRepository, noteRepository);

        String sql = """
                TRUNCATE TABLE client_notes, clients
                RESTART IDENTITY CASCADE
                """;
        try(Connection connection = DatabaseConnection.getConnection();
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
