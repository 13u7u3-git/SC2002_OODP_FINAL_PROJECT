package project;

import java.util.ArrayList;
import java.util.List;

public class ProjectRegistry {
    private static final List<Project> projects = new ArrayList<>();

    public static List<Project> getProjects() {
        return projects;
    }
    public static void addProject(Project project) {
        projects.add(project);
    }

    public static void removeProject(Project project) {
        projects.remove(project);
    }

    public static void setProjects(List<Project> projects) {
        for (Project project : projects) {
            addProject(project);
        }
    }
}
