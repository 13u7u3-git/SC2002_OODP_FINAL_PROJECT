package user;

public interface IUserService {
    PasswordService passwordService = new PasswordService();

    default void changePassword(User user, String oldPassword, String newPassword1, String newPassword2) {
        passwordService.validateChangePassword(user, oldPassword, newPassword1, newPassword2);
        user.setPassword(newPassword1);
    }
}
