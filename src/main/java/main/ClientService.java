package main;

import java.util.ArrayList;
import java.util.List;
public class ClientService {

    private ClientRepository repository = new ClientRepository();
    private int nextId = 1;

    private void ValidateClientData(String name, String phone, String email){
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name can not be empty");
        }

        if(phone == null || phone.isBlank()){
            throw new IllegalArgumentException("Phone can not be empty");
        }

        if(email == null || email.isBlank()){
            throw new IllegalArgumentException("Email can not be empty");
        }
    }

    private void CheckUniqueNumber(String phone){
        if(repository.findByPhone(phone) != null){
            throw new IllegalArgumentException("Client with that number already exists");
        }
    }

    public void addClient(String name, String phone, String email){
        ValidateClientData(name, phone, email);
        CheckUniqueNumber(phone);
        Client client = new Client.ClientBuilder()
                .SId(nextId)
                .SName(name)
                .SPhone(phone)
                .SEmail(email)
                .build();
        repository.Save(client);
        nextId++;
    }

    public List<Client> getAllClients(){
        return repository.findAll();
    }

    public Client findById(int id){
        return repository.findById(id);
    }

    public void delete(Client client){
        repository.delete(client);
    }

    public boolean updateClient(int id, Client updatedClient){
        ValidateClientData(
                updatedClient.getName(),
                updatedClient.getPhone(),
                updatedClient.getEmail()
        );

        Client client = findById(id);
        if(client == null) return false;
        client.setName(updatedClient.getName());
        client.setPhone(updatedClient.getPhone());
        client.setEmail(updatedClient.getEmail());
        return true;
    }

    public Client findByPhone(String phone){
        return repository.findByPhone(phone);
    }
}
