package manager;

import applicant.Applicant;
import applicant.Application;
import enquiry.Enquiry;
import enums.FlatType;
import enums.MaritalStatus;
import enums.UserType;
import user.UserFilterSettings;
import user.User;

public class Manager extends User {    public Manager(String name, String nric, String password, int age, MaritalStatus maritalStatus, UserFilterSettings filterSettings, UserType userType) {
        super(name, nric, password, age, maritalStatus, filterSettings, userType);
    }

    @Override
    public void displayMenu() {

    }
}
