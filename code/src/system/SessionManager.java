package system;

import user.User;

public class SessionManager {
   private final AuthenticationService authService;
   private User currentUser = null;

   public SessionManager(AuthenticationService authService) {
      this.authService = authService;
   }

   public boolean login(String nric, String password) {
      User authenticatedUser = authService.authenticate(nric, password);
      if (authenticatedUser != null) {
         this.currentUser = authenticatedUser;
         return true;
      }
      return false;
   }

   public User getCurrentUser() {
      return currentUser;
   }

   public void logout() {
      this.currentUser = null;
   }
}