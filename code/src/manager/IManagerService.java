package manager;

import officer.RegistrationForm;
import officer.RegistrationStatus;
import project.FlatType;
import project.Project;
import user.IUserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IManagerService extends IUserService {
   void setUser(Manager manager);

   void createProject(String projectName, String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                      Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                      LocalDate applicationClosingDate, String manager, Integer availableOfficerSlots,
                      List<String> officers);

   void updateProject(Project project, String name, String neighbourhood, Map<FlatType, Integer> availableFlats, LocalDate openingDate, LocalDate closingDate, boolean visibility);

   void deleteProject(Integer projectId);


   //List<Project> getAllProjects();

   List<Project> getMyProjects();

   List<RegistrationForm> getPendingOfficerRegistrations() throws Exception;

   Project getCurrentProject();

   void setCurrentProject(Project project);

   void viewAllEnquiries();

   String setRegistrationStatus(String identifier, RegistrationStatus status) throws Exception;

   void addToOfficersList(String officerStr);
}