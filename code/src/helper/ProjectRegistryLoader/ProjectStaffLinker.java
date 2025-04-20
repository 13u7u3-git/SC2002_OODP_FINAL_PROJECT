package helper.ProjectRegistryLoader;

import manager.Manager;
import officer.Officer;
import project.ProjectRegistry;
import user.UserRegistry;

import java.time.LocalDate;
import java.util.List;

public class ProjectStaffLinker { //Link Project to Officer
   private final ProjectRegistry projectRegistry;
   private final UserRegistry userRegistry;

   public ProjectStaffLinker(ProjectRegistry projectRegistry, UserRegistry userRegistry) {
      this.projectRegistry = projectRegistry;
      this.userRegistry = userRegistry;
   }

   public void linkProjectToOfficer() {
      projectRegistry.getProjects().forEach(project -> {
         //check if LocalDate is within Application open close dates inclusive,
         if (project.getApplicationOpeningDate().isAfter(LocalDate.now()) || project.getApplicationClosingDate().isBefore(LocalDate.now()) || project.getApplicationClosingDate().isEqual(LocalDate.now()) || project.getApplicationOpeningDate().isEqual(LocalDate.now())) {
            List<String> officers = project.getOfficers();
            for (String o : officers) {
               Officer officer = (Officer) userRegistry.getUser(o);
               officer.setCurrentProject(project);
               //print
               System.out.println(officer.getName() + "(Officer) is linked to " + officer.getCurrentProject().getProjectName());
            }
         }
      });
   }

   public void linkProjectToManager() {
      projectRegistry.getProjects().forEach(project -> {
         //check if LocalDate is within Application open close dates inclusive,
         if (project.getApplicationOpeningDate().isAfter(LocalDate.now()) || project.getApplicationClosingDate().isBefore(LocalDate.now()) || project.getApplicationClosingDate().isEqual(LocalDate.now()) || project.getApplicationOpeningDate().isEqual(LocalDate.now())) {
            String managerStr = project.getManager();
            Manager manager = (Manager) userRegistry.getUser(managerStr);
            if (manager == null) {
               System.out.println("Manager not found, Project Not Linked. Check for spelling errors");
               return;
            }
            manager.setCurrentProject(project);
            System.out.println(manager.getName() + "(Manager) is linked to " + manager.getCurrentProject().getProjectName());
         }
      });
   }
}
