package main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Test
    void findById_existingClient_shouldReturnClient(){
        service.addClient("Lev", "123", "lev@gmail.com");
        Client savedClient = service.getAllClients().get(0);
        int savedClientId = savedClient.getId();
        Client client = service.findById(savedClientId);

        assertNotNull(client);
        assertEquals("Lev", client.getName());
        assertEquals("123", client.getPhone());
        assertEquals("lev@gmail.com", client.getEmail());
    }

    @Test
    void updateClient_existingClient_shouldUpdateClient(){
        service.addClient("Lev", "123", "lev@gmial.com");
        Client newClient = service.getAllClients().get(0);
        int clientId = newClient.getId();
        boolean result = service.updateClient(clientId, "Pen", "321", "pen@gmail.com");

        assertTrue(result);
        Client client = service.findById(clientId);
        assertEquals("Pen", client.getName());
        assertEquals("321", client.getPhone());
        assertEquals("pen@gmail.com", client.getEmail());
    }

    @Test
    void updateClient_notExistingClient_shouldReturnFalse(){
        boolean result = service.updateClient(786, "Bob", "145",  "bob@gmail.com");

        assertFalse(result);
    }

    @Test
    void deleteClientById_shouldDeleteClient(){
        service.addClient("Lev", "123", "lev@gmail.com");
        Client delClient = service.getAllClients().get(0);
        int delClientId = delClient.getId();
        boolean result = service.deleteById(delClientId);

        assertTrue(result);
        assertEquals(0, service.getAllClients().size());
        assertNull(service.findById(delClientId));
    }

    @Test
    void deleteClientById_notExistingClient_shouldReturnFalse(){
        int id = 678;
        boolean result = service.deleteById(id);

        assertFalse(result);
    }

    @Test
    void updateClient_duplicatePhone_shouldThrowException(){
        service.addClient("Lev", "123", "lev@gmail.com");
        service.addClient("Pen", "321", "lev@gmail.com");
        Client client2 = service.getAllClients().get(1);
        int cId = client2.getId();

        assertThrows(IllegalArgumentException.class, () -> {
            service.updateClient(cId, "Bob", "123", "bob@gmail.com");
        });
    }

    @Test
    void updateClient_samePhoneForSameClient_shouldUpdate(){
        service.addClient("Lev", "123", "lev@gmail.com");
        Client client = service.getAllClients().get(0);
        int id = client.getId();

        boolean result = service.updateClient(id, "Lev upd", "123", "uplev@gmail.com");

        assertTrue(result);
        Client updatedClient = service.findById(id);

        assertEquals("Lev upd", updatedClient.getName());
        assertEquals("123", updatedClient.getPhone());
        assertEquals("uplev@gmail.com", updatedClient.getEmail());
    }

    @Test
    void addClient_shouldTrimClientData(){
        service.addClient("  Lev  ", "  123  ", "  lev@gmail.com  ");
        Client client = service.getAllClients().get(0);

        assertEquals("Lev", client.getName());
        assertEquals("123", client.getPhone());
        assertEquals("lev@gmail.com", client.getEmail());
    }
}
