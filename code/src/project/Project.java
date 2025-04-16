package project;

import applicant.Application;
import enquiry.Enquiry;
import enums.FlatType;
import manager.Manager;
import officer.Officer;
import officer.RegistrationForm;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Project {
    private Integer id;
    private String name;
    private String neighbourhood;
    private Map<FlatType, Integer> availableFlats;
    private LocalDate applicationOpeningDate;
    private LocalDate applicationClosingDate;
    private Manager manager;
    private boolean visibility;
    private Integer availableOfficerSlots;
    private List<Officer> officers;
    private List<RegistrationForm> registrationForms;
    private List<Application> applications;
    private List<Enquiry> enquiries;

    public Project(Integer id, String name, String neighbourhood, Map<FlatType, Integer> availableFlats,
                   LocalDate applicationOpeningDate, LocalDate applicationClosingDate, Manager manager,
                   boolean visibility, Integer availableOfficerSlots) {
        this.id = id;
        this.name = name;
        this.neighbourhood = neighbourhood;
        this.availableFlats = availableFlats;
        this.applicationOpeningDate = applicationOpeningDate;
        this.applicationClosingDate = applicationClosingDate;
        this.manager = manager;
        this.visibility = visibility;
        this.availableOfficerSlots = availableOfficerSlots;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public Map<FlatType, Integer> getAvailableFlats() {
        return availableFlats;
    }

    public void setAvailableFlats(Map<FlatType, Integer> availableFlats) {
        this.availableFlats = availableFlats;
    }

    public LocalDate getApplicationOpeningDate() {
        return applicationOpeningDate;
    }

    public void setApplicationOpeningDate(LocalDate applicationOpeningDate) {
        this.applicationOpeningDate = applicationOpeningDate;
    }

    public LocalDate getApplicationClosingDate() {
        return applicationClosingDate;
    }

    public void setApplicationClosingDate(LocalDate applicationClosingDate) {
        this.applicationClosingDate = applicationClosingDate;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public Integer getAvailableOfficerSlots() {
        return availableOfficerSlots;
    }

    public void setAvailableOfficerSlots(Integer availableOfficerSlots) {
        this.availableOfficerSlots = availableOfficerSlots;
    }

    public List<Officer> getOfficers() {
        return officers;
    }

    public void setOfficers(List<Officer> officers) {
        this.officers = officers;
    }

    public List<RegistrationForm> getRegistrationForms() {
        return registrationForms;
    }

    public void setRegistrationForms(List<RegistrationForm> registrationForms) {
        this.registrationForms = registrationForms;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

    public void setEnquiries(List<Enquiry> enquiries) {
        this.enquiries = enquiries;
    }
}
