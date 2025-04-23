package applicant;

import enquiry.Enquiry;
import project.FlatType;
import user.MaritalStatus;
import user.User;

import java.util.ArrayList;
import java.util.List;

public class Applicant extends User {
   private final List<Application> myApplications;
   private final List<Enquiry> enquiries;
   private FlatType bookedFlatType;

   public Applicant(
      String name,
      String nric,
      String password,
      int age,
      MaritalStatus maritalStatus,
      List<Application> loadedApplications,
      List<Enquiry> loadedEnquiries,
      FlatType bookedFlatType
   ) {
      // Call the new User constructor: no filterSettings param.
      super(name, nric, password, age, maritalStatus);
      
      // defensive copies
      this.myApplications = new ArrayList<>(loadedApplications);
      this.enquiries      = new ArrayList<>(loadedEnquiries);
      this.bookedFlatType = bookedFlatType;
   }

   public List<Application> getMyApplications() {
      return myApplications;
   }

   public List<Enquiry> getEnquiries() {
      return enquiries;
   }

   public FlatType getBookedFlatType() {
      return bookedFlatType;
   }

   public void setBookedFlatType(FlatType bookedFlatType) {
      this.bookedFlatType = bookedFlatType;
   }
}
