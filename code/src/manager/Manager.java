package manager;

import project.Project;
import user.MaritalStatus;
import user.User;
import user.UserFilterSettings;

public class Manager extends User {
   Project currentProject;

   public Manager(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
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
