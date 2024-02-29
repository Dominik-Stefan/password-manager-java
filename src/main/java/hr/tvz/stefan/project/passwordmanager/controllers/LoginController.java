package hr.tvz.stefan.project.passwordmanager.controllers;

import hr.tvz.stefan.project.passwordmanager.Launcher;
import hr.tvz.stefan.project.passwordmanager.data.ManageUsers;
import hr.tvz.stefan.project.passwordmanager.dialogs.AlertDialog;
import hr.tvz.stefan.project.passwordmanager.entities.Admin;
import hr.tvz.stefan.project.passwordmanager.entities.Change;
import hr.tvz.stefan.project.passwordmanager.entities.ChangedData;
import hr.tvz.stefan.project.passwordmanager.entities.User;
import hr.tvz.stefan.project.passwordmanager.exceptions.HashException;
import hr.tvz.stefan.project.passwordmanager.threads.AddNewUserChangeThread;
import hr.tvz.stefan.project.passwordmanager.util.Hash;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.Optional;

public class LoginController {

    private ManageUsers users;

    private Admin admin;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    public void initialize() {
        admin = new Admin();

        users = new ManageUsers();

        users.loadUsers();
    }

    public void logIn() {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        validation(username, password);
    }

    private void validation(String username, String password) {
        if(isSomethingEmpty(username, password)) {
            return;
        }

        if(isAdmin(username, password)) {
            ShowScreen.showAdminHomeScreen();

            return;
        }

        if(isUser(username, password)) {
            ShowScreen.showUserHomeScreen();
        }
    }

    public void signUp() {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        if(isSomethingEmpty(username, password)) {
            return;
        }

        if(isUsernameTaken(username)) {
            return;
        }

        String tempPassword = AlertDialog.showPasswordConfirmationDialog();

        if(tempPassword.isBlank()) {
            return;
        }

        if(!tempPassword.equals(password)) {
            String errorMsg = "Passwords do not match!";

            AlertDialog.showErrorDialog(errorMsg);

            return;
        }

        String passwordHash;

        try {
            passwordHash = Hash.hashString(password);
        } catch (HashException ex) {
            String errorMsg = "Failed to create a new user!\nHashing algorithm not found!\nPlease contact technical support!";

            AlertDialog.showErrorDialog(errorMsg);

            return;
        }

        User newUser = new User.Builder(users.getMaxId() + 1, username).active(true).password(passwordHash).build();

        boolean isSuccessful = users.addNewUser(newUser);

        if(!isSuccessful) {
            return;
        }

        Thread addNewUserChangeThread = new Thread(new AddNewUserChangeThread(new Change<>(
                Launcher.getChanges().getMaxId() + 1,
                "CREATED NEW USER",
                new ChangedData<>("none", newUser.getUsername()),
                "USER",
                LocalDateTime.now()
        )));
        addNewUserChangeThread.start();

        String infoMsg = "A new user has been created!\nusername = " + newUser.getUsername();

        AlertDialog.showInformationDialog(infoMsg);
    }

    private boolean isSomethingEmpty(String username, String password) {
        if (username.isBlank() || password.isBlank()) {
            StringBuilder errorMsg = new StringBuilder();

            if(username.isBlank()){
                errorMsg.append("Username text field is empty!\n");
            }
            if(password.isBlank()){
                errorMsg.append("Password field is empty!\n");
            }

            AlertDialog.showErrorDialog(errorMsg.toString());

            return true;
        }

        return false;
    }

    private boolean isUsernameTaken(String username) {
        boolean isUsernameTaken = admin.getUsrename().equals(username) || users.getUserList().stream().anyMatch(user -> user.getUsername().equals(username));

        if(isUsernameTaken) {
            String errorMsg = "Username is taken!";

            AlertDialog.showErrorDialog(errorMsg);

            return true;
        }

        return false;
    }

    private boolean isAdmin(String username, String password) {
        return admin.getUsrename().equals(username) && admin.getPassword().equals(password);
    }

    private boolean isUser(String username, String password) {
        String errorMsg;

        Optional<User> tempUser = users.getUserList().stream().filter(user -> user.getUsername().equals(username)).findAny();

        if(tempUser.isEmpty()) {
            errorMsg = "Username does not exist!";

            AlertDialog.showErrorDialog(errorMsg);

            return false;
        } else if (!tempUser.get().isActive()) {
            errorMsg = "User is not active!";

            AlertDialog.showErrorDialog(errorMsg);

            return false;
        }

        try {
            if(!Hash.checkHash(password, tempUser.get().getPassword())) {
                errorMsg = "Password is not correct!";

                AlertDialog.showErrorDialog(errorMsg);

                return false;
            }
        } catch (HashException ex) {
            errorMsg = "Hashing algorithm not found!\nPlease contact technical support!";

            AlertDialog.showErrorDialog(errorMsg);

            return false;
        }

        Launcher.setCurrentUser(tempUser.get());

        return true;
    }

}