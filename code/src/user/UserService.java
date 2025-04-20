package user;

public class UserService implements IUserService {
   private final IPasswordValidationService passwordValidationService;
   private final User user;

   public UserService(IPasswordValidationService passwordValidationService, User user) {
      this.passwordValidationService = passwordValidationService;
      this.user = user;
   }

   @Override
   public void validatePassword(String password) throws IllegalArgumentException {
      passwordValidationService.isPasswordMatch(user, password);
   }

   @Override
   public void changePassword(String oldPassword, String newPassword1, String newPassword2) throws IllegalArgumentException {
      passwordValidationService.validateChangePassword(user, oldPassword, newPassword1, newPassword1);
      user.setPassword(newPassword1);
   }
}
