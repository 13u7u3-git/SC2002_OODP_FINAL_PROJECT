package project;

import java.time.LocalDate;
import java.util.List;

public interface IProjectValidationService {

   void validateProjectNameUnique(String project, List<String> existingProjects);

   void validateApplicationDates(LocalDate openingDate, LocalDate closingDate);

   void validateFlatUnitsAndPrices(int twoRoomUnits, double twoRoomPrice,
                                   int threeRoomUnits, double threeRoomPrice);

   void validateOfficerSlots(int slots);

   void validateNeighborhood(java.lang.String neighborhood);
}

