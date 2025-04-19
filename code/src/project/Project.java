package project;

import applicant.Application;
import enquiry.Enquiry;
import officer.RegistrationForm;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Project implements Serializable {
   @Serial
   private static final long serialVersionUID = 1L;
   // =================== Immutable Project Details =================== (never modified, at least not directly, only through the setter methods)
   private final Integer id;//automatically generated
   private final String manager;
   private final Map<FlatType, Double> flatPrices;
   private final Map<FlatType, Integer> availableFlats;
   private final Map<FlatType, Integer> remainingFlats;//Officer can modify
   private final List<String> officers;
   private final List<RegistrationForm> registrationForms = new ArrayList<>();
   private final List<Application> applications = new ArrayList<>();
   private final List<Enquiry> enquiries = new ArrayList<>();

   // =================== Mutable Project Metadata ===================
   private String projectName;
   private String neighbourhood;
   private LocalDate applicationOpeningDate;
   private LocalDate applicationClosingDate;
   private boolean visibility = false;
   private Integer availableOfficerSlots;

   // Use this when Manager wants to create a new project
   public Project(int id, String projectName, String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                  Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                  LocalDate applicationClosingDate, String manager, Integer availableOfficerSlots,
                  List<String> officers) {
      this.id = id;
      this.projectName = projectName;
      this.neighbourhood = neighbourhood;
      this.flatPrices = Map.of(FlatType.TWO_ROOM, twoRoomPrice, FlatType.THREE_ROOM, threeRoomPrice);
      this.availableFlats = Map.of(FlatType.TWO_ROOM, twoRoomUnits, FlatType.THREE_ROOM, threeRoomUnits);
      this.remainingFlats = Map.of(FlatType.TWO_ROOM, twoRoomUnits, FlatType.THREE_ROOM, threeRoomUnits);
      this.applicationOpeningDate = applicationOpeningDate;
      this.applicationClosingDate = applicationClosingDate;
      this.manager = manager;
      this.availableOfficerSlots = availableOfficerSlots;
      this.officers = officers;
   }
   // =================== Immutable Project Details ===================

   public Integer getId() {
      return id;
   }

   public String getManager() {
      return manager;
   }

   public Map<FlatType, Double> getFlatPrices() {
      return flatPrices;
   }

   public Map<FlatType, Integer> getAvailableFlats() {
      return availableFlats;
   }

   public Map<FlatType, Integer> getRemainingFlats() {
      return remainingFlats;
   }

   public List<String> getOfficers() {
      return officers;
   }

   public List<RegistrationForm> getRegistrationForms() {
      return registrationForms;
   }

   public List<Application> getApplications() {
      return applications;
   }

   public List<Enquiry> getEnquiries() {
      return enquiries;
   }

   public String getProjectName() {
      return projectName;
   }

   public void setProjectName(String projectName) {
      this.projectName = projectName;
   }

   public String getNeighbourhood() {
      return neighbourhood;
   }

   public void setNeighbourhood(String neighbourhood) {
      this.neighbourhood = neighbourhood;
   }

   public LocalDate getApplicationOpeningDate() {
      return applicationOpeningDate;
   }

   public void setApplicationOpeningDate(LocalDate applicationOpeningDate) {
      this.applicationOpeningDate = applicationOpeningDate;
   }

   public LocalDate getApplicationClosingDate() {
      return applicationClosingDate;
   }

   public void setApplicationClosingDate(LocalDate applicationClosingDate) {
      this.applicationClosingDate = applicationClosingDate;
   }

   public boolean isVisibility() {
      return visibility;
   }

   public void setVisibility(boolean visibility) {
      this.visibility = visibility;
   }

   public Integer getAvailableOfficerSlots() {
      return availableOfficerSlots;
   }

   public void setAvailableOfficerSlots(Integer availableOfficerSlots) {
      this.availableOfficerSlots = availableOfficerSlots;
   }
// =================== Mutable Project Metadata ===================
}


