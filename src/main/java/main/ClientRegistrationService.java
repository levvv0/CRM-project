package main;
import java.sql.Connection;
import java.sql.SQLException;

public class ClientRegistrationService {
    private JdbcClientNoteRepository noteRepository;
    private JdbcClientRepository clientRepository;

    public ClientRegistrationService(JdbcClientRepository clientRepository, JdbcClientNoteRepository noteRepository){
       this.noteRepository = noteRepository;
       this.clientRepository = clientRepository;
    }

    public void createClientWithFirstNote(String name, String phone, String email, String noteText){

        Client client = new Client.ClientBuilder()
                .SName(name)
                .SPhone(phone)
                .SEmail(email)
                .build();

        try (Connection connection = DatabaseConnection.getConnection()){
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
