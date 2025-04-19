package project;

import UniqueID.IUniqueIdService;
import UniqueID.IdType;
import helper.Color;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static helper.ColumnFormatter.formatColumn;

public class ProjectService implements IProjectService {
   private final ProjectRegistry projectRegistry;
   private final IUniqueIdService uniqueIdService;

   public ProjectService(ProjectRegistry projectRegistry, IUniqueIdService uniqueIdService) {
      this.projectRegistry = projectRegistry;
      this.uniqueIdService = uniqueIdService;
   }

   @Override
   public Project getProjectById(Integer projectId) {
      return projectRegistry.getProjects().stream()
              .filter(p -> p.getId().equals(projectId))
              .findFirst()
              .orElse(null);
   }

   @Override
   public List<Project> getAllProjects() {
      return projectRegistry.getProjects();
   }

   @Override
   /*public void printProjectsToTable(List<Project> projects) {
      Color.println("--- Projects Table ---", Color.YELLOW);
      Color.println("ID\t\tName\t\tNeighbourhood\t\tApplication Opening Date\t\tApplication Closing " +
              "Date\t\tManager\t\tVisibility \t\tAvaliable 2Room Flats\t\tAvaliable 3RoomFlats", Color.YELLOW);
      for (Project project : projects) {
         Color.println(project.getId() + "\t\t" + project.getProjectName() + "\t\t" + project.getNeighbourhood() + "\t\t" + project.getApplicationOpeningDate() + "\t\t" + project.getApplicationClosingDate() + "\t\t" + project.getManager() + "\t\t" + project.isVisibility() + "\t\t" + project.getAvailableFlats().get(FlatType.TWO_ROOM) + "\t\t" + project.getAvailableFlats().get(FlatType.THREE_ROOM), Color.YELLOW);
         //Color.println("Manager: " + project.getManager().getNric() + "\t\t", Color.YELLOW);
      }
      Color.println("----------------------", Color.YELLOW);
   }*/
   public void printProjectsToTable(List<Project> projects) {
      Color.println("--- Projects Table ---", Color.YELLOW);
      int COLUMN_WIDTH = 15;
      // Header
      String header = formatColumn("ID", COLUMN_WIDTH) + " | " +
              formatColumn("Name", COLUMN_WIDTH) + " | " +
              formatColumn("Neighbourhood", COLUMN_WIDTH) + " | " +
              formatColumn("App Opening Date", COLUMN_WIDTH) + " | " +
              formatColumn("App Closing Date", COLUMN_WIDTH) + " | " +
              formatColumn("Manager", COLUMN_WIDTH) + " | " +
              formatColumn("Visibility", COLUMN_WIDTH) + " | " +
              formatColumn("Available 2-Room", COLUMN_WIDTH) + " | " +
              formatColumn("Available 3-Room", COLUMN_WIDTH);
      Color.println(header, Color.YELLOW);

      // Data Rows
      if (projects == null || projects.isEmpty()) {
         // You might want to calculate the total width to center this message
         // For simplicity, just print it at the start
         Color.println("No projects found.", Color.YELLOW);
      }
      else {
         for (Project project : projects) {
            // Safely get available flat counts, defaulting to 0 if map or key is null
            Map<FlatType, Integer> availableFlats = project.getAvailableFlats();
            Integer twoRoomFlats = availableFlats != null ? availableFlats.getOrDefault(FlatType.TWO_ROOM, 0) : 0;
            Integer threeRoomFlats = availableFlats != null ? availableFlats.getOrDefault(FlatType.THREE_ROOM, 0) : 0;

            String row = formatColumn(project.getId(), COLUMN_WIDTH) + " | " +
                    formatColumn(project.getProjectName(), COLUMN_WIDTH) + " | " +
                    formatColumn(project.getNeighbourhood(), COLUMN_WIDTH) + " | " +
                    formatColumn(project.getApplicationOpeningDate(), COLUMN_WIDTH) + " | " +
                    formatColumn(project.getApplicationClosingDate(), COLUMN_WIDTH) + " | " +
                    formatColumn(project.getManager(), COLUMN_WIDTH) + " | " +
                    formatColumn(project.isVisibility(), COLUMN_WIDTH) + " | " +
                    formatColumn(twoRoomFlats, COLUMN_WIDTH) + " | " +
                    formatColumn(threeRoomFlats, COLUMN_WIDTH);
            Color.println(row, Color.YELLOW);
         }
      }

      Color.println("----------------------", Color.YELLOW);
   }

   // --- Project Management ---
   @Override
   public Project createProject(String projectName, String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                                Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                                LocalDate applicationClosingDate, String manager, Integer availableOfficerSlots,
                                List<String> officers) {
      Project project = new Project(uniqueIdService.generateUniqueId(IdType.PROJECT_ID), projectName, neighbourhood,
              twoRoomUnits, twoRoomPrice, threeRoomUnits, threeRoomPrice, applicationOpeningDate, applicationClosingDate, manager, availableOfficerSlots, officers);
      addProjectToRegistry(project);
      return project;
   }

   @Override
   public void deleteProject(Project project) {
      projectRegistry.removeProject(project);
   }

   @Override
   public void addProjectToRegistry(Project project) {
      projectRegistry.addProject(project);
   }

   @Override
   public void removeProjectFromRegistry(Project project) {
      projectRegistry.removeProject(project);
   }

   @Override
   public List<Project> getFilteredProjects(Predicate<Project> predicate) {
      return projectRegistry.filter(predicate);
   }


}