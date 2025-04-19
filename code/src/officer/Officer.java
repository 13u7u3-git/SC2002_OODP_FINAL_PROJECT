package officer;

import applicant.Applicant;
import user.MaritalStatus;

public class Officer extends Applicant {
   public Officer(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
      super(name, nric, password, age, maritalStatus);
   }
}
