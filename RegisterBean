package beans;

import entity.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Named
@RequestScoped
public class RegisterBean {

    @PersistenceContext(unitName = "JobsDatabasePU")
    private EntityManager em;

    private String username;
    private String email;
    private String password;
    private String dateOfBirthInput;

    @Transactional
    public String register() {

    // 🔍 Check duplicate username
    Long usernameCount = em.createQuery(
        "SELECT COUNT(u) FROM User u WHERE u.username = :username",
        Long.class)
        .setParameter("username", username)
        .getSingleResult();

    if (usernameCount > 0) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Username already taken",
                "Please choose a different username."));
        return null;
    }

    // 🔍 Check duplicate email
    Long emailCount = em.createQuery(
        "SELECT COUNT(u) FROM User u WHERE u.email = :email",
        Long.class)
        .setParameter("email", email)
        .getSingleResult();

    if (emailCount > 0) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Email already registered",
                "Please use a different email address."));
        return null;
    }

    // 👤 Create user
    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPasswordHash(password); // (you can mention hashing in report)
    user.setCreatedAt(LocalDate.now());

    // 📅 Handle date of birth safely
    if (dateOfBirthInput != null && !dateOfBirthInput.isBlank()) {
        try {
            user.setDateOfBirth(LocalDate.parse(dateOfBirthInput));
        } catch (DateTimeParseException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Invalid date",
                    "Date of birth must be in yyyy-MM-dd format."));
            return null;
        }
    }

    // 💾 Save
    em.persist(user);

    // ✅ Success message (optional)
    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_INFO,
            "Registration successful",
            "You can now log in."));

    return "/Login.xhtml?faces-redirect=true";
}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOfBirthInput() {
        return dateOfBirthInput;
    }

    public void setDateOfBirthInput(String dateOfBirthInput) {
        this.dateOfBirthInput = dateOfBirthInput;
    }
}
