package manager;

import project.FlatType;
import project.Project;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IManagerService {
   void createProject(String projectName, String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                      Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                      LocalDate applicationClosingDate, String manager, Integer availableOfficerSlots,
                      List<String> officers);

   void updateProject(Project project, String name, String neighbourhood, Map<FlatType, Integer> availableFlats, LocalDate openingDate, LocalDate closingDate, boolean visibility);

   void deleteProject(Integer projectId);


   //List<Project> getAllProjects();

   List<Project> getMyProjects();

   void viewAllEnquiries();

   void setCurrentProject(Project project);
}