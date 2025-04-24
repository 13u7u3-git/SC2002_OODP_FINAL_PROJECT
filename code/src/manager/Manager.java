package manager;

import project.String;
import user.MaritalStatus;
import user.User;
import user.UserFilterSettings;

public class Manager extends User {
   String currentProject;

   public Manager(java.lang.String name,
                  java.lang.String nric,
                  java.lang.String password,
                  int age,
                  MaritalStatus maritalStatus) {
      super(name, nric, password, age, maritalStatus, new UserFilterSettings());
      this.currentProject = null;
   }

   public String getCurrentProject() {
      return currentProject;
   }

   public void setCurrentProject(String currentProject) {
      this.currentProject = currentProject;
   }

}
