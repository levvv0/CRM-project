package main.repository;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.sql.Statement;

import main.model.Client;
import main.exception.DatabaseException;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceUtils;

@Repository
public class JdbcClientRepository implements ClientRepository {

    private final DataSource dataSource;
    
    public JdbcClientRepository(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void save(Client client) {

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            save(connection, client);
        } catch (SQLException e) {
            throw new DatabaseException("Error while saving client", e);

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    public void save(Connection connection, Client client) throws SQLException {
        String sql = """
            INSERT INTO clients (name, phone, email)
            VALUES (?, ?, ?)
            """;

        try (PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setString(1, client.getName());
            statement.setString(2, client.getPhone());
            statement.setString(3, client.getEmail());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating client failed");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    client.setId(generatedId);
                } else {
                    throw new SQLException("Creating client failed, no ID was generated");
                }
            }
        }
    }

    @Override
    public List<Client> findAll() {

        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Client client = new Client.ClientBuilder()
                        .SId(resultSet.getInt("id"))
                        .SName(resultSet.getString("name"))
                        .SPhone(resultSet.getString("phone"))
                        .SEmail(resultSet.getString("email"))
                        .build();
                clients.add(client);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while finding all clients", e);
        }

        return clients;
    }

    @Override
    public Client findById(int id) {

        String sql = "SELECT * FROM clients WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Client.ClientBuilder()
                            .SId(resultSet.getInt("id"))
                            .SName(resultSet.getString("name"))
                            .SPhone(resultSet.getString("phone"))
                            .SEmail(resultSet.getString("email"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while finding client by id: ", e);
        }

        return null;
    }

    @Override
    public Client findByPhone(String phone){
        String sql = "SELECT * FROM clients WHERE phone = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, phone);

            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return new Client.ClientBuilder()
                            .SId(resultSet.getInt("id"))
                            .SName(resultSet.getString("name"))
                            .SPhone(resultSet.getString("phone"))
                            .SEmail(resultSet.getString("email"))
                            .build();
                }
            }
        }   catch(SQLException e){
            throw new DatabaseException("Error while finding client by phone", e);
        }
        return null;

    }

    @Override
    public void update(Client client){
        String sql = """
                UPDATE clients  
                SET name = ?, phone = ?, email = ?
                WHERE id = ?""";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, client.getName());
            statement.setString(2, client.getPhone());
            statement.setString(3, client.getEmail());
            statement.setInt(4, client.getId());

            statement.executeUpdate();
        } catch(SQLException e){
            throw new DatabaseException("Error while updating client", e);
        }
    }

    @Override
    public void deleteById(int id){

        String sql = "DELETE FROM clients WHERE id = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch(SQLException e){
            throw new DatabaseException("Error while deleted client by id", e);
        }

    }
}
