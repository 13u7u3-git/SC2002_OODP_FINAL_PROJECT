package applicant;

import enums.ApplicationStatus;
import enums.BookingStatus;
import enums.FlatType;
import enums.WithdrawalRequestStatus;
import helper.UniqueId;
import project.Project;
import user.User;

import java.time.LocalDate;

public class Application {
    private final int id;
    private final User applicant;
    private final Project project;
    private final FlatType flatType;
    private ApplicationStatus applicationStatus;
    private BookingStatus bookingStatus;
    private WithdrawalRequestStatus withdrawalRequestStatus;
    private final LocalDate dateApplied;

    Application(User applicant, Project project, FlatType flatType){
        UniqueId IdGenerator = UniqueId.getInstance();
        this.id = IdGenerator.getNextApplicationId();
        this.applicant = applicant;
        this.project = project;
        this.flatType = flatType;
        this.applicationStatus = ApplicationStatus.PENDING;
        this.withdrawalRequestStatus = WithdrawalRequestStatus.NOT_REQUESTED;
        this.dateApplied = LocalDate.now();
    }

    Application(int id, User applicant, Project project, FlatType flatType,
                       ApplicationStatus status, WithdrawalRequestStatus withdrawalRequestStatus, LocalDate dateApplied) {
        this.id = id;
        this.applicant = applicant;
        this.project = project;
        this.flatType = flatType;
        this.applicationStatus = status;
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

    public WithdrawalRequestStatus getWithdrawalRequestStatus() {
        return withdrawalRequestStatus;
    }

    void setWithdrawalRequestStatus(WithdrawalRequestStatus withdrawalRequestStatus) {
        this.withdrawalRequestStatus = withdrawalRequestStatus;
    }

    public LocalDate getDateApplied() {
        return dateApplied;
    }
}
