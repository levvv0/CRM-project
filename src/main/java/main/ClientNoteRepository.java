package main;
import java.util.List;

public interface ClientNoteRepository {

    void save(ClientNote note);

    ClientNote findById(int id);

    List<ClientNote> findByClientId(int clientId);

    List<ClientNote> findAll();

    void deleteById(int id);
}
