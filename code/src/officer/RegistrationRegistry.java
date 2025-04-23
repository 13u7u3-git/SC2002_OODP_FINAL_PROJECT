package officer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationRegistry implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<RegistrationForm> registrations;
    private static final String DATA_FILE = "registration_registry.dat";

    public RegistrationRegistry() {
        this.registrations = new ArrayList<>();
        loadRegistry();
    }

    public void addRegistration(RegistrationForm form) {
        registrations.add(form);
        saveRegistry();
    }

    public List<RegistrationForm> getRegistrations() {
        return registrations;
    }

    public RegistrationForm findById(Integer id) {
        for (RegistrationForm form : registrations) {
            if (form.getId().equals(id)) {
                return form;
            }
        }
        return null;
    }

    public void saveRegistry() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(registrations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadRegistry() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            registrations = (List<RegistrationForm>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
