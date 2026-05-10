import java.util.ArrayList;
import java.util.List;
public class ClientService {

    private List<Client> clients = new ArrayList<>();
    private int nextId = 1;

    public void addClient(String name, String phone, String email){
        Client client = new Client.ClientBuilder()
                .SId(nextId)
                .SName(name)
                .SPhone(phone)
                .SEmail(email)
                .build();
        clients.add(client);
        nextId++;
    }

    public List<Client> getAllClients(){
        return new ArrayList<>(clients);
    }

    public Client findById(int id){
        return clients.stream()
                .filter(client -> client.getId() == id)
                .findFirst().orElse(null);
    }

    public void deleteById(int id){
        Client client = findById(id);
        clients.remove(client);
    }

    public void updateClient(int id, Client updatedClient){
        Client client = findById(id);
        client.setName(updatedClient.getName());
        client.setPhone(updatedClient.getPhone());
        client.setEmail(updatedClient.getEmail());
    }
}
