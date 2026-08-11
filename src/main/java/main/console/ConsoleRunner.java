package main.console;

import main.model.Client;
import main.model.ClientNote;
import main.exception.DatabaseException;
import main.service.ClientNoteService;
import main.service.ClientRegistrationService;
import main.service.ClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;
import java.util.Scanner;

@Component
@Profile("console")
public class ConsoleRunner implements CommandLineRunner {

    private final ClientService clientService;
    private final ClientNoteService noteService;
    private final ClientRegistrationService registrationService;

    public ConsoleRunner(ClientService clientService, ClientNoteService noteService, ClientRegistrationService registrationService) {
        this.clientService = clientService;
        this.noteService = noteService;
        this.registrationService = registrationService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addClient(scanner);
                    break;

                case 2:
                    printClients();
                    break;

                case 3:
                    deleteClient(scanner);
                    break;

                case 4:
                    updateClient(scanner);
                    break;

                case 5:
                    findClient(scanner);
                    break;

                case 6:
                    addNote(scanner);
                    break;

                case 7:
                    printClientNotes(scanner);
                    break;

                case 8:
                    deleteNote(scanner);
                    break;

                case 9:
                    createClientWithNote(scanner);
                    break;

                case 10:
                    System.out.println("Exit");
                    scanner.close();
                    return;

                default:
                    System.out.println("Unknown menu command");
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("CRM");
        System.out.println("1. Add Client");
        System.out.println("2. Get Clients");
        System.out.println("3. Delete Client");
        System.out.println("4. Update Client");
        System.out.println("5. Find Client by ID");
        System.out.println("6. Add Client Note");
        System.out.println("7. Get Client Notes");
        System.out.println("8. Delete Client Note");
        System.out.println("9. Create Client with Note");
        System.out.println("10. Exit");
    }

    private void addClient(Scanner scanner) {
        System.out.println("Name:");
        String name = scanner.nextLine();

        System.out.println("Phone:");
        String phone = scanner.nextLine();

        System.out.println("Email:");
        String email = scanner.nextLine();

        try {
            clientService.addClient(name, phone, email);
            System.out.println("Client added");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void printClients() {
        try {
            System.out.println("Clients:");

            for (Client client : clientService.getAllClients()) {
                System.out.println(client);
            }
        } catch (DatabaseException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void deleteClient(Scanner scanner) {
        System.out.println("Print client ID:");
        int clientId = scanner.nextInt();
        scanner.nextLine();

        try {
            boolean deleted = clientService.deleteById(clientId);

            if (deleted) {
                System.out.println("Client deleted");
            } else {
                System.out.println("Client with that ID not found");
            }
        } catch (DatabaseException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void updateClient(Scanner scanner) {
        System.out.println("Print client ID:");
        int clientId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("New Name:");
        String newName = scanner.nextLine();

        System.out.println("New Phone:");
        String newPhone = scanner.nextLine();

        System.out.println("New Email:");
        String newEmail = scanner.nextLine();

        try {
            boolean updated = clientService.updateClient(clientId, newName, newPhone, newEmail);

            if (updated) {
                System.out.println("Client was updated");
            } else {
                System.out.println("Client not found");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void findClient(Scanner scanner) {
        System.out.println("Print client ID:");
        int clientId = scanner.nextInt();
        scanner.nextLine();

        try {
            Client client = clientService.findById(clientId);

            if (client != null) {
                System.out.println("Client:");
                System.out.println(client);
            } else {
                System.out.println("Client is not found");
            }
        } catch (DatabaseException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void addNote(Scanner scanner) {
        System.out.println("Print client ID:");
        int clientId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Note text:");
        String noteText = scanner.nextLine();

        try {
            noteService.addNote(clientId, noteText);
            System.out.println("Note added");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void printClientNotes(Scanner scanner) {
        System.out.println("Print client ID:");
        int clientId = scanner.nextInt();
        scanner.nextLine();

        try {
            System.out.println("Client notes:");

            for (ClientNote note : noteService.findByClientId(clientId)) {
                System.out.println(note);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void deleteNote(Scanner scanner) {
        System.out.println("Print note ID:");
        int noteId = scanner.nextInt();
        scanner.nextLine();

        try {
            boolean deleted = noteService.deleteById(noteId);

            if (deleted) {
                System.out.println("Note deleted");
            } else {
                System.out.println("Note with that ID not found");
            }
        } catch (DatabaseException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void createClientWithNote(Scanner scanner) {
        System.out.println("Name:");
        String name = scanner.nextLine();

        System.out.println("Phone:");
        String phone = scanner.nextLine();

        System.out.println("Email:");
        String email = scanner.nextLine();

        System.out.println("Note text:");
        String noteText = scanner.nextLine();

        try {
            registrationService.createClientWithFirstNote(name, phone, email, noteText);
            System.out.println("Client with note added");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}