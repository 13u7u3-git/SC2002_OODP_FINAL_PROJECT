package project;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * public class ProjectRegistry {
 *     private final List<Project> projects = new ArrayList<>();
 *
 *     public List<Project> getProjects() {
 *         return new ArrayList<>(projects); // return a copy if you want immutability
 *     }
 *
 *     public void addProject(Project project) {
 *         projects.add(project);
 *     }
 *
 *     public void removeProject(Project project) {
 *         projects.remove(project);
 *     }
 *
 *     public void setProjects(List<Project> projects) {
 *         this.projects.clear();
 *         this.projects.addAll(projects);
 *     }
 * }
 *
 * 2. Inject it where needed
 * Let’s say we have a MainApplication class or a ProjectManager class:
 *
 *
 * public class ProjectManager {
 *     private final ProjectRegistry projectRegistry;
 *
 *     public ProjectManager(ProjectRegistry projectRegistry) {
 *         this.projectRegistry = projectRegistry;
 *     }
 *
 *     public void createSampleProject() {
 *         Project p = new Project("HDB Tengah");
 *         projectRegistry.addProject(p);
 *     }
 * }
 *
 *
 * Now you can inject ProjectRegistry from outside, like so:
 *
 * public class MainApplication {
 *     public static void main(String[] args) {
 *         ProjectRegistry registry = new ProjectRegistry();
 *         ProjectManager manager = new ProjectManager(registry);
 *
 *         manager.createSampleProject();
 *         System.out.println(registry.getProjects());
 *     }
 * }
 *
 *
 */


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

