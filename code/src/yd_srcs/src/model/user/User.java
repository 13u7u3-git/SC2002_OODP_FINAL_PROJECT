package model.user;

import enums.MaritalStatus;
import enums.UserType;
import util.UserFilterSettings;

import java.util.Objects;

public abstract class User {
    private final String name;
    private final String nric;
    private String password;
    private final int age;
    private final MaritalStatus maritalStatus;
    private UserFilterSettings filterSettings;
    private final UserType userType;

    public User(String name, String nric, String password, int age, MaritalStatus maritalStatus,
                UserFilterSettings filterSettings, UserType userType) {
        this.name = name;
        this.nric = nric;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.filterSettings = filterSettings;
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public String getNric() {
        return nric;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public UserFilterSettings getFilterSettings() {
        return filterSettings;
    }

    public void setFilterSettings(UserFilterSettings filterSettings) {
        this.filterSettings = filterSettings;
    }

    public UserType getUserType() {
        return userType;
    }

    public boolean changePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            System.out.println("Password cannot be null or empty.");
            return false;
        }

        if (Objects.equals(this.password, password)) {
            System.out.println("Enter a different password.");
            return false;
        }

        this.password = password;
        return true;
    }

    public abstract void displayMenu();
}
