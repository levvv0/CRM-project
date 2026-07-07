package main;
import javax.xml.crypto.Data;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ClientRepository repository = new JdbcClientRepository();
        ClientService service;
        try {
            service = new ClientService(repository);
        } catch(DatabaseException e){
            System.out.println("Darabase error during application startup :" + e.getMessage());
            return;
        }

        while (true) {

            System.out.println("CRM");
            System.out.println("1. Add Client");
            System.out.println("2. Get Clients");
            System.out.println("3. Delete client");
            System.out.println("4. Update Client");
            System.out.println("5: Find Client by ID");
            System.out.println("6: Exit");

            int choise = scanner.nextInt();
            scanner.nextLine();
            switch (choise) {
                case 1:

                    System.out.println("Name: ");
                    String name = scanner.nextLine();
                    System.out.println("Phone: ");
                    String phone = scanner.nextLine();
                    System.out.println("Email: ");
                    String email = scanner.nextLine();
                    try {
                        service.addClient(name, phone, email);
                        System.out.println("Client added");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    } catch (DatabaseException e){
                        System.out.println("Database error :" + e.getMessage());
                    }

                    break;

                case 2:

                    try {
                        System.out.println("Clients: ");
                        for (Client client : service.getAllClients()) {
                            System.out.println(client);
                        }
                    } catch(DatabaseException e){
                        System.out.println("Database error :" + e.getMessage());
                    }

                    break;

                case 3:

                    System.out.println("Print ID");
                    int id = scanner.nextInt();
                    try{
                        if (service.deleteById(id)) System.out.println("Client deleted");
                        else System.out.println("Client with that ID not found");
                    } catch(DatabaseException e){
                        System.out.println("Database error:" + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Print ID");
                    int id2 = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("New Name: ");
                    String Nname = scanner.nextLine();
                    System.out.println("New Phone: ");
                    String Nphone = scanner.nextLine();
                    System.out.println("New Email: ");
                    String Nemail = scanner.nextLine();
                    boolean updatedC;

                    try {
                        updatedC = service.updateClient(id2, Nname, Nphone, Nemail);
                        if (updatedC) System.out.println("Client was updated");
                        else System.out.println("Client not found");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    } catch(DatabaseException e){
                        System.out.println("Database error :" + e.getMessage());
                    }

                    break;

                case 5:

                    System.out.println("Print ID:");
                    id = scanner.nextInt();

                    try {
                        Client clientg = service.findById(id);
                        if (clientg != null) {
                            System.out.println("Client: ");
                            System.out.println(clientg);
                        } else {
                            System.out.println("Client is not found");
                        }
                    } catch(DatabaseException e){
                        System.out.println("Database error:" + e.getMessage());
                    }

                    break;

                case 6:

                    System.out.println("Exit");
                    return;
            }

        }
    }
}
