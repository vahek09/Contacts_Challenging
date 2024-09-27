package guru.springframework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class PersonTest {

    private Person person;

    @BeforeEach
    void setUp() {
        // given
        person = new Person("John", "Doe", "123-456-789");
    }

    @Test
    @DisplayName("Should correctly set the createdTime during initialization")
    void testGetCreatedTime() {
        // when
        LocalDateTime createdTime = person.getCreatedTime();

        // then
        assertNotNull(createdTime);
        assertTrue(createdTime.isBefore(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should update the lastEditTime when a field is modified")
    void testUpdateLastEditTime() throws InterruptedException {
        // given
        LocalDateTime initialEditTime = person.getLastEditTime();
        Thread.sleep(1000); // Wait to ensure the lastEditTime changes

        // when
        person.updateField("phoneNumber", "987-654-321");

        // then
        assertTrue(person.getLastEditTime().isAfter(initialEditTime));
    }

    @Test
    @DisplayName("Should return correct editable fields for a person")
    void testGetEditableFields() {
        // when
        String[] editableFields = person.getEditableFields();

        // then
        assertArrayEquals(new String[]{"name", "surname", "birthDate", "gender", "phoneNumber"}, editableFields);
    }

    @Test
    @DisplayName("Should return correct field values for a person")
    void testGetFieldValue() {
        // when
        String name = person.getFieldValue("name");
        String surname = person.getFieldValue("surname");
        String phoneNumber = person.getFieldValue("phoneNumber");
        String birthDate = person.getFieldValue("birthDate");
        String gender = person.getFieldValue("gender");

        // then
        assertEquals("John", name);
        assertEquals("Doe", surname);
        assertEquals("123-456-789", phoneNumber);
        assertEquals("[no data]", birthDate);
        assertEquals("[no data]", gender);
    }

    @Test
    @DisplayName("Should correctly update the fields of a person")
    void testUpdateField() {
        // when
        person.updateField("name", "Jane");
        person.updateField("surname", "Smith");
        person.updateField("birthDate", "1990-01-01");
        person.updateField("gender", "M");
        person.updateField("phoneNumber", "987-654-321");

        // then
        assertEquals("Jane", person.getFieldValue("name"));
        assertEquals("Smith", person.getFieldValue("surname"));
        assertEquals("1990-01-01", person.getFieldValue("birthDate"));
        assertEquals("M", person.getFieldValue("gender"));
        assertEquals("987-654-321", person.getFieldValue("phoneNumber"));
    }

    @Test
    @DisplayName("Should handle update attempts for invalid fields")
    void testUpdateField_InvalidField() {
        // given
        String initialName = person.getFieldValue("name");

        // when
        person.updateField("invalidField", "Invalid");

        // then
        assertEquals(initialName, person.getFieldValue("name"));  // No change should happen
    }

    @Test
    @DisplayName("Should return an empty string when accessing an invalid field")
    void testGetFieldValue_InvalidField() {
        // when
        String result = person.getFieldValue("nonExistentField");

        // then
        assertEquals("", result);
    }

    @Test
    @DisplayName("Should display person details without errors")
    void testDisplay() {
        // when & then
        assertDoesNotThrow(() -> person.display());  // Verify no exceptions during display
    }
}
