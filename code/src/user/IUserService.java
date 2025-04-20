package user;

public interface IUserService {
   void validatePassword(String password);

   void changePassword(String oldPassword, String newPassword1, String newPassword2);
}

