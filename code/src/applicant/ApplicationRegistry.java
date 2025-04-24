package applicant;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationRegistry implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String filePath = "./data/applicationRegistry.dat";

    private final List<Application> applications;

    public ApplicationRegistry() {
        this.applications = new ArrayList<>();
    }

    public static ApplicationRegistry load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (ApplicationRegistry) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("No existing ApplicationRegistry found, starting a new one.");
            return new ApplicationRegistry();
        }
    }

    public void addApplication(Application application) {
        applications.add(application);
        save();
    }

    public void removeApplication(Application application) {
        applications.remove(application);
        save();
    }

    public void printAllApplications() {
        for (Application app : applications) {
            System.out.println(app);
        }
    }

    public List<Application> getApplications() {
        return List.copyOf(applications);
    }

    public int size() {
        return applications.size();
    }

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
            System.out.println("ApplicationRegistry saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving ApplicationRegistry: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
