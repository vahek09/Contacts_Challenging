package guru.springframework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class OrganizationTest {

    private Organization organization;

    @BeforeEach
    void setUp() {
        // given
        organization = new Organization("Pizza Shop", "Wall St. 1", "+0 (123) 456-789-9999");
    }

    @Test
    @DisplayName("Should correctly set the createdTime during initialization")
    void testGetCreatedTime() {
        // when
        LocalDateTime createdTime = organization.getCreatedTime();

        // then
        assertNotNull(createdTime);
        assertTrue(createdTime.isBefore(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should update the lastEditTime when a field is modified")
    void testUpdateLastEditTime() throws InterruptedException {
        // given
        LocalDateTime initialEditTime = organization.getLastEditTime();
        Thread.sleep(1000); // Wait to ensure the lastEditTime changes

        // when
        organization.updateField("address", "Wall St. 2");

        // then
        assertTrue(organization.getLastEditTime().isAfter(initialEditTime));
    }

    @Test
    @DisplayName("Should return correct editable fields for an organization")
    void testGetEditableFields() {
        // when
        String[] editableFields = organization.getEditableFields();

        // then
        assertArrayEquals(new String[]{"organizationName", "address", "phoneNumber"}, editableFields);
    }

    @Test
    @DisplayName("Should return correct field values for an organization")
    void testGetFieldValue() {
        // when
        String organizationName = organization.getFieldValue("organizationName");
        String address = organization.getFieldValue("address");
        String phoneNumber = organization.getFieldValue("phoneNumber");

        // then
        assertEquals("Pizza Shop", organizationName);
        assertEquals("Wall St. 1", address);
        assertEquals("+0 (123) 456-789-9999", phoneNumber);
    }

    @Test
    @DisplayName("Should correctly update the fields of an organization")
    void testUpdateField() {
        // when
        organization.updateField("organizationName", "New Pizza Shop");
        organization.updateField("phoneNumber", "+0 (987) 654-321-9999");

        // then
        assertEquals("New Pizza Shop", organization.getFieldValue("organizationName"));
        assertEquals("+0 (987) 654-321-9999", organization.getFieldValue("phoneNumber"));
    }

    @Test
    @DisplayName("Should display organization details without errors")
    void testDisplay() {
        // when & then
        assertDoesNotThrow(() -> organization.display());  // Verify no exceptions during display
    }
}
