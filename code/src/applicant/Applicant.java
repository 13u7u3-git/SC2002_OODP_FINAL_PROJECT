package applicant;

import enums.FlatType;
import enums.MaritalStatus;
import enums.UserType;
import enquiry.Enquiry;
import user.User;
import user.UserFilterSettings;


import java.util.List;

public class Applicant extends User {
    private final List<Application> applicationList;
    private FlatType bookedFlatType;
    private final List<Enquiry> enquiries;

    public Applicant(String name, String nric, String password, int age, MaritalStatus maritalStatus,
                     UserFilterSettings filterSettings, UserType userType,
                     List<Application> applicationList, FlatType bookedFlatType, List<Enquiry> enquiries) {
        super(name, nric, password, age, maritalStatus, filterSettings, userType);
        this.applicationList = applicationList;
        this.bookedFlatType = bookedFlatType;
        this.enquiries = enquiries;
    }

    public List<Application> getApplicationList() {
        return applicationList;
    }

    public void addApplication(Application application) {
        applicationList.add(application);
    }

    public void removeApplication(Application application) {applicationList.remove(application); }

    public FlatType getBookedFlatType() {
        return bookedFlatType;
    }

    public void setBookedFlatType(FlatType flatType) {
        this.bookedFlatType = flatType;
    }

    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

    public void addEnquiry(Enquiry enquiry) {
        enquiries.add(enquiry);
    }

    public void removeEnquiry(Enquiry enquiry) { enquiries.remove(enquiry);}

    public void displayMenu() {
        //replace with actual menu options
        System.out.println("Applicant Menu:");
        System.out.println("1. View Profile");
        System.out.println("2. Update Profile");
        System.out.println("3. Logout");
    }
}
