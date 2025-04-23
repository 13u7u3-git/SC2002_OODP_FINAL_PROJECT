package project;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ProjectValidationService {

    private static final Set<String> VALID_NEIGHBORHOODS = Set.of(
            "Yishun", "Boon Lay", "Tampines", "Jurong West", "Bedok"
    );

    public void validateProjectNameUnique(Project project, List<Project> allProjects) {
        boolean exists = allProjects.stream()
                .anyMatch(p -> p.getProjectName().equalsIgnoreCase(project.getProjectName())
                        && !p.getId().equals(project.getId()));
        if (exists) {
            throw new IllegalArgumentException("Project name must be unique.");
        }
    }

    public void validateApplicationDates(LocalDate openingDate, LocalDate closingDate) {
        if (openingDate == null || closingDate == null) {
            throw new IllegalArgumentException("Application dates must not be null.");
        }
        if (openingDate.isAfter(closingDate)) {
            throw new IllegalArgumentException("Opening date must be before closing date.");
        }
    }

    public void validateNeighborhood(String neighborhood) {
        if (neighborhood == null || neighborhood.isBlank()) {
            throw new IllegalArgumentException("Neighborhood must not be empty.");
        }
        if (!VALID_NEIGHBORHOODS.contains(neighborhood)) {
            throw new IllegalArgumentException("Invalid neighborhood. Valid options are: " + VALID_NEIGHBORHOODS);
        }
    }

    public void validateFlatUnitsAndPrices(Integer twoRoomUnits, Double twoRoomPrice,
                                           Integer threeRoomUnits, Double threeRoomPrice) {
        if (twoRoomUnits == null || twoRoomUnits < 0) {
            throw new IllegalArgumentException("2-room units must be non-negative.");
        }
        if (twoRoomPrice == null || twoRoomPrice < 0) {
            throw new IllegalArgumentException("2-room price must be non-negative.");
        }
        if (threeRoomUnits == null || threeRoomUnits < 0) {
            throw new IllegalArgumentException("3-room units must be non-negative.");
        }
        if (threeRoomPrice == null || threeRoomPrice < 0) {
            throw new IllegalArgumentException("3-room price must be non-negative.");
        }
    }

    public void validateOfficerSlots(Integer availableOfficerSlots) {
        if (availableOfficerSlots == null || availableOfficerSlots < 1) {
            throw new IllegalArgumentException("Available officer slots must be at least 1.");
        }
    }
} 
