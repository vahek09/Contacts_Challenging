package guru.springframework;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PhoneBook implements Serializable {
    private List<Contact> contacts = new ArrayList<>();

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public List<Contact> search(String query) {
        List<Contact> results = new ArrayList<>();
        Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        for (Contact contact : contacts) {
            if (pattern.matcher(contact.getAllFieldsForSearch()).find()) {
                results.add(contact);
            }
        }
        return results;
    }

    public void listContacts() {
        for (int i = 0; i < contacts.size(); i++) {
            System.out.println((i + 1) + ". " + contacts.get(i).getAllFieldsForSearch());
        }
    }

    public Contact getContact(int index) {
        if (index >= 0 && index < contacts.size()) {
            return contacts.get(index); // Return the contact at the specified index
        }
        return null; // Return null if index is out of bounds
    }

    public int count() {
        return contacts.size();
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
    }


}
