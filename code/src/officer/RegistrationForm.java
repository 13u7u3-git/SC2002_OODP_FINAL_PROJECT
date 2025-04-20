package officer;

import java.time.LocalDate;

public class RegistrationForm {
   private final int id;
   private final String officer;
   private final String nric;
   private final Integer projectId;
   private final String projectName;
   private final LocalDate dateApplied;

   public RegistrationForm(int id, String officer, String nric, Integer projectId, String projectName) {
      this.id = id;
      this.officer = officer;
      this.nric = nric;
      this.projectId = projectId;
      this.projectName = projectName;
      this.dateApplied = LocalDate.now();
   }

   public int getId() {
      return id;
   }

   public String getOfficer() {
      return officer;
   }

   public Integer getProjectId() {
      return projectId;
   }

   public LocalDate getDateApplied() {
      return dateApplied;
   }

   public String getNric() {
      return nric;
   }

   @Override
   public String toString() {
      return "============== Registration Form ==============\n" +
              "Registration ID     : " + id + "\n" +
              "Officer             : " + officer + "\n" +
              "NRIC                : " + nric + "\n" +
              "Project ID          : " + projectId + "\n" +
              "Project Name        : " + projectName + "\n" +
              "Date Applied        : " + dateApplied + "\n" +
              "==============================================";
   }

}
