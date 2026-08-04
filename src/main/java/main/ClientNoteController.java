package main;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ClientNoteController {

    private final ClientNoteService noteService;

    public ClientNoteController(ClientNoteService noteService){
        this.noteService = noteService;
    }

    @PostMapping("/clients/{clientId}/notes")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNote(@PathVariable int clientId, @Valid @RequestBody CreateNoteRequest request){

        noteService.addNote(clientId, request.noteText());
    }

    @GetMapping("/clients/{clientId}/notes")
    public List<ClientNote> getClientNotes(@PathVariable int clientId){
        return noteService.findByClientId(clientId);
    }

    @DeleteMapping("/notes/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable int noteId){

        boolean deleted = noteService.deleteById(noteId);

        if(!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }

}
