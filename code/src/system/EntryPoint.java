package system;

import UniqueID.IUniqueIdService;
import UniqueID.UniqueId;
import UniqueID.UniqueIdService;
import applicant.Applicant;
import helper.Color;
import interfaces.Menu;
import manager.*;
import officer.Officer;
import project.ProjectRegistry;
import project.ProjectService;
import user.UserRegistry;

import java.util.Scanner;

public class EntryPoint {
   public void start() throws Exception {
      boolean running = true; // Flag to control the main loop
// Initialize services with proper dependencies
      UniqueId uniqueId = new UniqueId();
      uniqueId.loadFromPropertiesFile();
      IUniqueIdService uniqueIdService = new UniqueIdService(uniqueId);

      // Initialize system components with proper dependency injection
      Scanner scanner = new Scanner(System.in);

      // Initialize UserRegistry and ProjectRegistry from Text Files (Only once)
      /*UserRegistryLoadingController userRegistryLoadingController = new UserRegistryLoadingController();
      UserRegistry userRegistry = userRegistryLoadingController.initializeUserRegistry();
      ProjectRegistryLoadingController projectRegistryLoadingController = new ProjectRegistryLoadingController(uniqueIdService);
      ProjectRegistry projectRegistry = projectRegistryLoadingController.initializeProjectRegistry();*/

      // Load UserRegistry and ProjectRegistry from dat
      UserRegistry userRegistry = UserRegistry.load();
      ProjectRegistry projectRegistry = ProjectRegistry.load();

      //clear mememory
//      UserRegistry userRegistry = new UserRegistry();
//      userRegistry.clearFromFile();
//      ProjectRegistry projectRegistry = new ProjectRegistry();
//      projectRegistry.save();
//      exit(0);//

      AuthenticationService authService = new AuthenticationService(userRegistry);
      SessionManager sessionManager = new SessionManager(authService);
      ProjectService projectService = new ProjectService(projectRegistry, uniqueIdService);


      // Create login menu
      LoginMenu loginMenu = new LoginMenu(sessionManager, scanner);
      Color.println("Welcome to the BTO System!", Color.CYAN);

      while (running) {
         // Get current menu based on user state
         Menu currentMenu = null;

         if (sessionManager.getCurrentUser() == null) {
            // User is not logged in, show login menu
            currentMenu = loginMenu;
         }
         else {

            Object user = sessionManager.getCurrentUser();

            if (user instanceof Manager) {
               IManagerService managerService = new ManagerService(projectService, (Manager) user);
               ManagerController managerController = new ManagerController(managerService, projectService);
               currentMenu = new ManagerMenu(scanner, sessionManager, managerController);
            }
            else if (user instanceof Officer) {
               // currentMenu = new OfficerMenu(scanner, sessionManager, officerController);
            }
            else if (user instanceof Applicant) {
               // currentMenu = new ApplicantMenu(scanner, sessionManager, applicantController);
            }
            else {
               Color.println("Unsupported user type. Logging out.", Color.RED);
               sessionManager.logout();
               currentMenu = loginMenu;
            }

         }

         // Display and handle the current menu
         currentMenu.display();
         currentMenu.handleInput();

         // Check if user wants to exit
         if (!running) {
            Color.println("\nEnter 'exit' to quit or any key to continue:", Color.BLUE);
            String input = scanner.nextLine();
            if ("exit".equalsIgnoreCase(input)) {
               running = false;
            }
         }
      }

      Color.println("Thank you for using the BTO Housing Application System. Goodbye!", Color.CYAN);
      scanner.close();
   }
}
