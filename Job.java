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
    
    // THE FIX: Added jobType so AddJob.xhtml can find it
    private String jobType; 

    public Job() {} 

    // Original constructor (Keeps JobBean.java from crashing)
    public Job(int id, String jobTitle, String location) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.location = location;
        this.jobType = "Full-Time"; // Default fallback
    }

    // New constructor including jobType
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

    // THE FIX: JSF uses these to read and write the value from the web page
    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }

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
