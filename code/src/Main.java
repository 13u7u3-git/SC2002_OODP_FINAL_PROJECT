//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import applicant.Applicant;
import applicant.ApplicantController;
import applicant.ApplicantMenu;
import applicant.ApplicantService;
import enquiry.EnquiryService;
import enums.FlatType;
import enums.MaritalStatus;
import enums.UserType;
import helper.UniqueId;
import project.Project;
import project.ProjectRegistry;
import project.ProjectService;
import user.UserFilterSettings;

import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Initialize UniqueId for generating unique identifiers
        UniqueId uniqueId = new UniqueId();

        // Create a list to hold projects
        List<Project> projects = new ArrayList<>();

        // Project 1: Includes TWO_ROOM flats, open now
        Map<FlatType, Integer> availableFlats1 = new HashMap<>();
        availableFlats1.put(FlatType.TWO_ROOM, 5);
        availableFlats1.put(FlatType.THREE_ROOM, 3);
        projects.add(new Project(1, "Sunrise Gardens", "Tampines", availableFlats1,
                LocalDate.now().minusDays(15), LocalDate.now().plusDays(15), null, true));

        // Project 2: Includes TWO_ROOM and FOUR_ROOM, open now
        Map<FlatType, Integer> availableFlats2 = new HashMap<>();
        availableFlats2.put(FlatType.TWO_ROOM, 2);
        availableFlats2.put(FlatType.THREE_ROOM, 1);
        projects.add(new Project(2, "Riverfront Residences", "Hougang", availableFlats2,
                LocalDate.now().minusDays(10), LocalDate.now().plusDays(20), null, true));

        // Project 3: Includes THREE_ROOM and FIVE_ROOM, open now
        Map<FlatType, Integer> availableFlats3 = new HashMap<>();
        availableFlats3.put(FlatType.THREE_ROOM, 4);
        availableFlats3.put(FlatType.TWO_ROOM, 2);
        projects.add(new Project(3, "Parkview Estates", "Jurong", availableFlats3,
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(10), null, true));

        // Project 4: Includes TWO_ROOM, recently opened
        Map<FlatType, Integer> availableFlats4 = new HashMap<>();
        availableFlats4.put(FlatType.TWO_ROOM, 3);
        availableFlats4.put(FlatType.THREE_ROOM, 2);
        projects.add(new Project(4, "Coastal Breeze", "Punggol", availableFlats4,
                LocalDate.now().minusDays(2), LocalDate.now().plusDays(30), null, true));

        // Project 5: Includes FOUR_ROOM and FIVE_ROOM, open now
        Map<FlatType, Integer> availableFlats5 = new HashMap<>();
        availableFlats5.put(FlatType.TWO_ROOM, 3);
        availableFlats5.put(FlatType.THREE_ROOM, 1);
        projects.add(new Project(5, "City Heights", "Bishan", availableFlats5,
                LocalDate.now().minusDays(20), LocalDate.now().plusDays(5), null, true));

        // Initialize ProjectRegistry with the projects
        ProjectRegistry projectRegistry = new ProjectRegistry(projects);

        // Initialize core services
        ProjectService projectService = new ProjectService();
        ApplicantService applicantService = new ApplicantService();
        EnquiryService enquiryService = new EnquiryService(projectService);

        // Create a sample applicant (age 30, single, eligible for TWO_ROOM flats)
        UserFilterSettings filterSettings = new UserFilterSettings();
        Applicant applicant = new Applicant("John Doe", "S1234567A", "password", 30,
                MaritalStatus.MARRIED, filterSettings, UserType.APPLICANT,
                new ArrayList<>(), null, new ArrayList<>());

        // Set up the controller with services and applicant
        ApplicantController controller = new ApplicantController(applicantService, enquiryService,
                projectService, projectRegistry, uniqueId, applicant);

        // Initialize and start the menu
        ApplicantMenu menu = new ApplicantMenu(controller);
        menu.start();
    }
}