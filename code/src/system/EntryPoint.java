package system;

import UniqueID.IUniqueIdService;
import UniqueID.UniqueId;
import UniqueID.UniqueIdService;
import applicant.Applicant;
import helper.Color;
import helper.ProjectRegistryLoader.ProjectRegistryLoadingController;
import helper.ProjectRegistryLoader.ProjectStaffLinker;
import helper.UserRegistryLoader.UserRegistryLoadingController;
import interfaces.Menu;
import manager.*;
import officer.*;
import project.IProjectService;
import project.ProjectRegistry;
import project.ProjectService;
import user.*;

import java.util.Scanner;

public class EntryPoint {
   public void start() throws Exception {
      boolean running = true; // Flag to control the main loop
// Initialize services with proper dependencies
      UniqueId uniqueId = new UniqueId();
      uniqueId.loadFromPropertiesFile();
      IUniqueIdService uniqueIdService = new UniqueIdService(uniqueId);
      IPasswordValidationService passwordValidationService = new PasswordValidationService();
      // Initialize system components with proper dependency injection
      Scanner scanner = new Scanner(System.in);

      // Initialize UserRegistry and ProjectRegistry from Text Files (Only once)
      UserRegistryLoadingController userRegistryLoadingController = new UserRegistryLoadingController();
      UserRegistry userRegistry = userRegistryLoadingController.initializeUserRegistry();
      ProjectRegistryLoadingController projectRegistryLoadingController = new ProjectRegistryLoadingController(uniqueIdService);
      ProjectRegistry projectRegistry = projectRegistryLoadingController.initializeProjectRegistry();
      ProjectStaffLinker projectStaffLinker = new ProjectStaffLinker(projectRegistry, userRegistry);
      projectStaffLinker.linkProjectToOfficer();
      projectStaffLinker.linkProjectToManager();
      //exit(0);

      // Load UserRegistry and ProjectRegistry from dat
//      UserRegistry userRegistry = UserRegistry.load();
//      ProjectRegistry projectRegistry = ProjectRegistry.load();

      //clear mememory
      /*UserRegistry userRegistry = new UserRegistry();
      userRegistry.clearFromFile();
      ProjectRegistry projectRegistry = new ProjectRegistry();
      projectRegistry.save();
      exit(0);*/

      AuthenticationService authService = new AuthenticationService(userRegistry); // TODO: Pass the validation service
      SessionManager sessionManager = new SessionManager(authService);
      IProjectService projectService = new ProjectService(projectRegistry, uniqueIdService);


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

            IUserService userService = new UserService(passwordValidationService, (User) user);

            if (user instanceof Manager) {
               IManagerService managerService = new ManagerService(projectService, (Manager) user);
               ManagerController managerController = new ManagerController(managerService, projectService);// TODO: Pass the validation service
               currentMenu = new ManagerMenu(scanner, sessionManager, managerController);
            }
            else if (user instanceof Officer) {
               IOfficerService officerService = new OfficerService(projectService, uniqueIdService, (Officer) user, userService);
               OfficerController officerController = new OfficerController(officerService, projectService);
               currentMenu = new OfficerMenu(scanner, sessionManager, officerController);
            }
            else if (user instanceof Applicant) {
               // currentMenu = new ApplicantMenu(scanner, sessionManager, applicantController); // TODO: Pass the validation service
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
         if (sessionManager.getCurrentUser() == null) {
            Color.print("\n\nEnter 'exit' to quit or any key to continue:", Color.BLUE);
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

