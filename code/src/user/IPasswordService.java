package user;

public interface IPasswordService {
   void isPasswordMatch(User user, String password);
   void validateChangePassword(User user, String old, String newPassword1, String newPassword2);
   void changePassword(String old, String newPassword1, String newPassword2);
   //   default void validatePassword(String password) throws IllegalArgumentException {
//      getPasswordValidationService().isPasswordMatch(getUser(), password);
//   }
//
//   default void changePassword(String oldPassword, String newPassword1, String newPassword2) {
//      getPasswordValidationService().validateChangePassword(getUser(), oldPassword, newPassword1, newPassword2);
//      getUser().setPassword(newPassword1);
//   }
}

