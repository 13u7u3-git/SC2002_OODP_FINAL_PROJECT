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

public class String implements Serializable {
   @Serial
   private static final long serialVersionUID = 1L;
   // =================== Immutable Project Details =================== (never modified, at least not directly, only through the setter methods)
   private final Integer id;//automatically generated
   private final java.lang.String manager;
   private final Map<FlatType, Double> flatPrices;
   private final Map<FlatType, Integer> availableFlats;
   private final Map<FlatType, Integer> remainingFlats;//Officer can modify
   private final List<java.lang.String> officers;
   private final List<RegistrationForm> registrationForms = new ArrayList<>();
   private final List<Application> applications = new ArrayList<>();
   private final List<Enquiry> enquiries = new ArrayList<>();

   // =================== Mutable Project Metadata ===================
   private java.lang.String projectName;
   private java.lang.String neighborhood;
   private LocalDate applicationOpeningDate;
   private LocalDate applicationClosingDate;
   private boolean visibility = false;
   private Integer availableOfficerSlots;

   // Use this when Manager wants to create a new project
   public String(int id, java.lang.String projectName, java.lang.String neighborhood, Integer twoRoomUnits, Double twoRoomPrice,
                 Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                 LocalDate applicationClosingDate, java.lang.String manager, Integer availableOfficerSlots,
                 List<java.lang.String> officers) {
      this.id = id;
      this.projectName = projectName;
      this.neighborhood = neighborhood;
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

   public java.lang.String getManager() {
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

   public List<java.lang.String> getOfficers() {
      return officers;
   }

   public List<RegistrationForm> getRegistrationForms() {
      return registrationForms;
   }

   public void addRegistrationForm(RegistrationForm registrationForm) {
      registrationForms.add(registrationForm);
   }

   public void removeRegistrationForm(RegistrationForm registrationForm) {
      registrationForms.remove(registrationForm);
   }

   public List<Application> getApplications() {
      return applications;
   }

   public List<Enquiry> getEnquiries() {
      return enquiries;
   }

   public java.lang.String getProjectName() {
      return projectName;
   }

   public void setProjectName(java.lang.String projectName) {
      this.projectName = projectName;
   }

   public java.lang.String getNeighborhood() {
      return neighborhood;
   }

   public void setNeighborhood(java.lang.String neighborhood) {
      this.neighborhood = neighborhood;
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

   public Integer getTwoRoomUnits() {
      return availableFlats.get(FlatType.TWO_ROOM);
   }

   public Double getTwoRoomPrice() {
      return flatPrices.get(FlatType.TWO_ROOM);
   }

   public Integer getThreeRoomUnits() {
      return availableFlats.get(FlatType.THREE_ROOM);
   }

   public Double getThreeRoomPrice() {
      return flatPrices.get(FlatType.THREE_ROOM);
   }


   // =================== Mutable Project Metadata ===================
   public void addApplication(Application application) {
      applications.add(application);
   }

   public void removeApplication(Application application) {
      applications.remove(application);
   }

   public void addEnquiry(Enquiry enquiry) {
      enquiries.add(enquiry);
   }

   public void removeEnquiry(Enquiry enquiry) {
      enquiries.remove(enquiry);
   }


// =================== Printable String Project Metadata ===================

   //"Project ID", "Project Name", "Neighbourhood", "Visibility", "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price", "Appln..Opening Date", "Appln..Closing Date", "Manager", "Officer Slots", "Officers"
   public List<java.lang.String> toStringAsList() {
      return List.of(id.toString(), projectName, neighborhood, visibility ? "Visible" : "Hidden", getTwoRoomUnits().toString(), getTwoRoomPrice().toString(), getThreeRoomUnits().toString(), getThreeRoomPrice().toString(), applicationOpeningDate.toString(), applicationClosingDate.toString(), manager, availableOfficerSlots.toString(), officers.toString());
   }

   @Override // to print as a form like
   public java.lang.String toString() {
      return "============== Project Details ==============\n" +
              "Project ID          : " + id + "\n" +
              "Project Name        : " + projectName + "\n" +
              "Neighbourhood       : " + neighborhood + "\n" +
              "Visibility          : " + (visibility ? "Visible" : "Hidden") + "\n" +
              "Two Room Units      : " + getTwoRoomUnits() + "\n" +
              "Two Room Price      : " + getTwoRoomPrice() + "\n" +
              "Three Room Units    : " + getThreeRoomUnits() + "\n" +
              "Three Room Price    : " + getThreeRoomPrice() + "\n" +
              "Opening Date        : " + applicationOpeningDate + "\n" +
              "Closing Date        : " + applicationClosingDate + "\n" +
              "Manager             : " + manager + "\n" +
              "Officer Slots       : " + availableOfficerSlots + "\n" +
              "Officers            : " + officers + "\n" +
              "==============================================";
   }


}


