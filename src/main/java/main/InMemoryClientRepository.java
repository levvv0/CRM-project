package main;

import java.util.ArrayList;
import java.util.List;

public class InMemoryClientRepository implements ClientRepository{

    private List<Client> clients = new ArrayList<>();
    private int nextId = 1;

    public InMemoryClientRepository(){}

    public InMemoryClientRepository(List<Client> clients){
        this.clients = new ArrayList<>(clients);
    }
    @Override
    public void save(Client client) {
        if (client.getId() == 0) {
            client.setId(nextId);
            nextId++;
        } else if (client.getId() >= nextId) {
            nextId = client.getId() + 1;
        }

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
    public void deleteById(int id){
        Client client = findById(id);
        clients.remove(client);
    }

    @Override
    public Client findByPhone(String phone){
        return clients.stream()
                .filter(client -> client.getPhone().equals(phone))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Client client){
        for(int i = 0; i < clients.size(); i++){
            if(client.getId() == clients.get(i).getId()){
                clients.set(i, client);
                return;
            }
        }
    }
}