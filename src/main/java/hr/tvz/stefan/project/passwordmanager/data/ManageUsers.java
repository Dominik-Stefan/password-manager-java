package hr.tvz.stefan.project.passwordmanager.data;

import hr.tvz.stefan.project.passwordmanager.Launcher;
import hr.tvz.stefan.project.passwordmanager.database.Database;
import hr.tvz.stefan.project.passwordmanager.dialogs.AlertDialog;
import hr.tvz.stefan.project.passwordmanager.entities.Entity;
import hr.tvz.stefan.project.passwordmanager.entities.User;
import hr.tvz.stefan.project.passwordmanager.exceptions.DatabaseException;
import hr.tvz.stefan.project.passwordmanager.util.FileNames;
import hr.tvz.stefan.project.passwordmanager.util.UsersFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

public class ManageUsers {

    private List<User> userList;

    public ManageUsers() {
        userList = new ArrayList<>();
    }

    public void loadUsers() {
        try {
            userList = UsersFile.read();
        } catch (IOException ex) {
            Launcher.logger.error("Failed to read file " + FileNames.FILENAME_USER_TXT.getFileName(), ex);

            String errorMsg = "Failed to load critical information!\nPlease contact technical support!";

            AlertDialog.showErrorDialog(errorMsg);

            System.exit(1);
        }
    }

    public boolean storeUsers() {
        try {
            UsersFile.write(userList);
        } catch (IOException ex) {
            Launcher.logger.error("Failed to write to file " + FileNames.FILENAME_USER_TXT, ex);

            String errorMsg = "Failed to store new information!\nPlease contact technical support!";

            AlertDialog.showErrorDialog(errorMsg);

            return false;
        }

        return true;
    }

    public boolean addNewUser(User newUser) {
        try {
            Database.createNewTableForUser(newUser);
        } catch (DatabaseException ex) {
            String errorMsg = "Failed to create a table for the new user!\nPlease contact technical support!";

            AlertDialog.showErrorDialog(errorMsg);

            return false;
        }

        userList.add(newUser);

        return storeUsers();
    }

    public boolean deleteUser(User user) {
        String confMsg = "Are you sure you want to delete this user?\nusername = " + user.getUsername();

        boolean isAccepted = AlertDialog.showConfirmationDialog(confMsg);

        if (isAccepted) {

            try {
                Database.deleteTableForUser(user);
            } catch (DatabaseException ex) {
                String errorMsg = "Failed to delete a table for the user!\nPlease contact technical support!";

                AlertDialog.showErrorDialog(errorMsg);

                return false;
            }

            userList.remove(user);

            return storeUsers();
        }

        return false;
    }

    public boolean deactivateUser(User user) {
        String confMsg = "Are you sure you want to deactivate this user?\nusername = " + user.getUsername();

        boolean isAccepted = AlertDialog.showConfirmationDialog(confMsg);

        if(isAccepted) {
            userList.get(userList.indexOf(user)).setActive(false);
            return storeUsers();
        }

        return false;
    }

    public boolean activateUser(User user) {
        String confMsg = "Are you sure you want to activate this user?\nusername = " + user.getUsername();

        boolean isAccepted = AlertDialog.showConfirmationDialog(confMsg);

        if (isAccepted) {
            userList.get(userList.indexOf(user)).setActive(true);
            return storeUsers();
        }

        return false;
    }

    public List<User> getUserList() {
        return userList;
    }

    public long getMaxId() {
        OptionalLong tempMaxId = userList.stream()
                .mapToLong(Entity::getId).max();

        if(tempMaxId.isEmpty()) {
            return 0;
        }

        return tempMaxId.getAsLong();
    }

}
