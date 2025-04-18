package project;

import applicant.Application;
import enquiry.Enquiry;
import enums.FlatType;
import helper.UniqueId;
import manager.Manager;
import officer.Officer;
import officer.RegistrationForm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Project {
    private final Integer id;
    private String name;
    private String neighbourhood;
    private final Map<FlatType, Integer> availableFlats;
    private LocalDate applicationOpeningDate;
    private LocalDate applicationClosingDate;
    private final Manager manager;
    private boolean visibility;
    private Integer availableOfficerSlots;
    private final List<Officer> officers;
    private final List<RegistrationForm> registrationForms;
    private final List<Application> applications;
    private final List<Enquiry> enquiries;

    // Use this when Manager wants to create a new project
    public Project(final UniqueId id, String name, String neighbourhood, Map<FlatType, Integer> availableFlats,
                   LocalDate applicationOpeningDate, LocalDate applicationClosingDate, Manager manager, boolean visibility) {
        this.id = id.getNextProjectId();
        this.name = name;
        this.neighbourhood = neighbourhood;
        this.availableFlats = availableFlats;
        this.applicationOpeningDate = applicationOpeningDate;
        this.applicationClosingDate = applicationClosingDate;
        this.manager = manager;
        this.visibility = visibility;
        this.availableOfficerSlots = 10;
        this.officers = new ArrayList<>();
        this.registrationForms = new ArrayList<>();
        this.applications = new ArrayList<>();
        this.enquiries = new ArrayList<>();
    }

    // Use this when loading from csv
    public Project(Integer id, String name, String neighbourhood, Map<FlatType, Integer> availableFlats,
                   LocalDate applicationOpeningDate, LocalDate applicationClosingDate, Manager manager,
                   boolean visibility, Integer availableOfficerSlots, List<Officer> officers, List<RegistrationForm> registrationForms,
                   List<Application> applications, List<Enquiry> enquiries) {
        this.id = id;
        this.name = name;
        this.neighbourhood = neighbourhood;
        this.availableFlats = availableFlats;
        this.applicationOpeningDate = applicationOpeningDate;
        this.applicationClosingDate = applicationClosingDate;
        this.manager = manager;
        this.visibility = visibility;
        this.availableOfficerSlots = availableOfficerSlots;
        this.officers = officers;
        this.registrationForms = registrationForms;
        this.applications = applications;
        this.enquiries = enquiries;
    }

    public Integer getId() {
        return id;
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

    public List<RegistrationForm> getRegistrationForms() {
        return registrationForms;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public List<Enquiry> getEnquiries() {
        return enquiries;
    }
}
