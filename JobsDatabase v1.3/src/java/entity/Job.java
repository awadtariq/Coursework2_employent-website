package entity;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "JOBS")
public class Job implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JOBID")
    private Long id;

    @Column(name = "JOBTITLE", nullable = false, length = 150)
    private String jobTitle;

    @Column(name = "LOCATION", nullable = false, length = 150)
    private String location;

    @Column(name = "JOBTYPE", nullable = false, length = 50)
    private String jobType;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @Column(name = "SALARY", nullable = false)
    private double salary;

    @Column(name = "JOBBEGINS", length = 20)
    private String jobBegins;

    @Column(name = "POSTEDDATE", length = 20)
    private String postedDate;

    @Column(name = "STATUS", length = 20)
    private String status;

    @Column(name = "POSTEDBY", length = 100)
    private String postedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public Job() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getJobBegins() {
        return jobBegins;
    }

    public void setJobBegins(String jobBegins) {
        this.jobBegins = jobBegins;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Job other = (Job) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
public String getFormattedJobBegins() {
    if (jobBegins == null || jobBegins.isBlank()) {
        return "";
    }

    try {
        java.time.LocalDate date = java.time.LocalDate.parse(jobBegins);
        return date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    } catch (java.time.format.DateTimeParseException e) {
        return jobBegins;
    }
}    

public String getFormattedJobType() {
    if (jobType == null) {
        return "";
    }

    switch (jobType) {
        case "JOB":
            return "Job";
        case "INTERNSHIP":
            return "Internship";
        case "VOLUNTEER":
            return "Volunteer";
        default:
            return jobType;
    }
}    
}