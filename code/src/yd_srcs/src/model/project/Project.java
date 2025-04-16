package model.project;

import enums.FlatType;
import model.enquiry.Enquiry;
import model.form.Application;
import model.form.RegistrationForm;
import model.user.Manager;
import model.user.Officer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Project {
    private final int id;
    private String name;
    private String neighbourhood;
    private final Map<FlatType, Integer> availableFlats;
    private LocalDate applicationOpeningDate;
    private LocalDate applicationClosingDate;
    private final Manager manager;
    private boolean visibility;
    private int availableOfficerSlots;
    private final List<Officer> officerList;
    private final List<RegistrationForm> registrationFormList;
    private final List<Application> applicationList;
    private final List<Enquiry> enquiryList;

    public Project(int id, String name, String neighbourhood, Map<FlatType, Integer> availableFlats,
                   LocalDate applicationOpeningDate, LocalDate applicationClosingDate,
                   Manager manager, boolean visibility, int availableOfficerSlots,
                   List<Officer> officerList, List<RegistrationForm> registrationFormList,
                   List<Application> applicationList, List<Enquiry> enquiryList) {
        this.id = id;
        this.name = name;
        this.neighbourhood = neighbourhood;
        this.availableFlats = availableFlats;
        this.applicationOpeningDate = applicationOpeningDate;
        this.applicationClosingDate = applicationClosingDate;
        this.manager = manager;
        this.visibility = visibility;
        this.availableOfficerSlots = availableOfficerSlots;
        this.officerList = officerList;
        this.registrationFormList = registrationFormList;
        this.applicationList = applicationList;
        this.enquiryList = enquiryList;
    }

    public int getId() {
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

    public boolean isVisible() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public int getAvailableOfficerSlots() {
        return availableOfficerSlots;
    }

    public void setAvailableOfficerSlots(int availableOfficerSlots) {
        this.availableOfficerSlots = availableOfficerSlots;
    }

    public List<Officer> getOfficerList() {
        return officerList;
    }

    public void addOfficer(Officer officer) {
        officerList.add(officer);
    }

    public List<RegistrationForm> getRegistrationFormList() {
        return registrationFormList;
    }

    public void addRegistrationForm(RegistrationForm registrationForm) {
        registrationFormList.add(registrationForm);
    }

    public List<Application> getApplicationList() {
        return applicationList;
    }

    public void addApplication(Application application) {
        applicationList.add(application);
    }

    public List<Enquiry> getEnquiryList() {
        return enquiryList;
    }

    public void addEnquiry(Enquiry enquiry) {
        enquiryList.add(enquiry);
    }
}
