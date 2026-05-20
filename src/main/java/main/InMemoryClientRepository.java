package main;

import java.util.ArrayList;
import java.util.List;

public class InMemoryClientRepository implements ClientRepository{

    private List<Client> clients = new ArrayList<>();

    public InMemoryClientRepository(){}

    public InMemoryClientRepository(List<Client> clients){
        this.clients = new ArrayList<>(clients);
    }
    @Override
    public void save(Client client){
        clients.add(client);
    }

    @Override
    public List<Client> findAll(){
        return new ArrayList<>(clients);
    }

    @Override
    public Client findById(int id) {
        return clients.stream()
                .filter(client -> client.getId() == id)
                .findFirst().orElse(null);
    }

    @Override
    public void delete(Client client){
        clients.remove(client);
    }

    @Override
    public Client findByPhone(String phone){
        return clients.stream()
                .filter(client -> client.getPhone().equals(phone))
                .findFirst()
                .orElse(null);
    }
}