package beans;

import entity.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    @PersistenceContext(unitName = "JobsDatabasePU")
    private EntityManager em;

    private String username;
    private String password;
    private boolean loggedIn = false;
    private User currentUser;

    public String login() {
        TypedQuery<User> query = em.createQuery(
            "SELECT u FROM User u WHERE u.username = :username AND u.passwordHash = :password",
            User.class
        );
        query.setParameter("username", username);
        query.setParameter("password", password);

        List<User> results = query.getResultList();

        if (!results.isEmpty()) {
            currentUser = results.get(0);
            loggedIn = true;
            return "/Jobs.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Invalid login",
                    "Username or password is incorrect."));
            return null;
        }
    }

    public String logout() {
        loggedIn = false;
        currentUser = null;
        username = null;
        password = null;
        return "/index.xhtml?faces-redirect=true";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}