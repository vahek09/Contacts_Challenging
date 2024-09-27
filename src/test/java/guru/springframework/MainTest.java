package guru.springframework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainTest {

    private ByteArrayOutputStream outContent;
    private PhoneBook phoneBook;
    private Scanner mockScanner;
    private Contact mockContact;

    @BeforeEach
    void setUp() {
        // Redirect System.out to capture printed output
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Create a new PhoneBook for each test
        phoneBook = new PhoneBook();

        // Mock the Scanner and Contact objects
        mockScanner = mock(Scanner.class);
        mockContact = mock(Contact.class);

        // Mock getEditableFields to return a valid array
        when(mockContact.getEditableFields()).thenReturn(new String[]{"name", "surname", "phoneNumber"});
    }

    @Test
    @DisplayName("Should add a person to the phone book")
    void testHandleAddPerson() {
        // given
        when(mockScanner.nextLine()).thenReturn("person", "John", "Doe", "123 456 789");

        // when
        Main.handleAdd(phoneBook, mockScanner);

        // then
        assertEquals(1, phoneBook.count());
        assertTrue(outContent.toString().contains("The record added."));
        verify(mockScanner, times(4)).nextLine();
    }

    @Test
    @DisplayName("Should add an organization to the phone book")
    void testHandleAddOrganization() {
        // given
        when(mockScanner.nextLine()).thenReturn("organization", "Pizza Shop", "Wall St. 1", "+0 (123) 456-789-9999");

        // when
        Main.handleAdd(phoneBook, mockScanner);

        // then
        assertEquals(1, phoneBook.count());
        assertTrue(outContent.toString().contains("The record added."));
        verify(mockScanner, times(4)).nextLine();
    }

    @Test
    @DisplayName("Should list contacts from the phone book")
    void testHandleList() {
        // given
        phoneBook.addContact(new Person("John", "Smith", "123 456 789"));
        when(mockScanner.nextLine()).thenReturn("1", "back");

        // when
        Main.handleList(phoneBook, mockScanner);

        // then
        assertTrue(outContent.toString().contains("John Smith"));
        verify(mockScanner, times(2)).nextLine();
    }

    @Test
    @DisplayName("Should edit a person's surname in the phone book")
    void testHandleEditPerson() {
        // given
        Person person = new Person("John", "Doe", "123 456 789");
        phoneBook.addContact(person);
        when(mockScanner.nextLine()).thenReturn("surname", "Doe Jr.");
        Contact contact = phoneBook.getContact(0);

        // when
        Main.handleEdit(mockScanner, contact);

        // then
        assertEquals("Doe Jr.", contact.getFieldValue("surname"));
        verify(mockScanner, times(2)).nextLine();
    }

    @Test
    @DisplayName("Should count the number of contacts in the phone book")
    void testHandleCount() {
        // given
        phoneBook.addContact(new Person("John", "Smith", "123 456 789"));

        // when
        Main.handleCount(phoneBook);

        // then
        assertTrue(outContent.toString().contains("The Phone Book has 1 records."));
    }

    @Test
    @DisplayName("Should edit a contact through the handleRecordMenu")
    void testHandleRecordMenu_EditAction() {
        // given
        when(mockScanner.nextLine()).thenReturn("edit");

        // when
        Main.handleRecordMenu(phoneBook, mockScanner, mockContact);

        // then
        verify(mockContact, atLeastOnce()).getEditableFields();
        assertTrue(outContent.toString().contains("The record updated!"));
    }

    @Test
    @DisplayName("Should delete a contact through the handleRecordMenu")
    void testHandleRecordMenu_DeleteAction() {
        // given
        when(mockScanner.nextLine()).thenReturn("delete");

        // when
        Main.handleRecordMenu(phoneBook, mockScanner, mockContact);

        // then
        assertEquals(0, phoneBook.count());
        assertTrue(outContent.toString().contains("The record removed!"));
    }

    @Test
    @DisplayName("Should return to the menu without additional output")
    void testHandleRecordMenu_MenuAction() {
        // given
        when(mockScanner.nextLine()).thenReturn("menu");

        // when
        Main.handleRecordMenu(phoneBook, mockScanner, mockContact);

        // then
        assertFalse(outContent.toString().contains("The record updated!"));
        assertFalse(outContent.toString().contains("The record removed!"));
    }

    @Test
    @DisplayName("Should handle an unknown action in the handleRecordMenu")
    void testHandleRecordMenu_UnknownAction() {
        // given
        when(mockScanner.nextLine()).thenReturn("invalidAction");

        // when
        Main.handleRecordMenu(phoneBook, mockScanner, mockContact);

        // then
        assertTrue(outContent.toString().contains("Unknown action!"));
    }

    @Test
    @DisplayName("Should correctly handle gender input during edit")
    void testHandleEdit_GenderField() {
        // given
        Contact mockContact = mock(Person.class);
        when(mockContact.getEditableFields()).thenReturn(new String[]{"name", "surname", "gender", "phoneNumber"});
        when(mockScanner.nextLine()).thenReturn("gender", "X", "Y", "F");

        // when
        Main.handleEdit(mockScanner, mockContact);

        // then
        assertTrue(outContent.toString().contains("Invalid gender! Please enter either 'M' or 'F'."));
        verify(mockContact).updateField("gender", "F");
        verify(mockScanner, times(4)).nextLine();
    }

    @Test
    @DisplayName("Should handle search with no records in the phone book")
    void testHandleSearch_NoRecords() {
        // given
        when(mockScanner.nextLine()).thenReturn("John");

        // when
        Main.handleSearch(phoneBook, mockScanner);

        // then
        assertTrue(outContent.toString().contains("No records to search."));
    }

    @Test
    @DisplayName("Should return no results when search query does not match any contacts")
    void testHandleSearch_NoResultsFound() {
        // given
        phoneBook.addContact(new Person("John", "Doe", "123 456 789"));
        when(mockScanner.nextLine()).thenReturn("Smith");

        // when
        Main.handleSearch(phoneBook, mockScanner);

        // then
        assertTrue(outContent.toString().contains("No results found."));
    }

    @Test
    @DisplayName("Should display matching results for a search query")
    void testHandleSearch_WithResults() {
        // given
        phoneBook.addContact(new Person("John", "Doe", "123 456 789"));
        when(mockScanner.nextLine()).thenReturn("Doe", "1", "back");

        // when
        Main.handleSearch(phoneBook, mockScanner);

        // then
        assertTrue(outContent.toString().contains("Found 1 results:"));
        assertTrue(outContent.toString().contains("John Doe"));
        assertTrue(outContent.toString().contains("Name: John"));
        assertTrue(outContent.toString().contains("Surname: Doe"));
    }

    @Test
    @DisplayName("Should handle invalid selection index during search")
    void testHandleSearch_InvalidIndex() {
        // given
        phoneBook.addContact(new Person("John", "Doe", "123 456 789"));
        when(mockScanner.nextLine()).thenReturn("Doe", "5", "back");

        // when
        Main.handleSearch(phoneBook, mockScanner);

        // then
        assertTrue(outContent.toString().contains("Invalid index."));
    }

    @Test
    @DisplayName("Should handle invalid non-numeric input during search")
    void testHandleSearch_InvalidInput() {
        // given
        phoneBook.addContact(new Person("John", "Doe", "123 456 789"));
        when(mockScanner.nextLine()).thenReturn("Doe", "abc", "back");

        // when
        Main.handleSearch(phoneBook, mockScanner);

        // then
        assertTrue(outContent.toString().contains("Invalid input."));
    }

    @Test
    @DisplayName("Should restart the search process when 'again' is selected")
    void testHandleSearch_SearchAgain() {
        // given
        phoneBook.addContact(new Person("John", "Doe", "123 456 789"));
        when(mockScanner.nextLine()).thenReturn("Doe", "again", "Smith", "back");

        // when
        Main.handleSearch(phoneBook, mockScanner);

        // then
        assertTrue(outContent.toString().contains("Enter search query:"));
    }
}
