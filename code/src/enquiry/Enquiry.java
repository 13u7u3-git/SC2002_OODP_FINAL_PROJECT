package enquiry;

import user.User;

import java.time.LocalDate;
import java.util.List;

public class Enquiry {
   private final Integer id;
   private final String projectName;
   private final Integer projectId;
   private final String applicantName;
   private final Integer applicantNric;
   private final LocalDate dateEnquired;
   private String enquiry;
   private String reply;
   private User respondent;
   private LocalDate dateCreated;
   private LocalDate dateReplied;

   protected Enquiry(Integer id, String projectName, Integer projectId, String applicantName, Integer applicantNric, LocalDate dateEnquired) {
      this.id = id;
      this.projectName = projectName;
      this.projectId = projectId;
      this.applicantName = applicantName;
      this.applicantNric = applicantNric;
      this.dateEnquired = dateEnquired;
      this.dateCreated = LocalDate.now();
   }

   public Integer getId() {
      return id;
   }

   public String getProjectName() {
      return projectName;
   }

   public String getApplicantName() {
      return applicantName;
   }

   public Integer getApplicantNric() {
      return applicantNric;
   }

   public Integer getProjectId() {
      return projectId;
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

   public LocalDate getDateCreated() {
      return dateCreated;
   }

   @Override
   public String toString() {
      return "============== Enquiry Details ==============\n" +
              "Enquiry ID          : " + id + "\n" +
              "Project             : " + projectName + "\n" +
              "Applicant           : " + applicantName + "\n" +
              "Date Enquired       : " + dateEnquired + "\n" +
              "Enquiry             : " + enquiry + "\n" +
              "Reply               : " + (reply != null ? reply : "N/A") + "\n" +
              "Respondent          : " + (respondent != null ? respondent.getName() : "N/A") + "\n" +
              "Date Replied        : " + (dateReplied != null ? dateReplied : "Pending") + "\n" +
              "==============================================";
   }

   public List<String> toStringList() {
      return List.of(
              String.valueOf(id),
              projectName,
              applicantName,
              dateEnquired.toString(),
              enquiry,
              reply != null ? reply : "N/A",
              respondent != null ? respondent.getName() : "Unanswered",
              dateReplied != null ? dateReplied.toString() : "Pending"
      );
   }
}