package project;

import officer.RegistrationForm;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;


public interface IProjectService {


   Project getProjectById(Integer projectId);

   Project getProjectByName(String projectName);

   List<Project> getAllProjects();


   public Project createProject(String projectName, String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                                Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                                LocalDate applicationClosingDate, String manager, Integer availableOfficerSlots,
                                List<String> officers);

   void deleteProject(Project project);


   String returnNameIfProjectExists(String projectName);

   void addProjectToRegistry(Project project);


   void removeProjectFromRegistry(Project project);

   List<Project> getFilteredProjects(Predicate<Project> predicate);

   void validateNewProject(Project project) throws IllegalArgumentException;

   void addRegistrationToProject(RegistrationForm form);
}