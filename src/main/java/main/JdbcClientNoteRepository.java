package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;

@Repository
public class JdbcClientNoteRepository implements ClientNoteRepository {

    private final DataSource dataSource;

    public JdbcClientNoteRepository(DataSource dataSource){
        this.dataSource = dataSource;
    }
    @Override
    public void save(ClientNote note) {
        try (Connection connection = dataSource.getConnection()) {
            save(connection, note);
        } catch (SQLException e) {
            throw new DatabaseException("Error while saving note", e);
        }
    }

    public void save(Connection connection, ClientNote note) throws SQLException {


            String sql ="""
                INSERT INTO client_notes (client_id, note_text)
                VALUES (?, ?)
                """;


        try (PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setInt(1, note.getClientId());
            statement.setString(2, note.getNoteText());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating client note failed");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    note.setId(generatedId);
                } else {
                    throw new SQLException("Creating client note failed, no ID was generated");
                }
            }
        }
    }

    @Override
    public ClientNote findById(int id) {
        String sql = """
                SELECT *
                FROM client_notes
                WHERE id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSetToClientNote(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error while finding client note by id", e);
        }

        return null;
    }

    @Override
    public List<ClientNote> findByClientId(int clientId) {
        String sql = """
                SELECT *
                FROM client_notes
                WHERE client_id = ?
                ORDER BY created_at
                """;

        List<ClientNote> notes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, clientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ClientNote note = resultSetToClientNote(resultSet);
                    notes.add(note);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error while finding client notes by client id", e);
        }

        return notes;
    }

    @Override
    public List<ClientNote> findAll() {
        String sql = """
                SELECT *
                FROM client_notes
                ORDER BY created_at
                """;

        List<ClientNote> notes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                ClientNote note = resultSetToClientNote(resultSet);
                notes.add(note);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error while finding all notes", e);
        }

        return notes;
    }

    @Override
    public void deleteById(int id) {
        String sql = """
                DELETE FROM client_notes
                WHERE id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error while deleting client note", e);
        }
    }

    private ClientNote resultSetToClientNote(ResultSet resultSet) throws SQLException {
        return new ClientNote.ClientNoteBuilder()
                .SId(resultSet.getInt("id"))
                .SClientId(resultSet.getInt("client_id"))
                .SNoteText(resultSet.getString("note_text"))
                .SCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .build();
    }
}