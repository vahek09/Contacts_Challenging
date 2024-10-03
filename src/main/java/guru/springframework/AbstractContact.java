package guru.springframework;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class AbstractContact implements Serializable {
    protected LocalDateTime createdTime;
    protected LocalDateTime lastEditTime;

    public AbstractContact() {
        this.createdTime = LocalDateTime.now();
        this.lastEditTime = LocalDateTime.now();
    }

    public abstract String[] getEditableFields();
    public abstract void updateField(String field, String newValue);
    public abstract String getFieldValue(String field);
    public abstract String getAllFieldsForSearch();

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getLastEditTime() {
        return lastEditTime;
    }

    protected void updateLastEditTime() {
        this.lastEditTime = LocalDateTime.now();
    }

    public abstract void display();

    public static String validatePhoneNumber(String phoneNumber) {
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
