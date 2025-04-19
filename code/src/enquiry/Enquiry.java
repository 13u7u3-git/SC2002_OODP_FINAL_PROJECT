package enquiry;

import applicant.Applicant;
import project.Project;
import user.User;

import java.io.Serializable;
import java.time.LocalDate;

public class Enquiry implements Serializable {
   private final int id;
   private final Project project;
   private final Applicant applicant;
   private final LocalDate dateEnquired;
   private String enquiry;
   private String reply;
   private User respondent;
   private LocalDate dateReplied;

   protected Enquiry(int id, Project project, Applicant applicant, String enquiry) {
      this.id = id;
      this.project = project;
      this.applicant = applicant;
      this.enquiry = enquiry;
      this.dateEnquired = LocalDate.now();
   }

   // To load data for data persistence
   Enquiry(int id, Project project, Applicant applicant, String enquiry, LocalDate dateEnquired, String Reply, User respondent, LocalDate dateReplied) {
      this.id = id;
      this.project = project;
      this.applicant = applicant;
      this.enquiry = enquiry;
      this.dateEnquired = dateEnquired;
      this.reply = Reply;
      this.respondent = respondent;
      this.dateReplied = dateReplied;
   }

   public Integer getId() {
      return id;
   }

   public Project getProject() {
      return project;
   }

   public Applicant getApplicant() {
      return applicant;
   }

   public LocalDate getDateEnquired() {
      return dateEnquired;
   }

   public String getEnquiry() {
      return enquiry;
   }

   //package-private
   protected void setEnquiry(String enquiry) {
      this.enquiry = enquiry;
   }

   protected void setReply(String reply, User respondent) {
      this.reply = reply;
      this.respondent = respondent;
      this.dateReplied = LocalDate.now();
   }

   public String getReply() {
      return reply;
   }

   public User getRespondent() {
      return respondent;
   }

   public LocalDate getDateReplied() {
      return dateReplied;
   }


   @Override
   public String toString() {
      return "Enquiry{" +
              "id=" + id +
              ", project=" + project +
              ", applicant=" + applicant +
              ", dateEnquired=" + dateEnquired +
              ", enquiry='" + enquiry + '\'' +
              ", reply='" + reply + '\'' +
              ", respondent=" + respondent +
              ", dateReplied=" + dateReplied +
              '}';
   }
}
