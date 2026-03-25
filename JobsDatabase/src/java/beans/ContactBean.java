package beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;

@Named
@RequestScoped
public class ContactBean implements Serializable {

    private String name;
    private String email;
    private String subject;
    private String message;

    public String sendMessage() {
        // Here is where you would normally save to a database or send an email
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Your message has been sent!"));
        
        // Clear the form
        name = "";
        email = "";
        subject = "";
        message = "";
        
        return null; // Stay on the same page
    }

    // --- GETTERS AND SETTERS (CRITICAL FOR JSF) ---

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}