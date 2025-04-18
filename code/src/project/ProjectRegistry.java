package project;

import java.util.Collections;
import java.util.List;

public class ProjectRegistry {
    private final List<Project> projects;

    // We will always use this because we will always be loading projects from csv.
    public ProjectRegistry(List<Project> projects) {
        this.projects = projects;
    }

    // Only for other service classes to view and filter, cannot make any changes to the original list.
    // Cannot modify the original list structure (add/remove projects).
    // Please use add or remove methods explicitly if manager wants to add or remove projects.
    public List<Project> getProjects() {
        return Collections.unmodifiableList(projects);
    }
    public void addProject(Project project) {
        projects.add(project);
    }

    public void removeProject(Project project) {
        projects.remove(project);
    }
}
