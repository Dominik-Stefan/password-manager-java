package hr.tvz.stefan.project.passwordmanager.controllers;

import hr.tvz.stefan.project.passwordmanager.Launcher;
import hr.tvz.stefan.project.passwordmanager.data.ManageAccounts;
import hr.tvz.stefan.project.passwordmanager.dialogs.AlertDialog;
import hr.tvz.stefan.project.passwordmanager.entities.Account;
import hr.tvz.stefan.project.passwordmanager.entities.Change;
import hr.tvz.stefan.project.passwordmanager.entities.ChangedData;
import hr.tvz.stefan.project.passwordmanager.threads.AddNewAccountChangeThread;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.time.LocalDateTime;
import java.util.Optional;

public class UserHomeController {

    private ManageAccounts accounts;

    private FilteredList<Account> filteredAccounts;

    private Optional<Account> selectedAccount = Optional.empty();

    private ContextMenu contextMenu;

    private MenuItem deleteOption;

    private MenuItem editOption;

    private MenuItem addOption;

    @FXML
    private CheckBox showPasswordsCheckBox;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TableView<Account> accountTableView;

    @FXML
    private TableColumn<Account, String> descriptionTableColumn;

    @FXML
    private TableColumn<Account, String> usernameTableColumn;

    @FXML
    private TableColumn<Account, String> passwordTableColumn;

    public void initialize() {
        accounts = new ManageAccounts();

        accounts.loadAccounts();

        setTableColumns();

        setListenerForCheckBox();

        setListenerForFields();

        accountTableView.setItems(filteredAccounts);

        setListenerForSelectedAccount();

        createContextMenuForTable();
    }

    private void setTableColumns() {
        descriptionTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getDescription()));

        usernameTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getUsername()));

        passwordTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getPassword()));

        passwordTableColumn.setVisible(false);
    }

    private void setListenerForCheckBox() {
        showPasswordsCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            passwordTableColumn.setVisible(showPasswordsCheckBox.isSelected());
        });
    }

    private void setListenerForFields() {
        filteredAccounts = new FilteredList<>(FXCollections.observableList(accounts.getAccountList()), p -> true);

        ChangeListener<String> listener = (observable, oldValue, newValue) -> {
            filteredAccounts.setPredicate(account -> {
                boolean descriptionFilter = true;

                if(!descriptionTextField.getText().isBlank()){
                    descriptionFilter = account.getDescription().toLowerCase().contains(descriptionTextField.getText().toLowerCase());
                }

                boolean usernameFilter = true;

                if(!usernameTextField.getText().isBlank()){
                    usernameFilter = account.getUsername().toLowerCase().contains(usernameTextField.getText().toLowerCase());
                }

                return descriptionFilter && usernameFilter;
            });
        };

        descriptionTextField.textProperty().addListener(listener);
        usernameTextField.textProperty().addListener(listener);
    }

    private void setListenerForSelectedAccount() {
        accountTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedAccount = Optional.ofNullable(accountTableView.getSelectionModel().getSelectedItem());
        });
    }

    private void createContextMenuForTable() {
        contextMenu = new ContextMenu();

        editOption = new MenuItem("Edit");
        deleteOption = new MenuItem("Delete");
        addOption = new MenuItem("Add");

        editOption.setOnAction(event -> editAccount());
        deleteOption.setOnAction(event -> deleteAccount());
        addOption.setOnAction(event -> addAccount());

        editOption.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
        deleteOption.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
        addOption.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

        contextMenu.getItems().addAll(editOption, deleteOption, addOption);

        accountTableView.setContextMenu(contextMenu);

        accountTableView.setOnContextMenuRequested(event -> contextMenu.show(accountTableView, event.getScreenX(), event.getScreenY()));
    }

    private void editAccount() {
        if(selectedAccount.isPresent()) {
            Account newAccount = AlertDialog.showEditAccountDialog(selectedAccount.get());

            if(!newAccount.equals(selectedAccount.get())) {
                boolean isSuccessful = accounts.editAccount(selectedAccount.get(), newAccount);

                if (isSuccessful) {
                    Thread addNewAccountChangeThread = new Thread(new AddNewAccountChangeThread(new Change<>(
                            Launcher.getChanges().getMaxId() + 1,
                            "",
                            new ChangedData<>(selectedAccount.get(), newAccount),
                            Launcher.getCurrentUser(),
                            LocalDateTime.now()
                    )));
                    addNewAccountChangeThread.start();
                    refreshTable();
                }
            }
        }
    }

    private void deleteAccount() {
        if(selectedAccount.isPresent()) {
            boolean isSuccessful = accounts.deleteAccount(selectedAccount.get());

            if(isSuccessful) {
                Thread addNewAccountChangeThread = new Thread(new AddNewAccountChangeThread(new Change<>(
                        Launcher.getChanges().getMaxId() + 1,
                        "DELETED ACCOUNT",
                        new ChangedData<>(selectedAccount.get(), null),
                        Launcher.getCurrentUser(),
                        LocalDateTime.now()
                )));
                addNewAccountChangeThread.start();
                refreshTable();
            }
        }
    }

    public void addAccount() {
        Optional<Account> newAccount = Optional.ofNullable(AlertDialog.showAddAccountDialog(accounts.getMaxId()));
        if(newAccount.isPresent()) {
            boolean isSuccessful = accounts.saveNewAccount(newAccount.get());

            if (isSuccessful) {
                refreshTable();
            }
        }
    }

    private void refreshTable() {
        filteredAccounts = new FilteredList<>(FXCollections.observableList(accounts.getAccountList()));
        accountTableView.setItems(filteredAccounts);
        clearFields();
    }

    public void clearFields() {
        descriptionTextField.clear();
        usernameTextField.clear();
    }

    public void logOut() {
        Launcher.setCurrentUser(null);
        ShowScreen.showLoginScreen();
    }

}
