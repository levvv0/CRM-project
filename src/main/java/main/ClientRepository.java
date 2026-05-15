package main;

import java.util.ArrayList;
import java.util.List;

public class ClientRepository{

    private List<Client> clients = new ArrayList<>();

    public void Save(Client client){
        clients.add(client);
    }

    public List<Client> findAll(){
        return new ArrayList<>(clients);
    }

    public Client findById(int id) {
        return clients.stream()
                .filter(client -> client.getId() == id)
                .findFirst().orElse(null);
    }

    public void delete(Client client){
        clients.remove(client);
    }

    public Client findByPhone(String phone){
        return clients.stream()
                .filter(client -> client.getPhone().equals(phone))
                .findFirst()
                .orElse(null);
    }
}