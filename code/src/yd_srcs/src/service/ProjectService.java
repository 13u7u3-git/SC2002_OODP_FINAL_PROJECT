package service;

import enums.FlatType;
import model.project.Project;
import util.UserFilterSettings;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private ProjectService() {}

    private static class Holder {
        private static final ProjectService INSTANCE = new ProjectService();
    }

    public static ProjectService getInstance() {
        return ProjectService.Holder.INSTANCE;
    }

    /**
     * Returns the list of projects filtered by the provided user filter settings.
     */
    public List<Project> getProjectsByFilter(List<Project> allProjects, UserFilterSettings filterSettings) {
        return allProjects.stream()
                .filter(project -> {
                    boolean matches = true;
                    if (filterSettings.getName() != null) {
                        matches &= project.getName().toLowerCase().contains(filterSettings.getName().toLowerCase());
                    }
                    if (filterSettings.getNeighbourhood() != null) {
                        matches &= project.getNeighbourhood().equalsIgnoreCase(filterSettings.getNeighbourhood());
                    }
                    if (filterSettings.getFlatType() != null) {
                        // Project must have available flats for the given flat type.
                        matches &= project.getAvailableFlats().getOrDefault(filterSettings.getFlatType(), 0) > 0;
                    }
                    if (filterSettings.getApplicationOpeningDate() != null) {
                        matches &= !project.getApplicationOpeningDate().isBefore(filterSettings.getApplicationOpeningDate());
                    }
                    if (filterSettings.getApplicationClosingDate() != null) {
                        matches &= !project.getApplicationClosingDate().isAfter(filterSettings.getApplicationClosingDate());
                    }
                    return matches;
                })
                .sorted(Comparator.comparing(Project::getName))
                .collect(Collectors.toList());
    }

    /**
     * Returns the details of a project.
     */
    public Project viewProjectDetails(Project project) {
        // In a CLI, you might simply print the details.
        return project;
    }
}
