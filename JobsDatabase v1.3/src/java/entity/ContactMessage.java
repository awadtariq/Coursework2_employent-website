package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "CONTACTMESSAGES")
public class ContactMessage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageID;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 150)
    private String email;

    @Lob
    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDate submittedAt;

    public ContactMessage() {
    }

    public Long getMessageID() {
        return messageID;
    }

    public void setMessageID(Long messageID) {
        this.messageID = messageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDate submittedAt) {
        this.submittedAt = submittedAt;
    }
}