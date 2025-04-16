package system;

import applicant.Applicant;
import manager.Manager;
import officer.Officer;
import user.User;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static SessionManager instance = null;
    private User currentUser;
    private final Map<String, Applicant> applicants;
    private final Map<String, Officer> officers;
    private final Map<String, Manager> managers;

    private SessionManager() {
        this.applicants = new HashMap<>();
        this.officers = new HashMap<>();
        this.managers = new HashMap<>();
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void loadData() {
        // TODO
    }

    public void saveData() {
        // TODO
    }

    public boolean authenticateUser(String nric, String password){
        if(applicants.containsKey(nric) && applicants.get(nric).getPassword().equals(password)){
            currentUser = applicants.get(nric);
            return true;
        }
        else if(officers.containsKey(nric) && officers.get(nric).getPassword().equals(password)){
            currentUser = officers.get(nric);
            return true;
        }
        else if(managers.containsKey(nric) && managers.get(nric).getPassword().equals(password)) {
            currentUser = managers.get(nric);
            return true;
        }
        else{
            return false;
        }
    }

    public void logout() {
        this.currentUser = null;
    }

    public void displayMenu(User user) {
        // TODO main menu

    }

    public User getCurrentUser() {
        return currentUser;
    }


    public Map<String, Applicant> getApplicants() {
        return applicants;
    }

    public Map<String, Officer> getOfficers() {
        return officers;
    }

    public Map<String, Manager> getManagers() {
        return managers;
    }
}
