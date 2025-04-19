package applicant;

import project.FlatType;
import project.Project;
import user.User;

import java.io.Serializable;
import java.time.LocalDate;

public class Application implements Serializable {
   private final int id;
   private final User applicant;
   private final Project project;
   private final FlatType flatType;
   private final LocalDate dateApplied;
   private ApplicationStatus applicationStatus;
   private BookingStatus bookingStatus;
   private WithdrawalRequestStatus withdrawalRequestStatus;

   protected Application(int id, User applicant, Project project, FlatType flatType) {
      this.id = id;
      this.applicant = applicant;
      this.project = project;
      this.flatType = flatType;
      this.applicationStatus = ApplicationStatus.PENDING;
      this.bookingStatus = BookingStatus.NOT_BOOKED;
      this.withdrawalRequestStatus = WithdrawalRequestStatus.NOT_REQUESTED;
      this.dateApplied = LocalDate.now();
   }

   // to load data for data persistence
   protected Application(int id, User applicant, Project project, FlatType flatType, ApplicationStatus applicationStatus, BookingStatus bookingStatus,
                         WithdrawalRequestStatus withdrawalRequestStatus, LocalDate dateApplied) {
      this.id = id;
      this.applicant = applicant;
      this.project = project;
      this.flatType = flatType;
      this.applicationStatus = applicationStatus;
      this.bookingStatus = bookingStatus;
      this.withdrawalRequestStatus = withdrawalRequestStatus;
      this.dateApplied = dateApplied;
   }

   public int getId() {
      return id;
   }

   public User getApplicant() {
      return applicant;
   }

   public Project getProject() {
      return project;
   }

   public FlatType getFlatType() {
      return flatType;
   }

   public ApplicationStatus getApplicationStatus() {
      return applicationStatus;
   }

   void setApplicationStatus(ApplicationStatus applicationStatus) {
      this.applicationStatus = applicationStatus;
   }

   public BookingStatus getBookingStatus() {
      return bookingStatus;
   }

   public void setBookingStatus(BookingStatus bookingStatus) {
      this.bookingStatus = bookingStatus;
   }

   public WithdrawalRequestStatus getWithdrawalRequestStatus() {
      return withdrawalRequestStatus;
   }

   void setWithdrawalRequestStatus(WithdrawalRequestStatus withdrawalRequestStatus) {
      this.withdrawalRequestStatus = withdrawalRequestStatus;
   }

   public LocalDate getDateApplied() {
      return dateApplied;
   }

   @Override
   public String toString() {
      return "Application {" +
              "ID=" + id +
              ", Applicant=" + applicant.getName() +
              ", Project=" + project.getProjectName() +
              ", Flat Type=" + flatType +
              ", Application Status=" + applicationStatus +
              ", Withdrawal Status=" + withdrawalRequestStatus +
              ", Booking Status=" + bookingStatus +
              ", Date Applied=" + dateApplied +
              '}';
   }
}
