package manager;

import officer.RegistrationForm;
import officer.RegistrationStatus;
import project.FlatType;
import project.String;
import user.IUserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IManagerService extends IUserService {
   void setUser(Manager manager);

   void createProject(java.lang.String projectName, java.lang.String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                      Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                      LocalDate applicationClosingDate, java.lang.String manager, Integer availableOfficerSlots,
                      List<java.lang.String> officers);

   void updateProject(String project, java.lang.String name, java.lang.String neighbourhood, Map<FlatType, Integer> availableFlats, LocalDate openingDate, LocalDate closingDate, boolean visibility);

   void deleteProject(Integer projectId);


   //List<Project> getAllProjects();

   List<String> getMyProjects();

   List<RegistrationForm> getPendingOfficerRegistrations() throws Exception;

   String getCurrentProject();

   void setCurrentProject(String project);

   void viewAllEnquiries();

   java.lang.String setRegistrationStatus(java.lang.String identifier, RegistrationStatus status) throws Exception;

   void addToOfficersList(java.lang.String officerStr);
}