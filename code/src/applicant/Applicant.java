package applicant;

import enquiry.Enquiry;
import project.FlatType;
import user.MaritalStatus;
import user.User;
import user.UserFilterSettings;

import java.util.ArrayList;
import java.util.List;

public class Applicant extends User {
   private final List<Application> myApplications;
   private final List<Enquiry> enquiries;
   private FlatType bookedFlatType;

   public Applicant(String name, String nric, String password, int age, MaritalStatus maritalStatus, UserFilterSettings filterSettings,
                    List<Application> loadedApplications, List<Enquiry> loadedEnquiries, FlatType bookedFlatType) {
      super(name, nric, password, age, maritalStatus, filterSettings);

      // make defensive copies so the internal state can’t be modified from outside
      this.myApplications  = new ArrayList<>(loadedApplications);
      this.enquiries       = new ArrayList<>(loadedEnquiries);
      this.bookedFlatType  = bookedFlatType;
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
