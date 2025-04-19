package officer;

import project.Project;

import java.time.LocalDate;

public class RegistrationForm {
   private final int id;
   private final Officer officer;
   private final Project project;
   private final LocalDate dateApplied;
   private OfficerRegistrationStatus registrationStatus;

   public RegistrationForm(int id, Officer officer, Project project) {
      this.id = id;
      this.officer = officer;
      this.project = project;
      this.registrationStatus = OfficerRegistrationStatus.PENDING;
      this.dateApplied = LocalDate.now();
   }

   // to load data for data persistence.
   public RegistrationForm(int id, Officer officer, Project project, OfficerRegistrationStatus registrationStatus, LocalDate dateApplied) {
      this.id = id;
      this.officer = officer;
      this.project = project;
      this.registrationStatus = registrationStatus;
      this.dateApplied = dateApplied;
   }

   public int getId() {
      return id;
   }

   public Officer getOfficer() {
      return officer;
   }

   public Project getProject() {
      return project;
   }

   public OfficerRegistrationStatus getRegistrationStatus() {
      return registrationStatus;
   }

   public void setRegistrationStatus(OfficerRegistrationStatus registrationStatus) {
      this.registrationStatus = registrationStatus;
   }

   public LocalDate getDateApplied() {
      return dateApplied;
   }
}
