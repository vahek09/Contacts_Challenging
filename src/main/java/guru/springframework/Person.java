package guru.springframework;

public class Person extends AbstractContact {
    private String name;
    private String surname;
    private String birthDate;
    private String gender;
    private String phoneNumber;

    public Person(String name, String surname, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = "";
        setPhoneNumber(phoneNumber);
        this.birthDate = "[no data]";
        this.gender = "[no data]";
    }

    @Override
    public String[] getEditableFields() {
        return new String[]{"name", "surname", "birthDate", "gender", "phoneNumber"};
    }

    @Override
    public void updateField(String field, String newValue) {
        switch (field) {
            case "name":
                this.name = newValue;
                break;
            case "surname":
                this.surname = newValue;
                break;
            case "birthDate":
                this.birthDate = newValue;
                break;
            case "gender":
                this.gender = newValue;
                break;
            case "phoneNumber":
                setPhoneNumber(newValue);  // Use setter to validate the phone number
                break;
        }
        updateLastEditTime();
    }

    @Override
    public String getFieldValue(String field) {
        switch (field) {
            case "name":
                return name;
            case "surname":
                return surname;
            case "birthDate":
                return birthDate;
            case "gender":
                return gender;
            case "phoneNumber":
                return getPhoneNumber();  // Ensure proper phone number retrieval
            default:
                return "";
        }
    }

    @Override
    public String getAllFieldsForSearch() {
        return name + " " + surname + " " + birthDate + " " + gender + " " + getPhoneNumber();
    }

    @Override
    public void display() {
        System.out.println("Name: " + name);
        System.out.println("Surname: " + surname);
        System.out.println("Birth date: " + birthDate);
        System.out.println("Gender: " + gender);
        System.out.println("Phone Number: " + getPhoneNumber());
        System.out.println("Time created: " + createdTime);
        System.out.println("Time last edit: " + lastEditTime);
    }

    // Additional method to validate and set phone number
    public void setPhoneNumber(String phoneNumber) {
        if (isValidPhoneNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            System.out.println("Wrong number format!");
            this.phoneNumber = "[no number]";
        }
    }

    public String getPhoneNumber() {
        return phoneNumber.isEmpty() ? "[no number]" : phoneNumber;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
       return !(validatePhoneNumber(phoneNumber).equals("[no number]"));
    }
}




