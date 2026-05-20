package main;
import java.io.*;
import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;
public class ClientFileService {

    private String fileName = "clients.txt";

    public void saveToFile(List<Client> clients){
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
        List<Client> clients = new ArrayList<>();

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
