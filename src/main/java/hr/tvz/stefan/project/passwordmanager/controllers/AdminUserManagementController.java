package hr.tvz.stefan.project.passwordmanager.controllers;

import hr.tvz.stefan.project.passwordmanager.Launcher;
import hr.tvz.stefan.project.passwordmanager.data.ManageUsers;
import hr.tvz.stefan.project.passwordmanager.dialogs.AlertDialog;
import hr.tvz.stefan.project.passwordmanager.entities.Change;
import hr.tvz.stefan.project.passwordmanager.entities.ChangedData;
import hr.tvz.stefan.project.passwordmanager.entities.User;
import hr.tvz.stefan.project.passwordmanager.threads.AddNewUserChangeThread;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.time.LocalDateTime;
import java.util.Optional;

public class AdminUserManagementController {

    private ManageUsers users;

    private FilteredList<User> filteredUsers;

    private ContextMenu contextMenu;

    private MenuItem addOption;

    private MenuItem deleteOption;

    private MenuItem activateOption;

    private MenuItem deactivateOption;

    private Optional<User> selectedUser = Optional.empty();

    @FXML
    private TextField usernameTextField;

    @FXML
    private ChoiceBox<String> activeChoiceBox;

    @FXML
    private TableView<User> userTableView;

    @FXML
    private TableColumn<User, String> usernameTableColumn;

    @FXML
    private TableColumn<User, String> activeTableColumn;

    public void initialize() {
        users = new ManageUsers();

        users.loadUsers();

        setTableColumns();

        setListeners();

        userTableView.setItems(filteredUsers);

        setListenerForSelectedUser();

        createContextMenuForTable();
    }

    private void setTableColumns() {
        usernameTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getUsername()));

        activeTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(Boolean.toString(cellData.getValue().isActive())));
    }

    private void setListeners() {
        filteredUsers = new FilteredList<>(FXCollections.observableList(users.getUserList()), p -> true);

        activeChoiceBox.getItems().addAll("All", "Active", "Deactive");

        activeChoiceBox.setValue("All");

        usernameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filter();
        });
        activeChoiceBox.setOnAction(event -> {
            filter();
        });
    }

    private void filter() {
        filteredUsers.setPredicate(user -> {
            boolean usernameFilter = true;

            if (!usernameTextField.getText().isBlank()) {
                usernameFilter = user.getUsername().toLowerCase().contains(usernameTextField.getText().toLowerCase());
            }

            String selectedOption = activeChoiceBox.getValue();

            boolean activeFilter = true;

            if (selectedOption.equals("Active")) {
                if(!user.isActive()) {
                    activeFilter = false;
                }
            } else if (selectedOption.equals("Deactive")) {
                if(user.isActive()) {
                    activeFilter = false;
                }
            }

            return usernameFilter && activeFilter;
        });
    }

    private void setListenerForSelectedUser() {
        userTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedUser = Optional.ofNullable(userTableView.getSelectionModel().getSelectedItem());
            if(selectedUser.isPresent()) {
                if(selectedUser.get().isActive()){
                    activateOption.setVisible(false);
                    activateOption.setDisable(true);
                    deactivateOption.setVisible(true);
                    deactivateOption.setDisable(false);
                } else {
                    deactivateOption.setVisible(false);
                    deactivateOption.setDisable(true);
                    activateOption.setVisible(true);
                    activateOption.setDisable(false);
                }
            }
        });
    }

    private void createContextMenuForTable() {
        contextMenu = new ContextMenu();

        deactivateOption = new MenuItem("Deactivate");
        activateOption = new MenuItem("Activate");
        deleteOption = new MenuItem("Delete");
        addOption = new MenuItem("Add");

        deactivateOption.setOnAction(event -> deactivateUser());
        activateOption.setOnAction(event -> activateUser());
        deleteOption.setOnAction(event -> deleteUser());
        addOption.setOnAction(event -> addUser());

        deactivateOption.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN));
        activateOption.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.ALT_DOWN));
        deleteOption.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
        addOption.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

        contextMenu.getItems().addAll(deactivateOption, activateOption, deleteOption, addOption);

        userTableView.setContextMenu(contextMenu);

        userTableView.setOnContextMenuRequested(event -> contextMenu.show(userTableView, event.getScreenX(), event.getScreenY()));
    }

    public void deactivateUser() {
        if (selectedUser.isPresent()) {
            boolean isSuccessful = users.deactivateUser(selectedUser.get());

            if (!isSuccessful) {
                return;
            }

            Thread addNewUserChangeThread = new Thread(new AddNewUserChangeThread(new Change<>(
                    Launcher.getChanges().getMaxId() + 1,
                    "DEACTIVATED USER",
                    new ChangedData<>(selectedUser.get().getUsername(), "deactivated"),
                    "ADMIN",
                    LocalDateTime.now()
            )));
            addNewUserChangeThread.start();

            refreshTable();
        }
    }

    public void activateUser() {
        if (selectedUser.isPresent()) {
            boolean isSuccessful = users.activateUser(selectedUser.get());

            if (!isSuccessful) {
                return;
            }

            Thread addNewUserChangeThread = new Thread(new AddNewUserChangeThread(new Change<>(
                    Launcher.getChanges().getMaxId() + 1,
                    "ACTIVATED USER",
                    new ChangedData<>(selectedUser.get().getUsername(), "activated"),
                    "ADMIN",
                    LocalDateTime.now()
            )));
            addNewUserChangeThread.start();

            refreshTable();
        }
    }

    private void deleteUser() {
        if (selectedUser.isPresent()) {
            boolean isSuccessful = users.deleteUser(selectedUser.get());

            if (!isSuccessful) {
                return;
            }

            Thread addNewUserChangeThread = new Thread(new AddNewUserChangeThread(new Change<>(
                    Launcher.getChanges().getMaxId() + 1,
                    "DELETED USER",
                    new ChangedData<>(selectedUser.get().getUsername(), "none"),
                    "ADMIN",
                    LocalDateTime.now()
            )));
            addNewUserChangeThread.start();

            refreshTable();
        }
    }

    public void addUser() {
        Optional<User> newUser = Optional.ofNullable(AlertDialog.showAddUserDialog(users.getMaxId()));
        if(newUser.isPresent()) {
            boolean isSuccessful = users.addNewUser(newUser.get());

            if (!isSuccessful) {
                return;
            }

            Thread addNewUserChangeThread = new Thread(new AddNewUserChangeThread(new Change<>(
                    Launcher.getChanges().getMaxId() + 1,
                    "CREATED NEW USER",
                    new ChangedData<>("none", newUser.get().getUsername()),
                    "ADMIN",
                    LocalDateTime.now()
            )));
            addNewUserChangeThread.start();

            refreshTable();
        }
    }

    private void refreshTable() {
        filteredUsers = new FilteredList<>(FXCollections.observableList(users.getUserList()));
        userTableView.setItems(filteredUsers);
        usernameTextField.clear();
        activeChoiceBox.setValue("Active");
        activeChoiceBox.setValue("All");
    }

    public void home() {
        ShowScreen.showAdminHomeScreen();
    }

    public void logOut() {
        ShowScreen.showLoginScreen();
    }

}
