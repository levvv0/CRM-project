package main;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {
            DatabaseMigration.migrate();
        } catch (DatabaseException e) {
            System.out.println("Database migration error: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);

        JdbcClientRepository jdbcClientRepository =
                new JdbcClientRepository();

        JdbcClientNoteRepository jdbcNoteRepository =
                new JdbcClientNoteRepository();

        ClientService clientService;
        ClientNoteService noteService;
        ClientRegistrationService registrationService;

        try {
            clientService = new ClientService(jdbcClientRepository);
            noteService = new ClientNoteService(jdbcNoteRepository, jdbcClientRepository);
            registrationService = new ClientRegistrationService(jdbcClientRepository, jdbcNoteRepository);

        } catch (DatabaseException e) {
            System.out.println("Database error during application startup: " + e.getMessage());

            return;
        }

        while (true) {

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
            System.out.println("9: Create client with note");
            System.out.println("10. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1: {
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

                    break;
                }

                case 2: {
                    try {
                        System.out.println("Clients:");

                        for (Client client : clientService.getAllClients()) {
                            System.out.println(client);
                        }

                    } catch (DatabaseException e) {
                        System.out.println("Database error: " + e.getMessage());
                    }

                    break;
                }

                case 3: {
                    System.out.println("Print client ID:");
                    int clientId = scanner.nextInt();
                    scanner.nextLine();

                    try {
                        boolean deleted =
                                clientService.deleteById(clientId);

                        if (deleted) {
                            System.out.println("Client deleted");
                        } else {
                            System.out.println(
                                    "Client with that ID not found"
                            );
                        }

                    } catch (DatabaseException e) {
                        System.out.println(
                                "Database error: " + e.getMessage()
                        );
                    }

                    break;
                }

                case 4: {
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
                        boolean updated = clientService.updateClient(
                                clientId,
                                newName,
                                newPhone,
                                newEmail
                        );

                        if (updated) {
                            System.out.println("Client was updated");
                        } else {
                            System.out.println("Client not found");
                        }

                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());

                    } catch (DatabaseException e) {
                        System.out.println(
                                "Database error: " + e.getMessage()
                        );
                    }

                    break;
                }

                case 5: {
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
                        System.out.println(
                                "Database error: " + e.getMessage()
                        );
                    }

                    break;
                }

                case 6: {
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
                        System.out.println(
                                "Database error: " + e.getMessage()
                        );
                    }

                    break;
                }

                case 7: {
                    System.out.println("Print client ID:");
                    int clientId = scanner.nextInt();
                    scanner.nextLine();

                    try {
                        System.out.println("Client notes:");

                        for (ClientNote note :
                                noteService.findByClientId(clientId)) {

                            System.out.println(note);
                        }

                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());

                    } catch (DatabaseException e) {
                        System.out.println(
                                "Database error: " + e.getMessage()
                        );
                    }

                    break;
                }

                case 8: {
                    System.out.println("Print note ID:");
                    int noteId = scanner.nextInt();
                    scanner.nextLine();

                    try {
                        boolean deleted =
                                noteService.deleteById(noteId);

                        if (deleted) {
                            System.out.println("Note deleted");
                        } else {
                            System.out.println(
                                    "Note with that ID not found"
                            );
                        }

                    } catch (DatabaseException e) {
                        System.out.println(
                                "Database error: " + e.getMessage()
                        );
                    }

                    break;
                }

                case 9: {
                    System.out.println("Print name");
                    String name = scanner.nextLine();

                    System.out.println("Print phone");
                    String phone = scanner.nextLine();

                    System.out.println("Print email");
                    String email = scanner.nextLine();

                    System.out.println("Print note text");
                    String text = scanner.nextLine();

                    try {
                        registrationService.createClientWithFirstNote(name, phone, email, text);
                        System.out.println("Client with note aded");

                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());

                    } catch (DatabaseException e) {
                        System.out.println("Database error" + e.getMessage());
                    }
                    break;
                }
                    case 10: {
                        System.out.println("Exit");
                        DatabaseConnection.closePool();
                        scanner.close();
                        return;
                    }

                default: {
                    System.out.println("Unknown menu command");
                }
            }
        }
    }
}