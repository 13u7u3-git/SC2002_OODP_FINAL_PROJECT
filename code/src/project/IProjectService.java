package project;

import applicant.Application;
import enquiry.Enquiry;
import officer.RegistrationForm;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;


public interface IProjectService {


   String getProjectById(Integer projectId);

   String getProjectByName(java.lang.String projectName);

   List<String> getAllProjects();


   public String createProject(java.lang.String projectName, java.lang.String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                               Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                               LocalDate applicationClosingDate, java.lang.String manager, Integer availableOfficerSlots,
                               List<java.lang.String> officers);

   void deleteProject(String project);


   java.lang.String returnNameIfProjectExists(java.lang.String projectName);

   void addProjectToRegistry(String project);


   void removeProjectFromRegistry(String project);

   List<String> getFilteredProjects(Predicate<String> predicate);

   void validateNewProject(String project) throws IllegalArgumentException;

   void addRegistrationToProject(RegistrationForm form);

   List<List<java.lang.String>> getAllEnquiriesFromAllProjects();

   List<List<java.lang.String>> getEnquiriesFrom(java.lang.String projectId);

   void addEnquiryToProject(Enquiry enquiry);

   void removeEnquiryFromProject(Enquiry enquiry);

   void addApplicationToProject(Application application);
}