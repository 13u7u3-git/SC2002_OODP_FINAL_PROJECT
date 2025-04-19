package project;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;


public interface IProjectService {


   Project getProjectById(Integer projectId);


   List<Project> getAllProjects();


   void printProjectsToTable(List<Project> projects);


   public Project createProject(String projectName, String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                                Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                                LocalDate applicationClosingDate, String manager, Integer availableOfficerSlots,
                                List<String> officers);

   void deleteProject(Project project);


   void addProjectToRegistry(Project project);


   void removeProjectFromRegistry(Project project);

   List<Project> getFilteredProjects(Predicate<Project> predicate);
}