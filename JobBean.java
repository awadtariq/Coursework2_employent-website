package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named(value = "jobBean")
@SessionScoped
public class JobBean implements Serializable {

    private List<Job> jobs;
    private List<Job> myJobs;
    private String searchText = "";
    private Job selectedJob;
    
    // THE FIX: Added the missing salaryInput property
    private String salaryInput; 

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
        this.salaryInput = ""; // Clear the input field for a fresh form
        return "AddJob?faces-redirect=true"; 
    }

    public String addJob() {
        // Save the job to our list
        selectedJob.setId(jobs.size() + 1);
        jobs.add(selectedJob);
        return "Jobs?faces-redirect=true";
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

    // --- Getters and Setters ---
    
    public List<Job> getJobs() {
        if (searchText != null && !searchText.isEmpty()) {
            List<Job> filtered = new ArrayList<>();
            for (Job j : jobs) {
                if (j.getJobTitle().toLowerCase().contains(searchText.toLowerCase())) {
                    filtered.add(j);
                }
            }
            return filtered;
        }
        return jobs;
    }

    public void setJobs(List<Job> jobs) { this.jobs = jobs; }
    
    public List<Job> getMyJobs() { return myJobs; }
    public void setMyJobs(List<Job> myJobs) { this.myJobs = myJobs; }
    
    public String getSearchText() { return searchText; }
    public void setSearchText(String searchText) { this.searchText = searchText; }
    
    public Job getSelectedJob() { return selectedJob; }
    public void setSelectedJob(Job selectedJob) { this.selectedJob = selectedJob; }

    // THE FIX: The Getters and Setters for salaryInput
    public String getSalaryInput() { return salaryInput; }
    public void setSalaryInput(String salaryInput) { this.salaryInput = salaryInput; }
}
