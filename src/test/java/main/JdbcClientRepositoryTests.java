package main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import javax.sql.DataSource;

@SpringBootTest
@ActiveProfiles("test")
public class JdbcClientRepositoryTests {

    @Autowired
    private JdbcClientRepository repository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        System.setProperty("CRM_DB_URL", "jdbc:postgresql://localhost:5432/crm_test_db");
        clearClientsTable();
    }

    private void clearClientsTable() {
        String sql = """
            TRUNCATE TABLE client_notes, clients
            RESTART IDENTITY CASCADE
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Error while clearing test tables",
                    e
            );
        }
    }

    @Test
    void saveClient_shouldSaveClientToDatabase(){
        Client client = new Client.ClientBuilder()
                .SId(1)
                .SName("Bob")
                .SPhone("987")
                .SEmail("bob@gmail.com")
                .build();

        repository.save(client);

        Client savedClient = repository.findById(1);

        assertNotNull(savedClient);
        assertEquals(1, savedClient.getId());
        assertEquals("Bob", savedClient.getName());
        assertEquals("987", savedClient.getPhone());
        assertEquals("bob@gmail.com", savedClient.getEmail());
    }

    @Test
    void findAll_shouldGetAll(){
        Client client1 = new Client.ClientBuilder()
                .SId(1)
                .SName("Pen")
                .SPhone("245")
                .SEmail("pen@gmail.com")
                .build();

        Client client2 = new Client.ClientBuilder()
                .SId(2)
                .SName("Bob")
                .SPhone("854")
                .SEmail("bob@gmail.com")
                .build();

        repository.save(client1);
        repository.save(client2);

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void findByPhone_shouldReturnClient(){
        Client client = new Client.ClientBuilder()
                .SId(1)
                .SName("Lev")
                .SPhone("783")
                .SEmail("Lev@gmail.com")
                .build();

        repository.save(client);
        Client savedClient = repository.findByPhone("783");

        assertNotNull(savedClient);
        assertEquals(1, savedClient.getId());
        assertEquals("Lev", savedClient.getName());
        assertEquals("783", savedClient.getPhone());
        assertEquals("Lev@gmail.com", savedClient.getEmail());
    }

    @Test
    void updateClient_shouldUpdateClientInDatabase(){
        Client client = new Client.ClientBuilder()
                .SId(1)
                .SName("Lev")
                .SPhone("123")
                .SEmail("lev@gmail.com")
                .build();
        repository.save(client);

        Client updateClient = new Client.ClientBuilder()
                .SId(client.getId())
                .SName("Gin")
                .SPhone("321")
                .SEmail("Gin@gmail.com")
                .build();
        repository.update(updateClient);

        Client foundClient = repository.findById(client.getId());

        assertEquals(1, foundClient.getId());
        assertEquals("Gin", foundClient.getName());
        assertEquals("321", foundClient.getPhone());
        assertEquals("Gin@gmail.com", foundClient.getEmail());
    }

    @Test
    void deleteById_shouldDeleteClient(){
        Client client = new Client.ClientBuilder()
                .SId(1)
                .SName("Lev")
                .SPhone("123")
                .SEmail("lev@gmail.com")
                .build();
        repository.save(client);
        repository.deleteById(client.getId());

        assertNull(repository.findById(client.getId()));
    }
}
