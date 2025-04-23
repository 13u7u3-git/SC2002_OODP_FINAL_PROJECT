package manager;

import project.Project;
import user.MaritalStatus;
import user.User;

public class Manager extends User {
   private Project currentProject;

   public Manager(
      String name,
      String nric,
      String password,
      int age,
      MaritalStatus maritalStatus
   ) {
      // Call the new User constructor: no filterSettings param.
      super(name, nric, password, age, maritalStatus);
      this.currentProject = null;
   }

   public Project getCurrentProject() {
      return currentProject;
   }

   public void setCurrentProject(Project currentProject) {
      this.currentProject = currentProject;
   }
}
