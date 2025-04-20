package project;

import java.util.Map;

public class ImmutableProjectView {
   private final Project project;

   public ImmutableProjectView(Project project) {
      this.project = project;
   }

   public int getId() {
      return project.getId();
   }

   public String getName() {
      return project.getProjectName();
   }

   public String getNeighbourhood() {
      return project.getNeighborhood();
   }

   public Map<FlatType, Integer> getAvailableFlats() {
      return project.getAvailableFlats();
   }
}
