package main;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> getAllClients(){
        return clientService.getAllClients();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createClient(@RequestBody CreateClientRequest request){
        clientService.addClient(request.name(), request.phone(), request.email());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable int id){
        Client client = clientService.findById(id);

        if(client == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientById(@PathVariable int id){

        boolean deleted = clientService.deleteById(id);

        if(!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateClient(@PathVariable int id, @RequestBody UpdateClientRequest client){

        boolean updated = clientService.updateClient(id, client.name(), client.phone(), client.email());

        if(!updated) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}
