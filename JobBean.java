package beans;

import entity.Job;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named(value = "jobBean")
@SessionScoped
public class JobBean implements Serializable {

    private List<Job> jobs;
    private List<Job> myJobs;
    private String searchText = "";
    private Job selectedJob;
    private String salaryInput;
    private String jobBeginsInput;

    // ADDED: inject LoginBean so we know who is logged in
    @Inject
    private LoginBean loginBean;

    public JobBean() {}

    @PostConstruct
    public void init() {
        jobs = new ArrayList<>();
        myJobs = new ArrayList<>();

        // Mock Data
        jobs.add(new Job(1, "Software Engineer", "London"));
        jobs.add(new Job(2, "Java Developer", "Manchester"));
        jobs.add(new Job(3, "Web Designer", "London"));
    }

    // --- Navigation & Actions ---

    public String prepareCreate() {
        this.selectedJob = new Job();
        this.salaryInput = "";
        this.jobBeginsInput = "";
        return "AddJob?faces-redirect=true";
    }

    public String createJob() {
        selectedJob.setId(jobs.size() + 1);

        try {
            double salary = Double.parseDouble(salaryInput);
            selectedJob.setSalary(salary);
        } catch (NumberFormatException e) {
            selectedJob.setSalary(0.0);
        }

        selectedJob.setJobBegins(jobBeginsInput);

        // ADDED: record which user posted this job
        if (loginBean != null && loginBean.getCurrentUser() != null) {
            selectedJob.setPostedBy(loginBean.getCurrentUser().getUsername());
        }

        jobs.add(selectedJob);
        return "Jobs?faces-redirect=true";
    }

    public String prepareEdit(Job job) {
        this.selectedJob = job;
        this.salaryInput = String.valueOf(job.getSalary());
        this.jobBeginsInput = job.getJobBegins();
        return "EditJob?faces-redirect=true";
    }

    public String updateJob() {
        try {
            double salary = Double.parseDouble(salaryInput);
            selectedJob.setSalary(salary);
        } catch (NumberFormatException e) {
            selectedJob.setSalary(0.0);
        }

        selectedJob.setJobBegins(jobBeginsInput);

        for (int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i).getId() == selectedJob.getId()) {
                jobs.set(i, selectedJob);
                break;
            }
        }
        return "Jobs?faces-redirect=true";
    }

    public void deleteJob(Job job) {
        jobs.remove(job);
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

    // ADDED: returns true if the logged in user posted this job
    public boolean isOwner(Job job) {
        if (loginBean == null || loginBean.getCurrentUser() == null) return false;
        return loginBean.getCurrentUser().getUsername().equals(job.getPostedBy());
    }

    // --- Getters and Setters ---

    public List<Job> getJobs() {
        if (searchText != null && !searchText.isEmpty()) {
            List<Job> filtered = new ArrayList<>();
            for (Job j : jobs) {
                if (j.getJobTitle().toLowerCase().contains(searchText.toLowerCase())
                        || j.getLocation().toLowerCase().contains(searchText.toLowerCase())) {
                    filtered.add(j);
                }
            }
            return filtered;
        }
        return jobs;
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
