package main;

import java.util.List;
public class ClientService {

    private final ClientRepository repository;
    private int nextId = 1;

    public ClientService(ClientRepository repository){
        this.repository = repository;
        this.nextId = repository.findAll().stream()
                .mapToInt(Client::getId)
                .max()
                .orElse(0)+1;
    }
    private void validateClientData(String name, String phone, String email){
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

    private void checkUniqueNumber(String phone){
        if(repository.findByPhone(phone) != null){
            throw new IllegalArgumentException("Client with that number already exists");
        }
    }

    public void addClient(String name, String phone, String email){
        validateClientData(name, phone, email);
        checkUniqueNumber(phone);
        Client client = new Client.ClientBuilder()
                .SId(nextId)
                .SName(name)
                .SPhone(phone)
                .SEmail(email)
                .build();
        repository.save(client);
        nextId++;
    }

    public List<Client> getAllClients(){
        return repository.findAll();
    }

    public Client findById(int id){
        return repository.findById(id);
    }

    public void delete(Client client){
        if(client != null){ repository.delete(client); }
        else return;
    }

    public boolean updateClient(int id, String name, String phone, String email){
        validateClientData(name, phone, email);

        Client clientWithSamePhone = repository.findByPhone(phone);
        if(clientWithSamePhone != null && clientWithSamePhone.getId() != id){
            throw new IllegalArgumentException("This phone is already used");
        }
        Client client = findById(id);
        if(client == null) return false;
        client.setName(name);
        client.setPhone(phone);
        client.setEmail(email);
        repository.update(client);
        return true;
    }

    public Client findByPhone(String phone){
        return repository.findByPhone(phone);
    }
}
