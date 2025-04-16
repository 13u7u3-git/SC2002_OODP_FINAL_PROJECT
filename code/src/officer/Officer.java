package officer;

import applicant.Applicant;
import applicant.Application;
import enquiry.Enquiry;
import enums.FlatType;
import enums.MaritalStatus;
import enums.UserType;
import user.UserFilterSettings;

import java.util.List;

public class Officer extends Applicant {
    public Officer(String name, String nric, String password, int age, MaritalStatus maritalStatus, UserFilterSettings filterSettings, UserType userType, List<Application> applicationList, FlatType bookedFlatType, List<Enquiry> enquiries) {
        super(name, nric, password, age, maritalStatus, filterSettings, userType, applicationList, bookedFlatType, enquiries);
    }
}
