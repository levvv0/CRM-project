package main;

import java.util.List;

public interface ClientRepository {

    public void save(Client client);

    public List<Client> findAll();

    public void delete(Client client);

    public Client findById(int id);

    public Client findByPhone(String phone);
}
