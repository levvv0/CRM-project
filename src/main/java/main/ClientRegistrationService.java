package main;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import javax.xml.crypto.Data;

@Service
public class ClientRegistrationService {
    private final JdbcClientNoteRepository noteRepository;
    private final JdbcClientRepository clientRepository;
    private final DataSource dataSource;

    public ClientRegistrationService(JdbcClientRepository clientRepository, JdbcClientNoteRepository noteRepository, DataSource dataSource){
       this.noteRepository = noteRepository;
       this.clientRepository = clientRepository;
       this.dataSource = dataSource;
    }

    public void createClientWithFirstNote(String name, String phone, String email, String noteText){

        Client client = new Client.ClientBuilder()
                .SName(name)
                .SPhone(phone)
                .SEmail(email)
                .build();

        try (Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);

            try {
                clientRepository.save(connection, client);

                ClientNote note = new ClientNote.ClientNoteBuilder()
                        .SClientId(client.getId())
                        .SNoteText(noteText)
                        .build();

                noteRepository.save(connection, note);

                connection.commit();

            } catch(SQLException e){
                connection.rollback();
                throw e;
            }
        } catch(SQLException e){
            throw new DatabaseException("Error while creating client with first note", e);
        }
    }
}
