package main.repository;

import main.model.Client;

import java.util.List;

public interface ClientRepository {

     void save(Client client);

     List<Client> findAll();

     void deleteById(int id);

    Client findById(int id);

    Client findByPhone(String phone);

    void update(Client client);
}
