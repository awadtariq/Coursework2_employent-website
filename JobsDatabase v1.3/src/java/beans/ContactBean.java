package beans;

import entity.ContactMessage;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Named
@RequestScoped
public class ContactBean implements Serializable {

    @PersistenceContext(unitName = "JobsDatabasePU")
    private EntityManager em;

    private String name;
    private String email;
    private String message;

    // --- SEND MESSAGE ---
    @Transactional
    public String sendMessage() {

        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setName(name);
        contactMessage.setEmail(email);
        contactMessage.setMessage(message);
        contactMessage.setSubmittedAt(LocalDate.now());

        em.persist(contactMessage);

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Success", "Your message has been sent!"));

        // Clear form
        name = "";
        email = "";
        message = "";

        return null;
    }

    // --- GET ALL MESSAGES ---
    public List<ContactMessage> getMessages() {
        return em.createQuery(
            "SELECT m FROM ContactMessage m ORDER BY m.submittedAt DESC",
            ContactMessage.class
        ).getResultList();
    }

    // --- DELETE MESSAGE ---
    @Transactional
    public void delete(ContactMessage msg) {
        ContactMessage managed = em.find(ContactMessage.class, msg.getMessageID());
        if (managed != null) {
            em.remove(managed);
        }
    }

    // --- GETTERS / SETTERS ---
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
}