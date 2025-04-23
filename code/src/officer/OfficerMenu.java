package officer;

import applicant.ApplicantController;
import applicant.ApplicantMenu;
import applicant.Application;
import applicant.ApplicationStatus;
import enquiry.Enquiry;
import enquiry.EnquiryService;
import helper.Color;
import helper.TablePrinter;
import interfaces.AbstractMenu;
import project.Project;
import project.ProjectService;
import system.SessionManager;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

public class OfficerMenu extends AbstractMenu {
    private final ApplicantMenu applicantMenu;
    private final ApplicantController applicantController;
    private final TablePrinter tablePrinter;
    private final OfficerService officerService;
    private final ProjectService projectService;
    private final EnquiryService enquiryService;
    private final SessionManager sessionManager;

    public OfficerMenu(Scanner scanner, ApplicantMenu applicantMenu, ApplicantController applicantController, TablePrinter tablePrinter,
                       OfficerService officerService, ProjectService projectService, EnquiryService enquiryService, SessionManager sessionManager) {
        super(scanner);
        this.applicantMenu = applicantMenu;
        this.applicantController = applicantController;
        this.tablePrinter = tablePrinter;
        this.officerService = officerService;
        this.projectService = projectService;
        this.enquiryService = enquiryService;
        this.sessionManager = sessionManager;
    }

    @Override
    public void display() {
        Color.print("""
                    ================================ Officer Menu ==================================
                 1. Register as Officer         |     2. View Registration Status       |     3. View Details of Handled Project
                 4. View & Reply to Enquiries   |     5. Manage Flat Selection/Booking  |     6. Change Password
                ----------------------------- Applicant Capabilities ------------------------------
                 7. View Available Projects     |     8. Apply for BTO Project          |     9. View My Application Status
                 10. Request Withdrawal         |     11. Submit Project Enquiry        |     12. View My Enquiries
                 13. Edit My Enquiry            |     14. Delete My Enquiry             |     0. Logout
                =====================================================================================
                Please enter your choice:""", Color.CYAN);
    }


    @Override
    public void handleInput() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (!input.matches("[0-9]|1[0-4]")) {
                    Color.println("Error: Please enter a digit (0–14)", Color.RED);
                    continue;
                }
                switch (input) {
                    case "1" -> handleRegisterAsOfficer();
                    case "2" -> handleViewOfficerRegistrationStatus();
                    case "3" -> handleViewHandledProjectDetails();
                    case "4" -> handleViewAndReplyToEnquiries();
                    case "5" -> handleManageFlatSelection();
                    case "6" -> handleChangePassword();
                    case "7" -> applicantMenu.handleViewAvailableProjects();
                    case "8" -> handleApplyForBTOProject();
                    case "9" -> applicantMenu.handleViewApplicationStatus();
                    case "10" -> applicantMenu.handleRequestWithdrawal();
                    case "11" -> applicantMenu.handleSubmitEnquiry();
                    case "12" -> applicantMenu.handleViewMyEnquiries();
                    case "13" -> applicantMenu.handleEditEnquiry();
                    case "14" -> applicantMenu.handleDeleteEnquiry();
                    case "0" -> {
                        sessionManager.logout();
                        return;
                    }
                    default -> Color.println("Invalid option. Please try again.", Color.RED);
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                Color.println("Error: " + e.getMessage(), Color.RED);
                return;
            }
        }
    }

    private void handleApplyForBTOProject() {
        // Officer also can apply as applicant but not for their handled project
        try {
            // Fetch eligible projects
            List<Project> all = applicantController.getEligibleProjects();
            Project current = officerService.getCurrentProject();
            if (current != null) {
                all = all.stream()
                        .filter(p -> p.getId() != current.getId())
                        .collect(Collectors.toList());
            }
            if (all.isEmpty()) {
                Color.println("No eligible projects available.", Color.RED);
                return;
            }
            // Reuse applicant menu print
            Integer COLUMN_WIDTH = 15;
            List<List<String>> table = all.stream()
                    .map(Project::toPreviewTableRow)
                    .collect(Collectors.toList());
            table.add(0, List.of("Project ID", "Project Name", "Neighbourhood", "Visibility", "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price", "Opening Date", "Closing Date"));
            tablePrinter.printTable(COLUMN_WIDTH, table);

            Color.print("Enter Project ID to apply: ", Color.CYAN);
            int projectId = Integer.parseInt(scanner.nextLine().trim());
            // check exists
            boolean contains = all.stream().anyMatch(p -> p.getId() == projectId);
            if (!contains) {
                Color.println("Invalid project ID.", Color.RED);
                return;
            }
            // Delegate to applicant controller
            Color.println("Proceeding to flat type selection...", Color.CYAN);
            applicantMenu.handleApplyForBTOProject();
        } catch (Exception e) {
            Color.println("Error: " + e.getMessage(), Color.RED);
        }
    }

    private void handleRegisterAsOfficer() {
        if (officerService.getOfficerStatus() != OfficerStatus.INACTIVE) {
            Color.println("You have either already registered as an officer or your registration is pending.", Color.RED);
            return;
        }
        try {
            List<List<String>> table = officerService.getOfficerEligibleProjectsTableData();
            if (table == null) {
                Color.println("No projects found.", Color.RED);
                return;
            }
            Integer COLUMN_WIDTH = 15;
            tablePrinter.printTable(COLUMN_WIDTH, table);

            Color.print("Enter the project name or ID you want to register for (0 to exit): ", Color.GREEN);
            String projectInput = scanner.nextLine();
            if (projectInput.equals("0")) {
                Color.println("Returning to Officer Menu.", Color.RED);
                return;
            }
            var form = officerService.createAndSendRegistrationForm(projectInput.trim());
            if (form == null) return;

            Color.println("Registration Form sent successfully.", Color.CYAN);
        } catch (Exception e) {
            Color.println("Registration Error: " + e.getMessage(), Color.RED);
        }
    }


    private void handleViewOfficerRegistrationStatus() {
        OfficerStatus status = officerService.getOfficerStatus();
        Color.println("===============================================", Color.YELLOW);
        switch (status) {
            case ACTIVE -> {
                Color.println("You are currently an active officer", Color.GREEN);
                try {
                    Project p = officerService.getCurrentProject();
                    Color.println(p.toString(), Color.YELLOW);
                } catch (Exception e) {
                    Color.println("Error: " + e.getMessage(), Color.RED);
                }
            }
            case INACTIVE -> {
                Color.println("You are currently inactive", Color.RED);
                try {
                    var f = officerService.getCurrentRegistrationForm();
                    Color.println(f.toString(), Color.YELLOW);
                } catch (Exception e) {
                    Color.println("Error: " + e.getMessage(), Color.RED);
                }
            }
            case PENDING -> {
                Color.println("You are currently pending for registration", Color.YELLOW);
                try {
                    var f = officerService.getCurrentRegistrationForm();
                    Color.println(f.toString(), Color.YELLOW);
                } catch (Exception e) {
                    Color.println("Error: " + e.getMessage(), Color.RED);
                }
            }
        }
    }

    private void handleViewHandledProjectDetails() {
        Color.println("Viewing Details of Handled Project", Color.GREEN);
        try {
            Project p = officerService.getCurrentProject();
            Color.println(p.toString(), Color.YELLOW);
        } catch (Exception e) {
            Color.println("Error: " + e.getMessage(), Color.RED);
        }
    }

    private void handleViewAndReplyToEnquiries() {
        try {
            List<Enquiry> enquiries = officerService.getCurrentProjectEnquiries();
            if (enquiries.isEmpty()) {
                Color.println("No enquiries to reply.", Color.RED);
                return;
            }
            Color.println("--- Enquiries for Current Project ---", Color.YELLOW);
            for (int i = 0; i < enquiries.size(); i++) {
                Enquiry e = enquiries.get(i);
                Color.println((i + 1) + ". " + e.getApplicant().getName() + " | " + e.getDateEnquired() + " | " + e.getEnquiry() + " | Reply: " + (e.getReply() != null ? e.getReply() : "<none>"), Color.CYAN);
            }
            Color.print("Enter number to reply (0 to cancel): ", Color.CYAN);
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= enquiries.size()) {
                Color.println("Cancelled or invalid selection.", Color.YELLOW);
                return;
            }
            Enquiry sel = enquiries.get(idx);
            Color.print("Enter your reply: ", Color.CYAN);
            String reply = scanner.nextLine().trim();
            enquiryService.replyToEnquiry(sel, reply, sessionManager);
            Color.println("Reply submitted.", Color.GREEN);
        } catch (Exception e) {
            Color.println("Error: " + e.getMessage(), Color.RED);
        }
    }

    private void handleManageFlatSelection() {
        try {
            Project current = officerService.getCurrentProject();
            if (current == null) {
                Color.println("You are not assigned to any project.", Color.RED);
                return;
            }
            List<Application> apps = projectService.getApplicationsForProject(current.getId())
                    .stream().filter(a -> a.getApplicationStatus() == ApplicationStatus.SUCCESSFUL)
                    .collect(Collectors.toList());
            if (apps.isEmpty()) {
                Color.println("No approved applications to book.", Color.RED);
                return;
            }
            Color.println("--- Approved Applications ---", Color.YELLOW);
            for (int i = 0; i < apps.size(); i++) {
                Application a = apps.get(i);
                Color.println((i + 1) + ". " + a.getApplicant().getName() + " | " + a.getFlatType() + " | " + a.getBookingStatus(), Color.CYAN);
            }
            Color.print("Enter number to book flat (0 to cancel): ", Color.CYAN);
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= apps.size()) {
                Color.println("Cancelled or invalid selection.", Color.YELLOW);
                return;
            }
            Application sel = apps.get(idx);
            officerService.bookFlat(sel);
            Color.println("Flat booked and receipt generated.", Color.GREEN);
        } catch (Exception e) {
            Color.println("Error: " + e.getMessage(), Color.RED);
        }
    }

    private void handleChangePassword() {
        List<String> inputs = super.getInputsChangePassword();
        try {
            officerService.changePassword(officerService.getUser(), inputs.get(0), inputs.get(1), inputs.get(2));
            Color.println("Password changed successfully.", Color.GREEN);
        } catch (IllegalArgumentException e) {
            Color.println("Password change error: " + e.getMessage(), Color.RED);
        }
    }


    @Override
    public void run() {
        while (sessionManager.isLoggedIn()) {
            display();
            handleInput();
        }
    }
}
