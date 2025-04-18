package applicant;

import enquiry.Enquiry;
import enums.FlatType;
import project.Project;

import java.util.List;
import java.util.Scanner;

public class ApplicantMenu {
    private final ApplicantController controller;
    private final Scanner scanner;

    public ApplicantMenu(ApplicantController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    /** Starts the main menu loop. */
    public void start() {
        while (true) {
            System.out.println("Welcome, " + controller.getApplicantName() + "!");
            System.out.println("1. View eligible projects");
            System.out.println("2. Apply for a project");
            System.out.println("3. View current application");
            System.out.println("4. View all applications");
            System.out.println("5. Book a flat");
            System.out.println("6. Request withdrawal");
            System.out.println("7. Manage enquiries");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewEligibleProjects();
                    break;
                case 2:
                    applyForProject();
                    break;
                case 3:
                    viewCurrentApplication();
                    break;
                case 4:
                    viewAllApplications();
                    break;
                case 5:
                    bookFlat();
                    break;
                case 6:
                    requestWithdrawal();
                    break;
                case 7:
                    manageEnquiries();
                    break;
                case 8:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /** Displays a list of eligible projects. */
    private void viewEligibleProjects() {
        List<Project> projects = controller.getEligibleProjects();
        if (projects.isEmpty()) {
            System.out.println("You are not eligible for any projects at the moment.");
        } else {
            System.out.println("Eligible Projects:");
            for (int i = 0; i < projects.size(); i++) {
                Project p = projects.get(i);
                System.out.println((i + 1) + ". ID: " + p.getId() + ", Name: " + p.getName());
            }
        }
    }

    /** Handles the application process for a project. */
    private void applyForProject() {
        List<Project> projects = controller.getEligibleProjects();
        Project selected = selectProject(projects);
        if (selected == null) return;
        FlatType flatType = selectFlatType();
        if (flatType == null) return;
        controller.applyForProject(selected, flatType);
        System.out.println("Application submitted.");
    }

    /** Displays the current application. */
    private void viewCurrentApplication() {
        Application app = controller.getCurrentApplication();
        if (app == null) {
            System.out.println("No current application found.");
        } else {
            System.out.println("Current Application: " + app.toString());
        }
    }

    /** Displays all applications. */
    private void viewAllApplications() {
        List<Application> applications = controller.getAllApplications();
        if (applications.isEmpty()) {
            System.out.println("No applications found.");
        } else {
            System.out.println("All Applications:");
            for (int i = 0; i < applications.size(); i++) {
                System.out.println((i + 1) + ". " + applications.get(i).toString());
            }
        }
    }

    /** Handles booking a flat based on an application. */
    private void bookFlat() {
        Application selected = selectApplication();
        if (selected != null) {
            controller.bookFlat(selected);
            System.out.println("Booking request sent.");
        }
    }

    /** Handles requesting withdrawal of an application. */
    private void requestWithdrawal() {
        Application selected = selectApplication();
        if (selected != null) {
            controller.requestWithdrawal(selected);
            System.out.println("Withdrawal request sent.");
        }
    }

    /** Manages enquiries with a sub-menu. */
    private void manageEnquiries() {
        while (true) {
            System.out.println("Manage Enquiries:");
            System.out.println("1. Create enquiry");
            System.out.println("2. View enquiries");
            System.out.println("3. Edit enquiry");
            System.out.println("4. Delete enquiry");
            System.out.println("5. Back to main menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    createEnquiry();
                    break;
                case 2:
                    viewEnquiries();
                    break;
                case 3:
                    editEnquiry();
                    break;
                case 4:
                    deleteEnquiry();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /** Creates a new enquiry. */
    private void createEnquiry() {
        List<Project> projects = controller.getProjectsForEnquiry();
        Project selected = selectProject(projects);
        if (selected == null) return;
        System.out.print("Enter enquiry message: ");
        String message = scanner.nextLine();
        controller.createEnquiry(selected, message);
        System.out.println("Enquiry created.");
    }

    /** Displays all enquiries. */
    private void viewEnquiries() {
        List<Enquiry> enquiries = controller.getEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
        } else {
            System.out.println("Enquiries:");
            for (int i = 0; i < enquiries.size(); i++) {
                System.out.println((i + 1) + ". " + enquiries.get(i).toString());
            }
        }
    }

    /** Edits an existing enquiry. */
    private void editEnquiry() {
        List<Enquiry> enquiries = controller.getEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries available to edit.");
            return;
        }
        Enquiry selected = selectEnquiry(enquiries);
        if (selected == null) return;
        System.out.print("Enter new enquiry message: ");
        String newMessage = scanner.nextLine();
        controller.editEnquiry(selected, newMessage);
        System.out.println("Enquiry updated.");
    }

    /** Deletes an existing enquiry. */
    private void deleteEnquiry() {
        List<Enquiry> enquiries = controller.getEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries available to delete.");
            return;
        }
        Enquiry selected = selectEnquiry(enquiries);
        if (selected == null) return;
        controller.deleteEnquiry(selected);
        System.out.println("Enquiry deleted.");
    }

    /** Utility method to select a project from a list. */
    private Project selectProject(List<Project> projects) {
        if (projects.isEmpty()) {
            System.out.println("No projects available.");
            return null;
        }
        viewEligibleProjects();
        System.out.print("Select a project by number: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice < 1 || choice > projects.size()) {
            System.out.println("Invalid selection.");
            return null;
        }
        return projects.get(choice - 1);
    }

    /** Utility method to select a flat type. */
    private FlatType selectFlatType() {
        System.out.println("Select flat type:");
        FlatType[] types = FlatType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Enter the number: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice < 1 || choice > types.length) {
            System.out.println("Invalid selection.");
            return null;
        }
        return types[choice - 1];
    }

    /** Utility method to select an application from a list. */
    private Application selectApplication() {
        List<Application> applications = controller.getAllApplications();
        if (applications.isEmpty()) {
            System.out.println("No applications available.");
            return null;
        }
        viewAllApplications();
        System.out.print("Select an application by number: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice < 1 || choice > applications.size()) {
            System.out.println("Invalid selection.");
            return null;
        }
        return applications.get(choice - 1);
    }

    /** Utility method to select an enquiry from a list. */
    private Enquiry selectEnquiry(List<Enquiry> enquiries) {
        viewEnquiries();
        System.out.print("Select an enquiry by number: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice < 1 || choice > enquiries.size()) {
            System.out.println("Invalid selection.");
            return null;
        }
        return enquiries.get(choice - 1);
    }
}