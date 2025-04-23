package applicant;

import project.FlatType;
import project.Project;
import user.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Application implements Serializable {
    private static final long serialVersionUID = 1L;

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

    protected Application(int id, User applicant, Project project, FlatType flatType,
                          ApplicationStatus applicationStatus, BookingStatus bookingStatus,
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
        return "============== Application Details ==============\n" +
                "Application ID      : " + id + "\n" +
                "Applicant           : " + applicant.getName() + "\n" +
                "Project             : " + project.getProjectName() + "\n" +
                "Flat Type           : " + flatType + "\n" +
                "Application Status  : " + applicationStatus + "\n" +
                "Withdrawal Status   : " + withdrawalRequestStatus + "\n" +
                "Booking Status      : " + bookingStatus + "\n" +
                "Date Applied        : " + dateApplied + "\n" +
                "==============================================";
    }

    public List<String> toTableRow() {
        return List.of(
                String.valueOf(id),
                applicant.getName(),
                project.getProjectName(),
                flatType.name(),
                dateApplied.toString(),
                applicationStatus.name(),
                bookingStatus.name(),
                withdrawalRequestStatus.name()
        );
    }
}
