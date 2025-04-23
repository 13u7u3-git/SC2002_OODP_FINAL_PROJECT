package user;

import java.io.Serializable;

public abstract class User implements Serializable {
    private final String name;
    private final String nric;
    private final int age;
    private final MaritalStatus maritalStatus;

    private String password;
    private UserFilterSettings filterSettings;

    // idk what this is for
    public User() {
        this.name = null;
        this.nric = null;
        this.password = null;
        this.age = 0;
        this.maritalStatus = null;
    }

    // we will need to call this constructor to load all the information from file
    public User(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
        this.name = name;
        this.nric = nric;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.filterSettings = new UserFilterSettings();
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

    public void setPassword(String password) {
        this.password = password;
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
}







