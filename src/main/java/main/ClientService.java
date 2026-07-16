package main;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository){
        this.repository = repository;
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

    private void checkUniquePhoneForAdd(String phone){
        if(repository.findByPhone(phone) != null){
            throw new IllegalArgumentException("Client with that number already exists");
        }
    }

    private void checkUniquePhoneForUpdate(int id, String phone){
        Client clientWithSamePhone = repository.findByPhone(phone);
        if(clientWithSamePhone != null && clientWithSamePhone.getId() != id){
            throw new IllegalArgumentException("This phone is already used");
        }
    }

    public void addClient(String name, String phone, String email){
        name = normalize(name);
        phone = normalize(phone);
        email = normalize(email);
        validateClientData(name, phone, email);
        checkUniquePhoneForAdd(phone);
        Client client = new Client.ClientBuilder()
                .SName(name)
                .SPhone(phone)
                .SEmail(email)
                .build();
        repository.save(client);
    }

    public List<Client> getAllClients(){
        return repository.findAll();
    }

    public Client findById(int id){
        return repository.findById(id);
    }

    public boolean deleteById(int id){
        Client client = findById(id);
        if(client != null){ repository.deleteById(id); return true;}
        else return false;
    }

    public boolean updateClient(int id, String name, String phone, String email){
        Client client = findById(id);
        if(client == null) return false;

        name = normalize(name);
        phone = normalize(phone);
        email = normalize(email);

        validateClientData(name, phone, email);
        checkUniquePhoneForUpdate(id, phone);

        client.setName(name);
        client.setPhone(phone);
        client.setEmail(email);
        repository.update(client);
        return true;
    }

    public Client findByPhone(String phone){
        return repository.findByPhone(phone);
    }

    private String normalize(String value){
        if(value == null) return null;
        return value.trim();
    }
}
