package beans;

import entity.Job;
import java.io.Serializable;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@Named(value = "jobBean")
@SessionScoped
public class JobBean implements Serializable {

    @PersistenceContext(unitName = "JobsDatabasePU")
    private EntityManager em;

    @Inject
    private LoginBean loginBean;

    private List<Job> myJobs;
    private String searchText = "";
    private Job selectedJob;
    private String salaryInput;
    private String jobBeginsInput;

    public JobBean() {}

    @PostConstruct
    public void init() {
        myJobs = new java.util.ArrayList<>();
    }

    // --- Navigation & Actions ---

    public String prepareCreate() {
        this.selectedJob = new Job();
        this.salaryInput = "";
        this.jobBeginsInput = "";
        return "AddJob?faces-redirect=true";
    }

    @Transactional
    public String createJob() {
        try {
            double salary = Double.parseDouble(salaryInput);
            selectedJob.setSalary(salary);
        } catch (NumberFormatException e) {
            selectedJob.setSalary(0.0);
        }

        selectedJob.setJobBegins(jobBeginsInput);

        // Record which user posted this job
        if (loginBean != null && loginBean.getCurrentUser() != null) {
            selectedJob.setPostedBy(loginBean.getCurrentUser().getUsername());
        }

        // Save to the database
        em.persist(selectedJob);

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Job posted successfully", ""));

        return "Jobs?faces-redirect=true";
    }

    public String prepareEdit(Job job) {
        this.selectedJob = job;
        this.salaryInput = String.valueOf(job.getSalary());
        this.jobBeginsInput = job.getJobBegins();
        return "EditJob?faces-redirect=true";
    }

    @Transactional
    public String updateJob() {
        try {
            double salary = Double.parseDouble(salaryInput);
            selectedJob.setSalary(salary);
        } catch (NumberFormatException e) {
            selectedJob.setSalary(0.0);
        }

        selectedJob.setJobBegins(jobBeginsInput);

        // Update in the database
        em.merge(selectedJob);

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Job updated successfully", ""));

        return "Jobs?faces-redirect=true";
    }

    @Transactional
    public void deleteJob(Job job) {
        // Find the managed version of the job and remove it
        Job managedJob = em.find(Job.class, job.getId());
        if (managedJob != null) {
            em.remove(managedJob);
        }
        if (myJobs != null) {
            myJobs.remove(job);
        }
    }

    public void trackJob(Job job) {
        if (myJobs != null && !myJobs.contains(job)) {
            myJobs.add(job);
        }
    }

    public void untrackJob(Job job) {
        if (myJobs != null) {
            myJobs.remove(job);
        }
    }

    // Returns true if the logged in user posted this job
    public boolean isOwner(Job job) {
        if (loginBean == null || loginBean.getCurrentUser() == null) return false;
        return loginBean.getCurrentUser().getUsername().equals(job.getPostedBy());
    }

    // --- Getters and Setters ---

    // Reads jobs from the database, with optional search filter
    public List<Job> getJobs() {
        if (searchText != null && !searchText.isEmpty()) {
            return em.createQuery(
                "SELECT j FROM Job j WHERE LOWER(j.jobTitle) LIKE :search " +
                "OR LOWER(j.location) LIKE :search", Job.class)
                .setParameter("search", "%" + searchText.toLowerCase() + "%")
                .getResultList();
        }
        return em.createQuery("SELECT j FROM Job j", Job.class).getResultList();
    }

    public List<Job> getMyJobs() {
        return myJobs;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public Job getSelectedJob() {
        return selectedJob;
    }

    public void setSelectedJob(Job selectedJob) {
        this.selectedJob = selectedJob;
    }

    public String getSalaryInput() {
        return salaryInput;
    }

    public void setSalaryInput(String salaryInput) {
        this.salaryInput = salaryInput;
    }

    public String getJobBeginsInput() {
        return jobBeginsInput;
    }

    public void setJobBeginsInput(String jobBeginsInput) {
        this.jobBeginsInput = jobBeginsInput;
    }
}
