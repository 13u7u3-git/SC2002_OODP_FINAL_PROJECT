package user;

import project.FlatType;

import java.io.Serializable;
import java.time.LocalDate;

public class User implements Serializable {
    // --- Core user fields ---
    private final String name;
    private final String nric;
    private final int age;
    private final MaritalStatus maritalStatus;

    // --- Authentication ---
    private String password;

    // --- “Flattened” filter settings ---
    private String projectName;
    private String neighbourhood;
    private FlatType flatType;
    private LocalDate date;
    private String sortOrder = "alphabetical";

    // --- Constructors ---

    /** No-arg constructor for serialization/frameworks. */
    public User() {
        this.name = null;
        this.nric = null;
        this.age = 0;
        this.maritalStatus = null;
        // password is null
        // filter fields are null or default
    }

    public User(String name,
                String nric,
                String password,
                int age,
                MaritalStatus maritalStatus,
                // initial filter‐settings
                String projectName,
                String neighbourhood,
                FlatType flatType,
                LocalDate date,
                String sortOrder) {
        this.name = name;
        this.nric = nric;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;

        this.projectName = projectName;
        this.neighbourhood = neighbourhood;
        this.flatType = flatType;
        this.date = date;
        this.sortOrder = sortOrder != null ? sortOrder : "alphabetical";
    }

    // --- Core getters/setters ---

    public String getName() {
        return name;
    }

    public String getNric() {
        return nric;
    }

    public int getAge() {
        return age;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // --- “Flattened” filter getters/setters ---

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    /** Reset all filter fields back to defaults. */
    public void resetFilters() {
        this.projectName = null;
        this.neighbourhood = null;
        this.flatType = null;
        this.date = null;
        this.sortOrder = "alphabetical";
    }
}
