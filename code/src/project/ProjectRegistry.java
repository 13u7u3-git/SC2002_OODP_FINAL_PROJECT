package project;

import interfaces.Filterable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// This class is used to store all projects in a list
public class ProjectRegistry implements Serializable, Filterable<Project> {
   @Serial
   private static final long serialVersionUID = 1L;
   private static final String filePath = "./data/projectRegistry.dat";
   private final List<Project> projects;

   public ProjectRegistry() {
      this.projects = new ArrayList<>();
   }

   // We will always use this because we will always be loading projects from csv.
   public ProjectRegistry(List<Project> projects) {
      this.projects = projects;
   }

   public ProjectRegistry load() {
      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
         return (ProjectRegistry) ois.readObject();
      }
      catch (IOException | ClassNotFoundException e) {
         System.err.println("Error loading ProjectRegistry: " + e.getMessage());
         e.printStackTrace();
         return new ProjectRegistry();
      }
   }


   // Only for other service classes to view and filter, cannot make any changes to the original list.
   // Cannot modify the original list structure (add/remove projects).
   // Please use add or remove methods explicitly if manager wants to add or remove projects.
   public List<Project> getProjects() {
      return List.copyOf(projects);
   }

   public void addProject(Project project) {
      projects.add(project);
      save();
   }

   public void removeProject(Project project) {
      projects.remove(project);
   }

   public void save() {
      try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
         oos.writeObject(this);
         System.out.println("ProjectRegistry saved successfully to " + filePath);
      }
      catch (IOException e) {
         System.err.println("Error saving ProjectRegistry: " + e.getMessage());
         e.printStackTrace();
      }
   }

   @Override
   public List<Project> filter(Predicate<Project> predicate) {
      return projects.stream()
              .filter(predicate)
              .collect(Collectors
                      .toList());
   }

   public Integer size() {
      return projects.size();
   }
}

//TODO : validate current user is manager