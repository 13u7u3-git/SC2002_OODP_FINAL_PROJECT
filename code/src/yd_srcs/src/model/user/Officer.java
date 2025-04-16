package model.user;

import enums.FlatType;
import enums.MaritalStatus;
import enums.UserType;
import model.enquiry.Enquiry;
import model.form.Application;
import model.form.RegistrationForm;
import model.project.Project;
import util.UserFilterSettings;

import java.util.List;

public class Officer extends Applicant{
    private Project currentAssignedProject;
    private final List<Project> assignedProjectList;
    private final List<RegistrationForm> registrationFormList;

    public Officer(String name, String nric, String password, int age, MaritalStatus maritalStatus,
                   UserFilterSettings filterSettings, UserType userType,
                   List<Application> applicationList, FlatType bookedFlatType, List<Enquiry> enquiryList,
                   Project currentAssignedProject, List<Project> assignedProjectList, List<RegistrationForm> registrationFormList) {
        super(name, nric, password, age, maritalStatus, filterSettings, userType, applicationList, bookedFlatType, enquiryList);
        this.currentAssignedProject = currentAssignedProject;
        this.assignedProjectList = assignedProjectList;
        this.registrationFormList = registrationFormList;
    }

    public Project getCurrentAssignedProject() {
        return currentAssignedProject;
    }

    public void setCurrentAssignedProject(Project currentAssignedProject) {
        this.currentAssignedProject = currentAssignedProject;
    }

    public List<Project> getAssignedProjectList() {
        return assignedProjectList;
    }

    public void addAssignedProject(Project assignedProject) {
        assignedProjectList.add(assignedProject);
    }

    public List<RegistrationForm> getRegistrationFormList() {
        return registrationFormList;
    }

    public void addRegistrationForm(RegistrationForm registrationForm) {
        registrationFormList.add(registrationForm);
    }

    @Override
    public void displayMenu() {
        int i = 1;
    }
}
