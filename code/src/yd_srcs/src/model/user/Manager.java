//package model.user;
//
//import enums.MaritalStatus;
//import enums.UserType;
//import model.project.Project;
//import util.UserFilterSettings;
//
//import java.util.List;
//
//public class Manager extends User{
//    private Project currentAssignedProject;
//    private final List<Project> assignedProjectList;
//
//    public Manager(String name, String nric, String password, int age, MaritalStatus maritalStatus,
//                   UserFilterSettings filterSettings, UserType userType,
//                   Project currentAssignedProject, List<Project> assignedProjectList) {
//        super(name, nric, password, age, maritalStatus, filterSettings, userType);
//        this.currentAssignedProject = currentAssignedProject;
//        this.assignedProjectList = assignedProjectList;
//    }
//
//    public Project getCurrentAssignedProject() {
//        return currentAssignedProject;
//    }
//
//    public void setCurrentAssignedProject(Project currentAssignedProject) {
//        this.currentAssignedProject = currentAssignedProject;
//    }
//
//    public List<Project> getAssignedProjectList() {
//        return assignedProjectList;
//    }
//
//    public void addAssignedProject(Project assignedProject) {
//        assignedProjectList.add(assignedProject);
//    }
//
//    public void displayMenu() {
//
//    }
//}
