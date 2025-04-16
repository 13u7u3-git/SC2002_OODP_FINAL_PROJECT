package user;

import enums.FlatType;

import java.time.LocalDate;

public class UserFilterSettings {
    private String projectName;
    private String neighbourhood;
    private FlatType flatType;
    private LocalDate applicationOpeningDate;
    private LocalDate applicationClosingDate;
    private String sortOrder;

    public UserFilterSettings() {
        this.projectName = null;
        this.neighbourhood = null;
        this.flatType = null;
        this.applicationOpeningDate = null;
        this.applicationClosingDate = null;
        this.sortOrder = "alphabetical"; // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public FlatType getFlatType() {
        return flatType;
    }

    public void setFlatType(FlatType flatType) {
        this.flatType = flatType;
    }

    public LocalDate getApplicationOpeningDate() {
        return applicationOpeningDate;
    }

    public void setApplicationOpeningDate(LocalDate applicationOpeningDate) {
        this.applicationOpeningDate = applicationOpeningDate;
    }

    public LocalDate getApplicationClosingDate() {
        return applicationClosingDate;
    }

    public void setApplicationClosingDate(LocalDate applicationClosingDate) {
        this.applicationClosingDate = applicationClosingDate;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void reset() {
        projectName = null;
        neighbourhood = null;
        flatType = null;
        applicationOpeningDate = null;
        applicationClosingDate = null;
        sortOrder = "alphabetical";
    }
}
