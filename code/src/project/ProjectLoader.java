//package project;
//
//public class ProjectLoader {
//
//    private UserManager userManager;  // to get the manager by id
//    private OfficerManager officerManager;  // to get officers if needed
//
//    public ProjectLoader(UserManager userManager, OfficerManager officerManager) {
//        this.userManager = userManager;
//        this.officerManager = officerManager;
//    }
//
//    public Project loadProjectFromCsv(String csvLine) {
//        String[] tokens = csvLine.split(",");
//
//        // Parse basic fields
//        Integer id = Integer.parseInt(tokens[0]);
//        String name = tokens[1];
//        String neighbourhood = tokens[2];
//
//        // Parse available flats (assuming format: "flatType1:5,flatType2:10")
//        Map<FlatType, Integer> availableFlats = new HashMap<>();
//        String[] flatData = tokens[3].split(",");  // Example: "2:5,3:10"
//        for (String flat : flatData) {
//            String[] flatParts = flat.split(":");
//            FlatType flatType = FlatType.valueOf(flatParts[0]);  // assuming FlatType is an enum
//            Integer quantity = Integer.parseInt(flatParts[1]);
//            availableFlats.put(flatType, quantity);
//        }
//
//        // Parse dates
//        LocalDate applicationOpeningDate = LocalDate.parse(tokens[4]);
//        LocalDate applicationClosingDate = LocalDate.parse(tokens[5]);
//
//        // Get manager by ID (assuming manager exists in userManager)
//        Manager manager = (Manager) userManager.getUserById(tokens[6]);
//
//        // Parse other fields
//        boolean visibility = Boolean.parseBoolean(tokens[7]);
//        Integer availableOfficerSlots = Integer.parseInt(tokens[8]);
//
//        // Parse lists (assuming simple ID-based parsing for officers, etc.)
//        List<Officer> officers = officerManager.getOfficersForProject(id);  // Assuming method exists to fetch officers
//        List<RegistrationForm> registrationForms = new ArrayList<>();  // You'd load them from another source
//        List<Application> applications = new ArrayList<>();  // Same as above
//        List<Enquiry> enquiries = new ArrayList<>();  // Same as above
//
//        // Create Project instance
//        return new Project(id, name, neighbourhood, availableFlats, applicationOpeningDate,
//                applicationClosingDate, manager, visibility, availableOfficerSlots,
//                officers, registrationForms, applications, enquiries);
//    }
//}
