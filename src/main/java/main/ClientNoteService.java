package main;
import java.util.List;

public class ClientNoteService {

    private final ClientNoteRepository noteRepository;
    private final ClientRepository clientRepository;

    public ClientNoteService(ClientNoteRepository noteRepository, ClientRepository clientRepository){
        this.noteRepository = noteRepository;
        this.clientRepository = clientRepository;
    }

    public void addNote(int clientId, String noteText){
        noteText = normalize(noteText);
        validateNoteText(noteText);
        checkClientExists(clientId);

        ClientNote note = new ClientNote.ClientNoteBuilder()
                .SClientId(clientId)
                .SNoteText(noteText)
                .build();

        noteRepository.save(note);
    }

    public ClientNote finById(int id){
        return noteRepository.findById(id);
    }

    public List<ClientNote> findByClientId(int clientId){
        return noteRepository.findByClientId(clientId);

    }

    public List<ClientNote> finAll(){
        return noteRepository.findAll();
    }

    public boolean deleteById(int id){
        ClientNote note = noteRepository.findById(id);
        if(note == null) return false;
        noteRepository.deleteById(id);
        return true;
    }

    private void validateNoteText(String noteText){
        if(noteText == null || noteText.isBlank()){
            throw new IllegalArgumentException("Note text cannot be empty");
        }
    }

    private void checkClientExists(int clientId){
        Client client = clientRepository.findById(clientId);

        if(client == null){
            throw new IllegalArgumentException("Client with this ID not found");
        }
    }

    private String normalize(String value){
        if(value == null){
            return null;
        }
        return value.trim();
    }
}
