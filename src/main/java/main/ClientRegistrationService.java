package main;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientRegistrationService {

    private final ClientNoteRepository noteRepository;
    private final ClientRepository clientRepository;


    public ClientRegistrationService(JdbcClientRepository clientRepository, JdbcClientNoteRepository noteRepository){
       this.noteRepository = noteRepository;
       this.clientRepository = clientRepository;
    }

    @Transactional
    public void createClientWithFirstNote(String name, String phone, String email, String noteText){

        Client client = new Client.ClientBuilder()
                .SName(name)
                .SPhone(phone)
                .SEmail(email)
                .build();

                clientRepository.save(client);

                ClientNote note = new ClientNote.ClientNoteBuilder()
                        .SClientId(client.getId())
                        .SNoteText(noteText)
                        .build();

                noteRepository.save(note);
    }
}
