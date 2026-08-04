package main.service;

import main.model.Client;
import main.model.ClientNote;
import main.repository.ClientNoteRepository;
import main.repository.ClientRepository;
import main.repository.InMemoryClientNoteRepository;
import main.repository.InMemoryClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClientNoteServiceTest {

    private ClientNoteService noteService;
    private ClientNoteRepository noteRepository;
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        noteRepository = new InMemoryClientNoteRepository();
        clientRepository = new InMemoryClientRepository();

        Client client = new Client.ClientBuilder()
                .SId(1)
                .SName("Bob")
                .SPhone("987")
                .SEmail("bob@gmail.com")
                .build();

        clientRepository.save(client);

        noteService = new ClientNoteService(
                noteRepository,
                clientRepository
        );
    }

    @Test
    void addNote_shouldSaveNote() {
        noteService.addNote(1, "Call client tomorrow");

        List<ClientNote> notes = noteRepository.findAll();

        assertEquals(1, notes.size());
        assertEquals(1, notes.get(0).getId());
        assertEquals(1, notes.get(0).getClientId());
        assertEquals("Call client tomorrow", notes.get(0).getNoteText());
    }

    @Test
    void addNote_blankText_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> noteService.addNote(1, "   ")
        );

        assertEquals(
                "Note text cannot be empty",
                exception.getMessage()
        );
    }

    @Test
    void addNote_nullText_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> noteService.addNote(1, null)
        );

        assertEquals(
                "Note text cannot be empty",
                exception.getMessage()
        );
    }

    @Test
    void addNote_nonExistingClient_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> noteService.addNote(999, "Some note")
        );

        assertEquals(
                "Client with this ID not found",
                exception.getMessage()
        );
    }

    @Test
    void addNote_shouldRemoveSpacesFromTextEdges() {
        noteService.addNote(1, "   Important note   ");

        ClientNote savedNote = noteRepository.findById(1);

        assertNotNull(savedNote);
        assertEquals("Important note", savedNote.getNoteText());
    }

    @Test
    void findByClientId_shouldReturnClientNotes() {
        noteService.addNote(1, "First note");
        noteService.addNote(1, "Second note");

        List<ClientNote> notes = noteService.findByClientId(1);

        assertEquals(2, notes.size());
        assertEquals("First note", notes.get(0).getNoteText());
        assertEquals("Second note", notes.get(1).getNoteText());
    }

    @Test
    void deleteById_existingNote_shouldReturnTrue() {
        noteService.addNote(1, "Note to delete");

        boolean result = noteService.deleteById(1);

        assertTrue(result);
        assertNull(noteRepository.findById(1));
    }

    @Test
    void deleteById_nonExistingNote_shouldReturnFalse() {
        boolean result = noteService.deleteById(999);

        assertFalse(result);
    }
}