package officer;

import enums.OfficerRegistrationStatus;
import helper.UniqueId;
import project.Project;

import java.time.LocalDate;

public class RegistrationForm {
    private final int id;
    private final Officer officer;
    private final Project project;
    private OfficerRegistrationStatus registrationStatus= OfficerRegistrationStatus.PENDING;
    private final LocalDate dateApplied;

    public RegistrationForm(Project project, Officer officer) {
        UniqueId IdGenerator = UniqueId.getInstance();
        this.id = IdGenerator.getNextRegistrationId();
        this.officer = officer;
        this.project = project;
        this.dateApplied = LocalDate.now();
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
