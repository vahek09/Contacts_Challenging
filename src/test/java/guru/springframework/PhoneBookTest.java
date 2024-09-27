package guru.springframework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class PhoneBookTest {

    private PhoneBook phoneBook;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        phoneBook = new PhoneBook();
        // Redirect System.out to capture output
        System.setOut(new PrintStream(outContent));
    }

    @Test
    @DisplayName("Should correctly add a person to the phone book")
    void testAddContactPerson() {
        // given
        Person person = new Person("John", "Doe", "123 456 789");

        // when
        phoneBook.addContact(person);

        // then
        assertEquals(1, phoneBook.count());
        assertEquals("John Doe [no data] [no data] 123 456 789", phoneBook.getContact(0).getAllFieldsForSearch());
    }

    @Test
    @DisplayName("Should correctly add an organization to the phone book")
    void testAddContactOrganization() {
        // given
        Organization organization = new Organization("Pizza Shop", "Wall St. 1", "+0 (123) 456-789-9999");

        // when
        phoneBook.addContact(organization);

        // then
        assertEquals(1, phoneBook.count());
        assertEquals("Pizza Shop Wall St. 1 +0 (123) 456-789-9999", phoneBook.getContact(0).getAllFieldsForSearch());
    }

    @Test
    @DisplayName("Should correctly search contacts by a query")
    void testSearchContacts() {
        // given
        Person person = new Person("John", "Doe", "123 456 789");
        Organization organization = new Organization("Pizza Shop", "Wall St. 1", "+0 (123) 456-789-9999");
        phoneBook.addContact(person);
        phoneBook.addContact(organization);

        // when
        int searchResultSize1 = phoneBook.search("123").size();
        int searchResultSize2 = phoneBook.search("Doe").size();
        int searchResultSize3 = phoneBook.search("Pizza").size();
        int searchResultSize4 = phoneBook.search("Unknown").size();

        // then
        assertEquals(2, searchResultSize1);
        assertEquals(1, searchResultSize2);
        assertEquals(1, searchResultSize3);
        assertEquals(0, searchResultSize4);
    }

    @Test
    @DisplayName("Should correctly remove a contact from the phone book")
    void testRemoveContact() {
        // given
        Person person = new Person("John", "Doe", "123 456 789");
        phoneBook.addContact(person);

        // when
        phoneBook.removeContact(person);

        // then
        assertEquals(0, phoneBook.count());
    }

    @Test
    @DisplayName("Should correctly list all contacts in the phone book")
    void testListContacts() {
        // given
        Person person1 = new Person("John", "Doe", "123 456 789");
        Organization org1 = new Organization("Pizza Shop", "Wall St. 1", "+0 (123) 456-789-9999");
        phoneBook.addContact(person1);
        phoneBook.addContact(org1);

        // when
        phoneBook.listContacts();

        // then
        String expectedOutput = "1. John Doe [no data] [no data] 123 456 789\n" +
                "2. Pizza Shop Wall St. 1 +0 (123) 456-789-9999\n";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    @Test
    @DisplayName("Should handle listing contacts when the phone book is empty")
    void testListContacts_empty() {
        // when
        phoneBook.listContacts();

        // then
        String expectedOutput = "";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    @Test
    @DisplayName("Should return null when accessing contacts out of bounds")
    void testGetContactOutOfBounds() {
        // given
        Person person = new Person("John", "Doe", "123 456 789");
        phoneBook.addContact(person);

        // when
        Contact validContact = phoneBook.getContact(0);
        Contact negativeIndexContact = phoneBook.getContact(-1);
        Contact outOfBoundsContact = phoneBook.getContact(10);

        // then
        assertNotNull(validContact);
        assertNull(negativeIndexContact);
        assertNull(outOfBoundsContact);
    }
}
