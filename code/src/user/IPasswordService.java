package user;

public interface IPasswordService {
   void isPasswordMatch(User user, String password);

   void changePassword(User user, String old, String newPassword1, String newPassword2);
}

