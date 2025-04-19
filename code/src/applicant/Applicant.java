package applicant;

import enquiry.Enquiry;
import project.FlatType;
import user.MaritalStatus;
import user.User;
import user.UserFilterSettings;

import java.util.ArrayList;
import java.util.List;

public class Applicant extends User {
   private final List<Application> applicationList;
   private final List<Enquiry> enquiries;
   private FlatType bookedFlatType;

   public Applicant(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
      super(name, nric, password, age, maritalStatus, new UserFilterSettings());
      this.applicationList = new ArrayList<>();
      this.bookedFlatType = null;
      this.enquiries = new ArrayList<>();
   }

   public List<Application> getApplicationList() {
      return applicationList;
   }

   public void addApplication(Application application) {
      applicationList.add(application);
   }

   public void removeApplication(Application application) {
      applicationList.remove(application);
   }

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

   public void removeEnquiry(Enquiry enquiry) {
      enquiries.remove(enquiry);
   }
}
