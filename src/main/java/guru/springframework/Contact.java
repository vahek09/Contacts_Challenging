package guru.springframework;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Contact implements Serializable {
    protected LocalDateTime createdTime;
    protected LocalDateTime lastEditTime;

    public Contact() {
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
}
