package guru.springframework;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PhoneBook phoneBook = new PhoneBook();  // No file saving/loading required

        while (true) {
            System.out.println();  // Ensure a blank line between actions
            System.out.println("[menu] Enter action (add, list, search, count, exit): ");
            String action = scanner.nextLine().trim();

            switch (action.toLowerCase()) {
                case "add":
                    handleAdd(phoneBook, scanner);
                    break;
                case "list":
                    handleList(phoneBook, scanner);
                    break;
                case "search":
                    handleSearch(phoneBook, scanner);
                    break;
                case "count":
                    handleCount(phoneBook);
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("Unknown action!");
            }
        }
    }

    static void handleAdd(PhoneBook phoneBook, Scanner scanner) {
        System.out.println("Enter the type (person, organization): ");
        String type = scanner.nextLine().trim();

        if ("person".equalsIgnoreCase(type)) {
            System.out.println("Enter name: ");
            String name = scanner.nextLine().trim();
            System.out.println("Enter surname: ");
            String surname = scanner.nextLine().trim();
            String phoneNumber = "";
            do {
                System.out.println("Enter phone number: ");
                phoneNumber = scanner.nextLine();
                if (isValidPhoneNumber(phoneNumber)) {
                    System.out.println("Wrong number format! Please enter again.");
                }
            } while (isValidPhoneNumber(phoneNumber));

            AbstractContact person = new Person(name, surname, phoneNumber);
            phoneBook.addContact(person);
            System.out.println("The record added.");
        } else if ("organization".equalsIgnoreCase(type)) {
            System.out.println("Enter organization name: ");
            String orgName = scanner.nextLine().trim();
            System.out.println("Enter address: ");
            String address = scanner.nextLine().trim();
            System.out.println("Enter phone number: ");
            String phoneNumber = scanner.nextLine().trim();

            AbstractContact organization = new Organization(orgName, address, phoneNumber);
            phoneBook.addContact(organization);
            System.out.println("The record added.");
        }
    }

    static void handleList(PhoneBook phoneBook, Scanner scanner) {
        phoneBook.listContacts();
        System.out.println();
        System.out.println("[list] Enter action ([number], back): ");
        String listAction = scanner.nextLine().trim();

        if (listAction.equalsIgnoreCase("back")) {
            return;  // Go back to the menu
        }

        try {
            int index = Integer.parseInt(listAction) - 1;  // Convert to 0-based index
            AbstractContact contact = phoneBook.getContact(index);
            if (contact != null) {
                contact.display();  // Display the selected contact
                handleRecordMenu(phoneBook, scanner, contact); // Handle further actions
            } else {
                System.out.println("No such contact.");
            }
        } catch (NumberFormatException ignored) {
            System.out.println("Invalid input.");
        }
    }

    static void handleRecordMenu(PhoneBook phoneBook, Scanner scanner, AbstractContact contact) {
        System.out.println("[record] Enter action (edit, delete, menu): ");
        String action = scanner.nextLine().trim();

        switch (action.toLowerCase()) {
            case "edit":
                handleEdit(scanner, contact);
                System.out.println("The record updated!");
                break;
            case "delete":
                phoneBook.removeContact(contact);
                System.out.println("The record removed!");
                break;
            case "menu":
                return;  // Go back to the main menu
            default:
                System.out.println("Unknown action!");
        }
    }

    static void handleEdit(Scanner scanner, AbstractContact contact) {
        // Show editable fields for the contact
        System.out.println("Select a field (" + String.join(", ", contact.getEditableFields()) + "): ");
        String field = scanner.nextLine().trim().toLowerCase();

        // Check if the field is valid for this contact
        if (!List.of(contact.getEditableFields()).contains(field)) {
            System.out.println("Invalid field.");
            return;
        }

        // Special handling for gender
        String newValue = "";
        if ("gender".equalsIgnoreCase(field)) {
            do {
                System.out.println("Enter new value for gender (M/F): ");
                newValue = scanner.nextLine().trim().toUpperCase();
                if (!newValue.equals("M") && !newValue.equals("F")) {
                    System.out.println("Invalid gender! Please enter either 'M' or 'F'.");
                }
            } while (!newValue.equals("M") && !newValue.equals("F"));
        } else {
            // Ask for the new value for other fields
            System.out.println("Enter new value for " + field + ": ");
            newValue = scanner.nextLine().trim();
        }

        // Update the field with the new value
        contact.updateField(field, newValue);
    }


    static void handleSearch(PhoneBook phoneBook, Scanner scanner) {
        if (phoneBook.count() == 0) {
            System.out.println("No records to search.");
            return;
        }

        System.out.println("Enter search query: ");
        String query = scanner.nextLine().trim();

        // Perform the search
        List<AbstractContact> results = phoneBook.search(query);

        // Display results
        if (results.isEmpty()) {
            System.out.println("No results found.");
        } else {
            System.out.println("Found " + results.size() + " results:");
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i).getAllFieldsForSearch());
            }

            // Choose an action after search results
            while (true) {
                System.out.println("[search] Enter action ([number], back, again, exit): ");
                String action = scanner.nextLine().trim().toLowerCase();

                if (action.equalsIgnoreCase("back")) {
                    return;  // Return to the main menu
                } else if (action.equalsIgnoreCase("again")) {
                    handleSearch(phoneBook, scanner); // Start search again
                    return;
                } else if (action.equalsIgnoreCase("exit")) {
                    System.exit(0);  // Exit the program
                }

                try {
                    int index = Integer.parseInt(action) - 1;  // Convert to 0-based index
                    if (index >= 0 && index < results.size()) {
                        AbstractContact contact = results.get(index);
                        contact.display();

                        // Allow edit or delete after displaying a contact
                        handleRecordMenu(phoneBook, scanner, contact);
                    } else {
                        System.out.println("Invalid index.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
            }
        }
    }



    static void handleCount(PhoneBook phoneBook) {
        System.out.println("The Phone Book has " + phoneBook.count() + " records.");
    }

    // Helper method to validate phone number format
    private static boolean isValidPhoneNumber(String phoneNumber) {
        String correctNumberRegexOne = "\\+?\\(?(\\w{2,}|\\d)\\)?((-|\\s)\\w{2,})*";
        String correctNumberRegexTwo = "\\+?(\\w{2,}|\\d)(-|\\s)\\(?\\w{2,}\\)?((-|\\s)\\w{2,})*";

//        // Check if the phone number matches either of the valid formats
        return !phoneNumber.matches(correctNumberRegexOne) && !phoneNumber.matches(correctNumberRegexTwo);
    }
}
