package entity;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "TRACKEDJOB") // matches your table name
public class TrackedJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // better for Derby
    @Column(name = "ID")
    private Long id;

    // Link to Job
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "JOB_ID", nullable = false)
    private Job job;

    // 🔥 CRITICAL: Link to User (this is what enables persistence per user)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public TrackedJob() {}

    // --- GETTERS AND SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // --- EQUALS & HASHCODE ---

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TrackedJob)) {
            return false;
        }
        TrackedJob other = (TrackedJob) object;
        if ((this.id == null && other.id != null) ||
            (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TrackedJob[id=" + id + "]";
    }
}