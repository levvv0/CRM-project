package main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientServiceTest {

    private ClientRepository repository;
    private ClientService service;

    @BeforeEach
    void setUp(){
        repository = new InMemoryClientRepository();
        service = new ClientService(repository);
    }
    @Test
    void addClientTest(){

        service.addClient("Lev", "123", "lev@gmail.com");
        assertEquals(1, service.getAllClients().size());
        Client client = service.getAllClients().get(0);

        assertEquals("Lev", client.getName());
        assertEquals("123", client.getPhone());
        assertEquals("lev@gmail.com", client.getEmail());

    }

    @Test
    void addClientWithSamePhoneTest(){

        service.addClient("Lev", "123", "lev@gmail.com");

        assertThrows(IllegalArgumentException.class, () -> {
            service.addClient("Ben", "123", "bob@gmail.com");
        });
    }

    @Test
    void addClient_emptyName_shouldThrowException(){

        assertThrows(IllegalArgumentException.class, () -> {
            service.addClient("", "123", "lev@gmail.com");
        });
    }

    @Test
    void addClient_emptyPhone_shouldThrowException(){

        assertThrows(IllegalArgumentException.class, () -> {
            service.addClient("Lev", "", "lev@gmail.com");
        });
    }

    @Test
    void addClient_emptyEmail_shouldThrowException(){
        assertThrows(IllegalArgumentException.class, () -> {
            service.addClient("Lev", "123", "");
        });
    }
}
