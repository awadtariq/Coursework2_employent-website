package beans;

import entity.Job;
import entity.TrackedJob;
import entity.User;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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

    private String searchText = "";
    private Job selectedJob;
    private String salaryInput;
    private String jobBeginsInput;

    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public JobBean() {
    }

    @PostConstruct
    public void init() {
        // no in-memory tracked jobs list needed anymore
    }

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
            if (salary < 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Salary cannot be negative.", ""));
                return null;
            }
            selectedJob.setSalary(salary);
        } catch (NumberFormatException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Please enter a valid salary.", ""));
            return null;
        }

        try {
            LocalDate parsedDate = LocalDate.parse(jobBeginsInput, DISPLAY_DATE_FORMAT);
            selectedJob.setJobBegins(parsedDate.toString());
        } catch (DateTimeParseException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Please enter a valid start date in the format DD/MM/YYYY.", ""));
            return null;
        }

        selectedJob.setPostedDate(LocalDate.now().toString());
        selectedJob.setStatus("OPEN");

        if (loginBean != null && loginBean.getCurrentUser() != null) {
            User currentUser = loginBean.getCurrentUser();
            selectedJob.setUser(currentUser);
            selectedJob.setPostedBy(currentUser.getUsername());
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "You must be logged in to post a job.", ""));
            return null;
        }

        em.persist(selectedJob);

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Job posted successfully", ""));

        return "Jobs?faces-redirect=true";
    }

    public String prepareEdit(Job job) {
        this.selectedJob = job;
        this.salaryInput = String.valueOf(job.getSalary());

        try {
            LocalDate parsedDate = LocalDate.parse(job.getJobBegins());
            this.jobBeginsInput = parsedDate.format(DISPLAY_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            this.jobBeginsInput = job.getJobBegins();
        }

        return "EditJob?faces-redirect=true";
    }

    @Transactional
    public String updateJob() {
        try {
            double salary = Double.parseDouble(salaryInput);
            if (salary < 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Salary cannot be negative.", ""));
                return null;
            }
            selectedJob.setSalary(salary);
        } catch (NumberFormatException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Please enter a valid salary.", ""));
            return null;
        }

        try {
            LocalDate parsedDate = LocalDate.parse(jobBeginsInput, DISPLAY_DATE_FORMAT);
            selectedJob.setJobBegins(parsedDate.toString());
        } catch (DateTimeParseException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Please enter a valid start date in the format DD/MM/YYYY.", ""));
            return null;
        }

        em.merge(selectedJob);

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Job updated successfully", ""));

        return "Jobs?faces-redirect=true";
    }

    @Transactional
    public void deleteJob(Job job) {
        Job managedJob = em.find(Job.class, job.getId());
        if (managedJob != null) {
            em.remove(managedJob);
        }

        // also remove any tracked-job rows pointing to this job
        List<TrackedJob> tracked = em.createQuery(
            "SELECT t FROM TrackedJob t WHERE t.job.id = :jobId", TrackedJob.class)
            .setParameter("jobId", job.getId())
            .getResultList();

        for (TrackedJob t : tracked) {
            TrackedJob managedTracked = em.find(TrackedJob.class, t.getId());
            if (managedTracked != null) {
                em.remove(managedTracked);
            }
        }
    }

    @Transactional
    public void trackJob(Job job) {
        if (loginBean == null || loginBean.getCurrentUser() == null || job == null) {
            return;
        }

        User currentUser = loginBean.getCurrentUser();

        List<TrackedJob> existing = em.createQuery(
            "SELECT t FROM TrackedJob t WHERE t.user.userID = :userId AND t.job.id = :jobId",
            TrackedJob.class)
            .setParameter("userId", currentUser.getUserID())
            .setParameter("jobId", job.getId())
            .getResultList();

        if (existing.isEmpty()) {
            TrackedJob trackedJob = new TrackedJob();
            trackedJob.setUser(currentUser);
            trackedJob.setJob(job);
            em.persist(trackedJob);
        }
    }

    @Transactional
    public void untrackJob(Job job) {
        if (loginBean == null || loginBean.getCurrentUser() == null || job == null) {
            return;
        }

        User currentUser = loginBean.getCurrentUser();

        List<TrackedJob> existing = em.createQuery(
            "SELECT t FROM TrackedJob t WHERE t.user.userID = :userId AND t.job.id = :jobId",
            TrackedJob.class)
            .setParameter("userId", currentUser.getUserID())
            .setParameter("jobId", job.getId())
            .getResultList();

        for (TrackedJob trackedJob : existing) {
            TrackedJob managedTracked = em.find(TrackedJob.class, trackedJob.getId());
            if (managedTracked != null) {
                em.remove(managedTracked);
            }
        }
    }

    public boolean isTracked(Job job) {
        if (loginBean == null || loginBean.getCurrentUser() == null || job == null) {
            return false;
        }

        User currentUser = loginBean.getCurrentUser();

        Long count = em.createQuery(
            "SELECT COUNT(t) FROM TrackedJob t WHERE t.user.userID = :userId AND t.job.id = :jobId",
            Long.class)
            .setParameter("userId", currentUser.getUserID())
            .setParameter("jobId", job.getId())
            .getSingleResult();

        return count > 0;
    }

    public boolean isOwner(Job job) {
        if (job == null || job.getUser() == null || loginBean == null || loginBean.getCurrentUser() == null) {
            return false;
        }

        Long jobUserId = job.getUser().getUserID();
        Long currentUserId = loginBean.getCurrentUser().getUserID();

        return jobUserId != null && currentUserId != null && jobUserId.equals(currentUserId);
    }

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
        if (loginBean == null || loginBean.getCurrentUser() == null) {
            return new ArrayList<>();
        }

        User currentUser = loginBean.getCurrentUser();

        return em.createQuery(
            "SELECT t.job FROM TrackedJob t WHERE t.user.userID = :userId",
            Job.class)
            .setParameter("userId", currentUser.getUserID())
            .getResultList();
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