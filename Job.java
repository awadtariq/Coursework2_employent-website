package beans;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Job implements Serializable {

    @Id
    private int id;
    private String jobTitle;
    private String location;
    private String jobType;
    private String description;
    private double salary;
    private String jobBegins; // ADDED

    public Job() {}

    public Job(int id, String jobTitle, String location) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.location = location;
        this.jobType = "Full-Time";
    }

    public Job(int id, String jobTitle, String location, String jobType) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.location = location;
        this.jobType = jobType;
    }

    // --- Getters and Setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    // ADDED: getter and setter for jobBegins
    public String getJobBegins() { return jobBegins; }
    public void setJobBegins(String jobBegins) { this.jobBegins = jobBegins; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Job other = (Job) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
