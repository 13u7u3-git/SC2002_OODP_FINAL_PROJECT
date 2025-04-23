package officer;

import applicant.ApplicantController;
import applicant.ApplicantMenu;
import applicant.ApplicantService;
import enquiry.Enquiry;
import helper.Color;
import project.Project;
import project.ProjectService;
import system.ServiceRegistry;
import user.User;
import user.UserRegistry;

import java.util.ArrayList;
import java.util.List;

public class OfficerService {
    private final ProjectService projectService;
    private Officer officer;

    public OfficerService(ProjectService projectService) {
        this.projectService = projectService;
    }

    private void atLogin() {
        if (officer == null) {
            System.out.println("Error: Officer not initialized");
            return;
        }

        Project currentProject = officer.getCurrentProject();
        RegistrationForm currentRegistrationForm = getCurrentRegistrationForm();

        if (currentProject != null) {
            setOfficerStatus(OfficerStatus.ACTIVE);
            return;
        }

        if (currentRegistrationForm == null) {
            System.out.println("No pending registration forms found");
            return;
        }

        if (getOfficerStatus() == OfficerStatus.PENDING && !currentRegistrationForm.isPending()) {
            if (currentRegistrationForm.isApproved()) {
                setOfficerStatus(OfficerStatus.ACTIVE);
                Project project = projectService.getProjectById(currentRegistrationForm.getProjectId());
                if (project != null) {
                    officer.setCurrentProject(project);
                } else {
                    System.out.println("Warning: Approved project not found");
                }
            } else {
                setOfficerStatus(OfficerStatus.INACTIVE);
            }
        }
    }

    // --- Unified Controller Methods Below ---

    public List<List<String>> getAllProjectsTableData() throws Exception {
        List<Project> projects = projectService.getAllProjects();
        if (projects == null || projects.isEmpty()) return null;

        List<String> headerRow = List.of("Project ID", "Project Name", "Neighbourhood", "Visibility", "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price", "Appln..Opening Date", "Appln..Closing Date", "Manager", "Officer Slots", "Officers");
        List<List<String>> tableData = new ArrayList<>();
        tableData.add(headerRow);
        for (Project p : projects) tableData.add(p.toStringAsList());
        return tableData;
    }

    public List<List<String>> getOfficerEligibleProjectsTableData() throws Exception {
        if (getOfficerStatus() != OfficerStatus.INACTIVE)
            throw new IllegalStateException("You are already registered as an officer for the project.");

        List<Project> projects = projectService.getFilteredProjects(project ->
                project.getOfficers().size() < project.getAvailableOfficerSlots());

        if (projects == null || projects.isEmpty()) return null;

        List<String> headerRow = List.of("Project ID", "Project Name", "Neighbourhood", "Visibility", "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price", "Appln..Opening Date", "Appln..Closing Date", "Manager", "Officer Slots", "Officers");
        List<List<String>> tableData = new ArrayList<>();
        tableData.add(headerRow);

        for (Project p : projects) {
            tableData.add(List.of(
                    p.getId().toString(), p.getProjectName(), p.getNeighborhood(),
                    p.isVisibility() ? "Visible" : "Hidden",
                    p.getTwoRoomUnits().toString(), p.getTwoRoomPrice().toString(),
                    p.getThreeRoomUnits().toString(), p.getThreeRoomPrice().toString(),
                    p.getApplicationOpeningDate().toString(), p.getApplicationClosingDate().toString(),
                    p.getManager(), p.getAvailableOfficerSlots().toString(), p.getOfficers().toString()
            ));
        }

        return tableData;
    }

    public RegistrationForm createAndSendRegistrationForm(String project) {
        String projectStr = projectService.returnNameIfProjectExists(project.trim());
        if (projectStr == null) {
            Color.println("Form not Created. Project not found or Error Creating Form. Try Again", Color.RED);
            return null;
        }
        RegistrationForm form = new RegistrationForm(uniqueIdService.generateUniqueId(IdType.REGISTRATION_FORM_ID), officer.getName(), officer.getNric(), projectService.getProjectByName(projectStr).getId(), projectStr);
        projectService.addRegistrationToProject(form);
        setOfficerStatus(OfficerStatus.PENDING);
        setCurrentRegistrationForm(form);
        addToMyRegistrations(form);
        Color.println(form.toString(), Color.YELLOW);
        return form;
    }

    public List<Enquiry> getCurrentProjectEnquiries() {
        return List.of();
    }

    public RegistrationForm createRegistrationForm(String projectName) {
        return new RegistrationForm(uniqueIdService.generateUniqueId(IdType.REGISTRATION_FORM_ID), officer.getName(), officer.getNric(), projectService.getProjectByName(projectName).getId(), projectName);
    }

    public void sendRegistrationRequest(RegistrationForm form) {
        projectService.addRegistrationToProject(form);
    }

    public OfficerStatus getOfficerStatus() {
        return officer.getOfficerStatus();
    }

    public void setOfficerStatus(OfficerStatus status) {
        officer.setOfficerStatus(status);
    }

    public RegistrationForm getCurrentRegistrationForm() {
        return officer.getCurrentRegistrationForm();
    }

    public void setCurrentRegistrationForm(RegistrationForm form) {
        officer.setCurrentRegistrationForm(form);
    }

    public void addToMyRegistrations(RegistrationForm form) {
        officer.addRegistrationForm(form);
    }

    public void removeRegistrationForm(RegistrationForm form) {
        officer.removeRegistrationForm(form);
    }

    public Project getCurrentProject() {
        return officer.getCurrentProject();
    }

    public User getUser() {
        return this.officer;
    }

    public void setUser(Officer officer) {
        this.officer = officer;
        this.atLogin();
    }

    public void setOfficerCurrentProject(String officerName, Project currentProject) {
        UserRegistry userRegistry = ServiceRegistry.get(UserRegistry.class);
        Officer officer = (Officer) userRegistry.getUser(officerName);
        officer.setCurrentProject(currentProject);
    }

    public IPasswordValidationService getPasswordValidationService() {
        return this.passwordValidationService;
    }

    public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
        passwordValidationService.changePassword(officer, oldPassword, newPassword, confirmPassword);
    }
}


//package officer;
//
//import UniqueID.IUniqueIdService;
//import UniqueID.IdType;
//import enquiry.Enquiry;
//import interfaces.StaffService;
//import project.IProjectService;
//import project.Project;
//import system.ServiceRegistry;
//import user.IPasswordValidationService;
//import user.User;
//import user.UserRegistry;
//
//import java.util.List;
//
//public class OfficerService implements IOfficerService, StaffService {
//   private final IProjectService projectService;
//   private final IUniqueIdService uniqueIdService;
//   private final IPasswordValidationService passwordValidationService;
//   private Officer officer;
//
//   public OfficerService() {
//
//      this.projectService = ServiceRegistry.get(IProjectService.class);
//      this.uniqueIdService = ServiceRegistry.get(IUniqueIdService.class);
//      this.passwordValidationService = ServiceRegistry.get(IPasswordValidationService.class);
//   }
//
//   private void atLogin() {
//      // Check if officer object is initialized
//      if (officer == null) {
//         System.out.println("Error: Officer not initialized");
//         return;
//      }
//
//      Project currentProject = officer.getCurrentProject();
//      RegistrationForm currentRegistrationForm = getCurrentRegistrationForm();
//
//      if (currentProject != null) {
//         //System.outprintln("Restoring officer status to ACTIVE");
//         setOfficerStatus(OfficerStatus.ACTIVE);
//         return;
//      }
//
//      if (currentRegistrationForm == null) {
//         System.out.println("No pending registration forms found");
//         return; // don't do anything
//      }
//
//      if (getOfficerStatus() == OfficerStatus.PENDING && !currentRegistrationForm.isPending()) {
//         // simulating active listener
//         if (currentRegistrationForm.isApproved()) {
//            //System.outprintln("Registration form approved. Setting status to ACTIVE");
//            setOfficerStatus(OfficerStatus.ACTIVE);
//            Project project = projectService.getProjectById(currentRegistrationForm.getProjectId());
//            if (project != null) {
//               officer.setCurrentProject(project);
//               //System.outprintln("Assigned to project: " + project.getProjectName());
//            }
//            else {
//               System.out.println("Warning: Approved project not found");
//            }
//         }
//         else {
//            //System.outprintln("Registration form rejected. Setting status to INACTIVE");
//            setOfficerStatus(OfficerStatus.INACTIVE);
//         }
//      }
//   }
//
//   @Override
//   public List<Enquiry> getCurrentProjectEnquiries() {
//      return List.of();
//   }
//
//   @Override
//   public RegistrationForm createRegistrationForm(String projectName) {
//      return new RegistrationForm(uniqueIdService.generateUniqueId(IdType.REGISTRATION_FORM_ID), officer.getName(), officer.getNric(), projectService.getProjectByName(projectName).getId(), projectName);
//   }
//
//   @Override
//   public void sendRegistrationRequest(RegistrationForm form) throws IllegalArgumentException {
//      projectService.addRegistrationToProject(form);
//   }
//
//   @Override
//   public OfficerStatus getOfficerStatus() {
//      //System.out.println("Officer status: " + officer.getOfficerStatus());
//      return officer.getOfficerStatus();
//   }
//
//   @Override
//   public void setOfficerStatus(OfficerStatus status) {
//      officer.setOfficerStatus(status);
//   }
//
//   @Override
//   public RegistrationForm getCurrentRegistrationForm() {
//      return officer.getCurrentRegistrationForm();
//   }
//
//   @Override
//   public void setCurrentRegistrationForm(RegistrationForm form) {
//      officer.setCurrentRegistrationForm(form);
//   }
//
//   @Override
//   public void addToMyRegistrations(RegistrationForm form) {
//      officer.addRegistrationForm(form);
//   }
//
//   @Override
//   public void removeRegistrationForm(RegistrationForm form) {
//      officer.removeRegistrationForm(form);
//   }
//
//   @Override
//   public Project getCurrentProject() {
//      return officer.getCurrentProject();
//   }
//
//   @Override
//   public User getUser() {
//      return this.officer;
//   }
//
//   @Override
//   public void setUser(Officer officer) {
//      this.officer = officer;
//      this.atLogin();
//   }
//
//   @Override
//   public void setOfficerCurrentProject(String officerName, Project currentProject) {
//      UserRegistry userRegistry = ServiceRegistry.get(UserRegistry.class);
//      Officer officer = (Officer) userRegistry.getUser(officerName);
//      officer.setCurrentProject(currentProject);
//   }
//
//   @Override
//   public IPasswordValidationService getPasswordValidationService() {
//      return this.passwordValidationService;
//   }
//}