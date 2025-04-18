//package service;
//
//import enums.UserType;
//import model.enquiry.Enquiry;
//import model.enquiry.Reply;
//import model.project.Project;
//import model.project.ProjectRegistry;
//import model.user.Applicant;
//import model.user.Manager;
//import model.user.Officer;
//import model.user.User;
//import util.UniqueId;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//public class EnquiryService {
//    private EnquiryService() {}
//
//    private static class Holder {
//        private static final EnquiryService INSTANCE = new EnquiryService();
//    }
//
//    public static EnquiryService getInstance() {
//        return EnquiryService.Holder.INSTANCE;
//    }
//
//    /**
//     * Submits an enquiry regarding a project.
//     */
//    public Enquiry submitEnquiry(Applicant applicant, Project project, String enquiryText) {
//        UniqueId idGen = new UniqueId();
//        int enquiryId = idGen.getNextEnquiryId();
//        Enquiry enquiry = new Enquiry(enquiryId, project, applicant, enquiryText, LocalDate.now());
//        // Add enquiry to both the applicant and the project.
//        applicant.addEnquiry(enquiry);
//        project.addEnquiry(enquiry);
//        System.out.println("Enquiry submitted.");
//        return enquiry;
//    }
//
//    /**
//     * Edits an existing enquiry (by enquiry ID) with new content.
//     */
//    public boolean editEnquiry(Applicant applicant, int enquiryId, String newContent) {
//        for (Enquiry enquiry : applicant.getEnquiryList()) {
//            if (enquiry.getId() == enquiryId) {
//                enquiry.setEnquiry(newContent);
//                System.out.println("Enquiry updated.");
//                return true;
//            }
//        }
//        System.out.println("Enquiry not found.");
//        return false;
//    }
//
//    /**
//     * Deletes an enquiry by its ID.
//     */
//    public boolean deleteEnquiry(Applicant applicant, int enquiryId) {
//        boolean removed = applicant.getEnquiryList().removeIf(e -> e.getId() == enquiryId);
//        if (removed) {
//            System.out.println("Enquiry deleted.");
//        } else {
//            System.out.println("Enquiry not found.");
//        }
//        return removed;
//    }
//
//    /**
//     * Adds a reply to a given enquiry.
//     */
//    public void replyToEnquiry(Enquiry enquiry, User respondent, String replyMessage) {
//        UniqueId idGen = new UniqueId();
//        int replyId = idGen.getNextReplyId();
//        Reply reply = new Reply(replyId, replyMessage, respondent, LocalDate.now());
//        enquiry.addReply(reply);
//        System.out.println("Reply added to enquiry.");
//    }
//
//    /**
//     * Returns the list of enquiries by project for all users.
//     */
//    public List<Enquiry> getEnquiriesByProject(User user) {
//        UserType userType = user.getUserType();
//        switch(userType) {
//            case MANAGER -> {
//                Manager manager = (Manager) user;
//                return manager.getAssignedProjectList().stream()
//                        .flatMap(project -> project.getEnquiryList().stream())
//                        .toList();
//            }
//            case OFFICER -> {
//                Officer officer = (Officer) user;
//                return officer.getAssignedProjectList().stream()
//                        .flatMap(project -> project.getEnquiryList().stream())
//                        .toList();
//            }
//            case APPLICANT -> {
//                Applicant applicant = (Applicant) user;
//                return applicant.getEnquiryList();
//            }
//            default -> {
//                return new ArrayList<>();
//            }
//        }
//    }
//
//    /**
//     * Returns all the enquiries for the manager to view.
//     */
//    public List<Enquiry> getAllEnquiries(ProjectRegistry projectRegistry) {
//        return projectRegistry.getProjectList().stream()
//                .flatMap(project -> project.getEnquiryList().stream())
//                .toList();
//    }
//}
