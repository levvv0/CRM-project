package main;

import java.io.FileWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileClientRepository implements ClientRepository{
    private final String fileName = "clients.txt";
    private List<Client> clients = new ArrayList<>();


    public FileClientRepository(){
        loadFile();
    }

    @Override
    public void save(Client client){
        clients.add(client);
        saveToFile();
    }

    @Override
    public List<Client> findAll(){
        return new ArrayList<>(clients);
    }

    @Override
    public void deleteById(int id){
        Client client = findById(id);
        clients.remove(client);
        saveToFile();
    }

    @Override
    public Client findById(int id){
        return clients.stream()
                .filter(client -> client.getId() == id)
                .findFirst()
                .orElse(null);
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
                saveToFile();
                return;
            }
        }
    }
    public void saveToFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){

            for(Client client : clients){
                String line =
                        client.getId() + ";" +
                                client.getName() + ";" +
                                client.getPhone() + ";" +
                                client.getEmail();
                writer.write(line);
                writer.newLine();
            }
        }
        catch(IOException e){
            System.out.println("Error while save to file" + e.getMessage());
        }
    }

    public List<Client> loadFile(){

        File file = new File(fileName);
        if(!file.exists()){
            return clients;
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){

            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(";");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String phone = parts[2];
                String email = parts[3];

                Client client = new Client.ClientBuilder()
                        .SId(id)
                        .SName(name)
                        .SPhone(phone)
                        .SEmail(email)
                        .build();
                clients.add(client);
            }

        }
        catch(IOException e){
            System.out.println("Error when load file" + e.getMessage());
        }

        return clients;
    }
}
