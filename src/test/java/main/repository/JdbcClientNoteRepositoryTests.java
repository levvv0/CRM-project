package main.repository;

import main.model.Client;
import main.model.ClientNote;
import main.exception.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
public class JdbcClientNoteRepositoryTests {

    @Autowired
    private JdbcClientNoteRepository noteRepository;

    @Autowired
    private JdbcClientRepository clientRepository;

    @Autowired
    DataSource dataSource;

    @BeforeEach
    void setUp() {
        System.setProperty("CRM_DB_URL", "jdbc:postgresql://localhost:5432/crm_test_db");

        clearTables();

        Client client = new Client.ClientBuilder()
                .SId(1)
                .SName("Bob")
                .SPhone("987")
                .SEmail("bob@gmail.com")
                .build();

        clientRepository.save(client);
    }

    @Test
    void saveNote_shouldSaveNote() {
        ClientNote note = new ClientNote.ClientNoteBuilder()
                .SId(1)
                .SClientId(1)
                .SNoteText("Client asked to call tomorrow")
                .build();

        noteRepository.save(note);

        ClientNote savedNote = noteRepository.findById(note.getId());

        assertNotNull(savedNote);
        assertEquals(1, savedNote.getId());
        assertEquals(1, savedNote.getClientId());
        assertEquals("Client asked to call tomorrow", savedNote.getNoteText());
        assertNotNull(savedNote.getCreatedAt());
    }

    @Test
    void findByClientId_shouldReturnClientNotes() {
        ClientNote note1 = new ClientNote.ClientNoteBuilder()
                .SId(1)
                .SClientId(1)
                .SNoteText("First note")
                .build();

        ClientNote note2 = new ClientNote.ClientNoteBuilder()
                .SId(2)
                .SClientId(1)
                .SNoteText("Second note")
                .build();

        noteRepository.save(note1);
        noteRepository.save(note2);

        List<ClientNote> notes = noteRepository.findByClientId(1);

        assertEquals(2, notes.size());
    }

    @Test
    void findAll_shouldReturnAllNotes() {
        ClientNote note1 = new ClientNote.ClientNoteBuilder()
                .SId(1)
                .SClientId(1)
                .SNoteText("First note")
                .build();

        ClientNote note2 = new ClientNote.ClientNoteBuilder()
                .SId(2)
                .SClientId(1)
                .SNoteText("Second note")
                .build();

        noteRepository.save(note1);
        noteRepository.save(note2);

        List<ClientNote> notes = noteRepository.findAll();

        assertEquals(2, notes.size());
    }

    @Test
    void deleteById_shouldDeleteNote() {
        ClientNote note = new ClientNote.ClientNoteBuilder()
                .SId(1)
                .SClientId(1)
                .SNoteText("Note to delete")
                .build();

        noteRepository.save(note);

        noteRepository.deleteById(1);

        ClientNote deletedNote = noteRepository.findById(1);
        assertNull(deletedNote);
    }

    private void clearTables() {
        String sql = """
            
                TRUNCATE TABLE client_notes, clients
            RESTART IDENTITY CASCADE
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Error while clearing test tables",
                    e
            );
        }
    }
}
