package main.repository;
import main.model.ClientNote;

import java.util.List;
import java.util.ArrayList;

public class InMemoryClientNoteRepository implements ClientNoteRepository {

    private final List<ClientNote> notes = new ArrayList<>();
    private int nextId = 1;

    @Override
    public void save(ClientNote note) {
        if (note.getId() == 0) {
            note.setId(nextId);
            nextId++;
        } else if (note.getId() >= nextId) {
            nextId = note.getId() + 1;
        }

        notes.add(note);
    }
    @Override
    public ClientNote findById(int id){
        return notes.stream()
                .filter(note -> note.getId() == id)
                .findFirst()
                .orElse(null);

    }

    @Override
    public List<ClientNote> findByClientId(int clientId){
        List<ClientNote> clientNotes = new ArrayList<>();

        for(ClientNote note : notes){
            if(note.getClientId() == clientId) clientNotes.add(note);
        }
        return clientNotes;
    }

    @Override
    public List<ClientNote> findAll() {
        return new ArrayList<>(notes);
    }

    @Override
    public void deleteById(int id) {
        notes.removeIf(note -> note.getId() == id);
    }
}
