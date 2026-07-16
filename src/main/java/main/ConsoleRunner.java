package main;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsoleRunner implements CommandLineRunner{

    private final ClientService clientService;

    public ConsoleRunner(ClientService clientService){
        this.clientService = clientService;
    }

    @Override
    public void run(String... args){

        System.out.println("ClientService class: " + clientService.getClass().getName());

        System.out.println("Clients in database : " + clientService.getAllClients().size());
    }
}
