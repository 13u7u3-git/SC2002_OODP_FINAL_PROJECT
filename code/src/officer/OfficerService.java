package officer;

import UniqueID.IDGenerator;
import UniqueID.IdType;
import applicant.Application;
import applicant.ApplicationStatus;
import applicant.BookingStatus;
import enquiry.Enquiry;
import helper.Color;
import project.FlatType;
import project.Project;
import project.ProjectService;
import system.ServiceRegistry;
import user.IUserService;
import user.User;
import user.UserRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OfficerService implements IUserService {
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
        RegistrationForm form = new RegistrationForm(IDGenerator.getInstance().getNextId(IdType.REGISTRATION), officer.getName(), officer.getNric(), projectService.getProjectByName(projectStr).getId(), projectStr);
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
        return new RegistrationForm(IDGenerator.getInstance().getNextId(IdType.REGISTRATION), officer.getName(), officer.getNric(), projectService.getProjectByName(projectName).getId(), projectName);
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

    public void bookFlat(Application application) {
        Project project = officer.getCurrentProject();
        if (project == null) {
            throw new IllegalStateException("You are not assigned to any project.");
        }

        // Ensure the application is successful
        if (application.getApplicationStatus() != ApplicationStatus.SUCCESSFUL) {
            throw new IllegalArgumentException("Only successful applications can be booked.");
        }

        // Check the flat type and decrement the corresponding count
        if (application.getFlatType() == FlatType.TWO_ROOM) {
            if (project.getTwoRoomUnits() > 0) {
                Map<FlatType, Integer> remainingFlats = project.getRemainingFlats();
                if (remainingFlats.containsKey(FlatType.TWO_ROOM)) {
                    int currentCount = remainingFlats.get(FlatType.TWO_ROOM);
                    if (currentCount > 0) {
                        remainingFlats.put(FlatType.TWO_ROOM, currentCount - 1);
                    }
                }

            } else {
                throw new IllegalStateException("No available two-room flats left.");
            }
        } else if (application.getFlatType() == FlatType.THREE_ROOM) {
            if (project.getThreeRoomUnits() > 0) {
                Map<FlatType, Integer> remainingFlats = project.getRemainingFlats();
                if (remainingFlats.containsKey(FlatType.THREE_ROOM)) {
                    int currentCount = remainingFlats.get(FlatType.THREE_ROOM);
                    if (currentCount > 0) {
                        remainingFlats.put(FlatType.THREE_ROOM, currentCount - 1);
                    }
                }

            } else {
                throw new IllegalStateException("No available three-room flats left.");
            }
        } else {
            throw new IllegalArgumentException("Invalid flat type.");
        }

        // Generate receipt or further booking actions
        projectService.updateProject(project);

        application.setBookingStatus(BookingStatus.BOOKED);
        projectService.updateApplicationStatus(application);

        Color.println("Flat booked successfully for " + application.getApplicant().getName(), Color.GREEN);
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


//package officer;
//
//import helper.Color;
//import project.IProjectService;
//import project.Project;
//import system.ServiceRegistry;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class OfficerController {
//
//   private final IOfficerService officerService;
//   private final IProjectService projectService;
//
//   public OfficerController() {
//      this.officerService = ServiceRegistry.get(IOfficerService.class);
//      this.projectService = ServiceRegistry.get(IProjectService.class);
//   }
//
//   public List<List<String>> getAllProjectsTableData() throws Exception {
//      List<Project> projects = projectService.getAllProjects();
//      if (projects == null || projects.isEmpty()) {
//         return null;
//      }
//      else {
//         List<String> headerRow = List.of("Project ID", "Project Name", "Neighbourhood", "Visibility", "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price", "Appln..Opening Date", "Appln..Closing Date", "Manager", "Officer Slots", "Officers");
//         List<List<String>> tableData = new ArrayList<>();
//         tableData.add(headerRow);
//         for (Project p : projects) {
//            List<String> fromEachProject = p.toStringAsList();
//            tableData.add(fromEachProject);
//         }
//         return tableData;
//      }
//   }
//
//   public List<List<String>> getOfficerEligibleProjectsTableData() throws Exception {
//      if (officerService.getOfficerStatus() != OfficerStatus.INACTIVE) {
//         throw new IllegalStateException("You are already registered as an officer for the project.");
//      }
//
//      List<Project> projects = projectService.getFilteredProjects(project ->
//              project.getOfficers().size() < project.getAvailableOfficerSlots()
//      );
//
//      if (projects == null || projects.isEmpty()) {
//         return null;
//      }
//
//      List<String> headerRow = List.of("Project ID", "Project Name", "Neighbourhood", "Visibility",
//              "Two Room Units", "Two Room Price", "Three Room Units",
//              "Three Room Price", "Appln..Opening Date", "Appln..Closing Date",
//              "Manager", "Officer Slots", "Officers");
//
//      List<List<String>> tableData = new ArrayList<>();
//      tableData.add(headerRow);
//
//      for (Project project : projects) {
//         List<String> row = List.of(
//                 project.getId().toString(),
//                 project.getProjectName(),
//                 project.getNeighborhood(),
//                 project.isVisibility() ? "Visible" : "Hidden",
//                 project.getTwoRoomUnits().toString(),
//                 project.getTwoRoomPrice().toString(),
//                 project.getThreeRoomUnits().toString(),
//                 project.getThreeRoomPrice().toString(),
//                 project.getApplicationOpeningDate().toString(),
//                 project.getApplicationClosingDate().toString(),
//                 project.getManager(),
//                 project.getAvailableOfficerSlots().toString(),
//                 project.getOfficers().toString()
//         );
//         tableData.add(row);
//      }
//
//      return tableData;
//   }
//
//
//   public RegistrationForm CreateRegistrationForm(String project) {
//      // TODO : check if officer is already an applicant for the project
//
//      String projectStr = null;
//      projectStr = projectService.returnNameIfProjectExists(project.trim());
//      if (projectStr == null) {
//         Color.println("Form not Created. Project not found or Error Creating Form. Try Again", Color.RED);
//         return null;
//      }
//      RegistrationForm registrationForm = officerService.createRegistrationForm(projectStr);
//      Color.println(registrationForm.toString(), Color.YELLOW);
//      return registrationForm;
//   }
//
//   public void sendRegistrationRequest(RegistrationForm form) throws IllegalArgumentException {
//      officerService.sendRegistrationRequest(form);
//      officerService.setOfficerStatus(OfficerStatus.PENDING);
//      //Color.println(" Registration Request Sent and set officer status", Color.GREEN);
//      officerService.setCurrentRegistrationForm(form);
//      officerService.addToMyRegistrations(form);
//   }
//
//   public OfficerStatus getOfficerStatus() {
//      return officerService.getOfficerStatus();
//   }
//
//   public Project getCurrentProject() throws Exception {
//      return officerService.getCurrentProject();
//   }
//
//   public RegistrationForm getCurrentRegistrationForm() throws Exception {
//      return officerService.getCurrentRegistrationForm();
//   }
//
//   public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
//      officerService.changePassword(oldPassword, newPassword, confirmPassword);
//   }
//
//}
//
