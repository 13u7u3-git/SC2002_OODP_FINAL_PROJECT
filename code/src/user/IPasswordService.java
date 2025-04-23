package user;

public interface IPasswordService {
    void isPasswordMatch(User user, String password);

    void validateChangePassword(User user, String old, String newPassword1, String newPassword2);
}

