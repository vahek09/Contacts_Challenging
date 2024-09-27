package guru.springframework;

public class Organization extends Contact {
    private String organizationName;
    private String address;
    private String phoneNumber;

    public Organization(String organizationName, String address, String phoneNumber) {
        this.organizationName = organizationName;
        this.address = address;
        this.phoneNumber = validatePhoneNumber(phoneNumber); // Validate phone number
    }

    @Override
    public String[] getEditableFields() {
        return new String[]{"organizationName", "address", "phoneNumber"};
    }

    @Override
    public void updateField(String field, String newValue) {
        switch (field) {
            case "organizationName": this.organizationName = newValue; break;
            case "address": this.address = newValue; break;
            case "phoneNumber": this.phoneNumber = validatePhoneNumber(newValue); break;
        }
        updateLastEditTime();
    }

    @Override
    public String getFieldValue(String field) {
        switch (field) {
            case "organizationName": return organizationName;
            case "address": return address;
            case "phoneNumber": return phoneNumber;
            default: return "";
        }
    }

    @Override
    public String getAllFieldsForSearch() {
        return organizationName + " " + address + " " + phoneNumber;
    }

    @Override
    public void display() {
        System.out.println("Organization name: " + organizationName);
        System.out.println("Address: " + address);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Time created: " + createdTime);
        System.out.println("Time last edit: " + lastEditTime);
    }

    private String validatePhoneNumber(String phoneNumber) {
        String correctNumberRegexOne = "\\+?\\(?(\\w{2,}|\\d)\\)?((-|\\s)\\w{2,})*";
        String correctNumberRegexTwo = "\\+?(\\w{2,}|\\d)(-|\\s)\\(?\\w{2,}\\)?((-|\\s)\\w{2,})*";

        if (phoneNumber.matches(correctNumberRegexOne) || phoneNumber.matches(correctNumberRegexTwo)) {
            return phoneNumber;
        } else {
            System.out.println("Wrong number format!");
            return "[no number]";
        }
    }
}
