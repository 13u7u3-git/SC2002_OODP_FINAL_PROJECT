package model.user;

import enums.FlatType;
import enums.MaritalStatus;
import enums.UserType;
import model.enquiry.Enquiry;
import model.form.Application;
import util.UserFilterSettings;

import java.util.List;

public class Applicant extends User{
    private final List<Application> applicationList;
    private FlatType bookedFlatType;
    private final List<Enquiry> enquiryList;

    public Applicant(String name, String nric, String password, int age, MaritalStatus maritalStatus,
                     UserFilterSettings filterSettings, UserType userType,
                     List<Application> applicationList, FlatType bookedFlatType, List<Enquiry> enquiryList) {
        super(name, nric, password, age, maritalStatus, filterSettings, userType);
        this.applicationList = applicationList;
        this.bookedFlatType = bookedFlatType;
        this.enquiryList =enquiryList;
    }

    public List<Application> getApplicationList() {
        return applicationList;
    }

    public void addApplication(Application application) {
        applicationList.add(application);
    }

    public FlatType getBookedFlatType() {
        return bookedFlatType;
    }

    public void setBookedFlatType(FlatType flatType) {
        this.bookedFlatType = flatType;
    }

    public List<Enquiry> getEnquiryList() {
        return enquiryList;
    }

    public void addEnquiry(Enquiry enquiry) {
        enquiryList.add(enquiry);
    }

    public void displayMenu() {

    }
}
