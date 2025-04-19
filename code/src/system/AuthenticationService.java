package system;

import user.User;
import user.UserRegistry;

/**
 * Service class for handling user authentication.
 * Separates authentication logic from session management.
 */
public class AuthenticationService {
   private final UserRegistry userRegistry;

   public AuthenticationService(UserRegistry userRegistry) {
      this.userRegistry = userRegistry;
   }

   /**
    * Authenticates a user based on credentials.
    *
    * @param nric     The user's NRIC (National Registration Identity Card) number
    * @param password The user's password
    * @return The authenticated User object if successful, null otherwise
    */
   public User authenticate(String nric, String password) {
      User user = userRegistry.getUserByNric(nric);
      if (user != null && user.getPassword().equals(password)) {
         return user;
      }
      return null;
   }
}