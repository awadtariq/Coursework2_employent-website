package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "JOBS")
public class Job implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobID;

    @Column(nullable = false, length = 200)
    private String jobTitle;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false, length = 50)
    private String jobType;

    private BigDecimal salary;

    @Column(nullable = false, length = 150)
    private String location;

    @Column(nullable = false)
    private LocalDate jobBegins;

    @Column(nullable = false)
    private LocalDate postedDate;

    @Column(nullable = false, length = 50)
    private String status;
    
    @Column(length = 150)
    private String contactEmail;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public Job() {
    }

    public Long getJobID() {
        return jobID;
    }

    public void setJobID(Long jobID) {
        this.jobID = jobID;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getJobBegins() {
        return jobBegins;
    }

    public void setJobBegins(LocalDate jobBegins) {
        this.jobBegins = jobBegins;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String getContactEmail() {
    return contactEmail;
}

    public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
}
    
    public String getJobTypeDisplay() {
    if (jobType == null || jobType.isBlank()) {
        return "";
    }

    String lower = jobType.toLowerCase();

    if (lower.length() == 1) {
        return lower.toUpperCase();
    }

    return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
}
}