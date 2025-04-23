package officer;

import applicant.Applicant;
import applicant.Application;
import enquiry.Enquiry;
import project.FlatType;
import project.Project;
import user.MaritalStatus;

import java.util.ArrayList;
import java.util.List;

public class Officer extends Applicant {
    private final List<RegistrationForm> myRegistrationForms;
    private Project currentProject;
    private RegistrationForm currentRegistrationForm;
    private OfficerStatus officerStatus;

    public Officer(String name, String nric, String password, int age, MaritalStatus maritalStatus,
                   List<Application> loadedApplications, List<Enquiry> loadedEnquiries, FlatType bookedFlatType) {
        super(name, nric, password, age, maritalStatus, loadedApplications, loadedEnquiries, bookedFlatType);
        this.currentProject = null;
        this.currentRegistrationForm = null;
        this.officerStatus = OfficerStatus.INACTIVE;
        this.myRegistrationForms = new ArrayList<>();
    }

    // for loading from file
    public Officer(String name, String nric, String password, int age, MaritalStatus maritalStatus,
                   List<Application> loadedApplications, List<Enquiry> loadedEnquiries, FlatType bookedFlatType,
                   List<RegistrationForm> myRegistrationForms, Project currentProject, RegistrationForm currentRegistrationForm, OfficerStatus officerStatus) {
        super(name, nric, password, age, maritalStatus, loadedApplications, loadedEnquiries, bookedFlatType);
        this.currentProject = currentProject;
        this.currentRegistrationForm = currentRegistrationForm;
        this.officerStatus = officerStatus;
        this.myRegistrationForms = myRegistrationForms;
    }

    public RegistrationForm getCurrentRegistrationForm() {
        return currentRegistrationForm;
    }

    public void setCurrentRegistrationForm(RegistrationForm currentRegistrationForm) {
        this.currentRegistrationForm = currentRegistrationForm;
    }

    public OfficerStatus getOfficerStatus() {
        return officerStatus;
    }

    public void setOfficerStatus(OfficerStatus officerStatus) {
        this.officerStatus = officerStatus;
    }

    public List<RegistrationForm> getMyRegistrationForms() {//Read-only as a whole
        return myRegistrationForms;
    }

    public void addRegistrationForm(RegistrationForm registrationForm) {
        myRegistrationForms.add(registrationForm);
    }

    public void removeRegistrationForm(RegistrationForm registrationForm) {
        myRegistrationForms.remove(registrationForm);
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    @Override
    public String toString() {
        return "========== Officer Information ==========\n" +
                "Name           : " + super.getName() + "\n" +
                "NRIC           : " + super.getNric() + "\n" +
                "Password       : " + super.getPassword() + "\n" +
                "Age            : " + super.getAge() + "\n" +
                "Marital Status : " + super.getMaritalStatus() + "\n" +
                "Officer Status : " + officerStatus + "\n" +
                "Current Project: " + (currentProject != null ? currentProject.getProjectName() : "") + "\n" +
                "=========================================";
    }
}
