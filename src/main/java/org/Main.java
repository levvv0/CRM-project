package org;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClientService service = new ClientService();

        while(true){

            System.out.println("CRM");
            System.out.println("1. Add Client");
            System.out.println("2. Get Clients");
            System.out.println("3. Delete by ID");
            System.out.println("4. Update Client");
            System.out.println("5: Find Client by ID");
            System.out.println("6: Exit");

            int choise = scanner.nextInt();
            scanner.nextLine();
            switch(choise){
                case 1:

                    System.out.println("Name: ");
                    String name = scanner.nextLine();
                    System.out.println("Phone: ");
                    String phone = scanner.nextLine();
                    System.out.println("Email: ");
                    String email = scanner.nextLine();

                    service.addClient(name, phone, email);
                    System.out.println("Client added");
                    break;

                case 2:

                    System.out.println("Clients: ");
                    for(Client client : service.getAllClients()){
                        System.out.println(client);
                    }
                    break;

                case 3:

                    System.out.println("Print ID");
                    int id = scanner.nextInt();
                    service.deleteById(id);
                    System.out.println("Client deleted");
                    break;
                case 4:

                    System.out.println("Print Client ID: ");
                    id = scanner.nextInt();
                    Client client = service.findById(id);
                    service.updateClient(id, client);
                    break;

                case 5:

                    System.out.println("Print ID:");
                    id = scanner.nextInt();

                    Client clientg = service.findById(id);
                    if(clientg == null) System.out.println("Not found client with that id");
                    else {System.out.println("Client: ");
                    System.out.println(clientg);}
                    break;

                case 6:

                    System.out.println("Exit");
                    return;
            }

        }
    }
}