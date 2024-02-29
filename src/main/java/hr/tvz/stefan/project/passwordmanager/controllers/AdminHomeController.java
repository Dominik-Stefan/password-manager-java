package hr.tvz.stefan.project.passwordmanager.controllers;

import hr.tvz.stefan.project.passwordmanager.Launcher;
import hr.tvz.stefan.project.passwordmanager.entities.Change;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AdminHomeController {

    @FXML
    private TableView<Change<String, String>> changeTableView;

    @FXML
    private TableColumn<Change<String, String>, String> descriptionTableColumn;

    @FXML
    private TableColumn<Change<String, String>, String> oldValueTableColumn;

    @FXML
    private TableColumn<Change<String, String>, String> newValueTableColumn;

    @FXML
    private TableColumn<Change<String, String>, String> whoMadeTheChangeTableColumn;

    @FXML
    private TableColumn<Change<String, String>, String> dateAndTimeTableColumn;

    public void initialize() {
        setTableColumns();

        changeTableView.setItems(FXCollections.observableList(Launcher.getChanges().getChangeList()));
    }

    private void setTableColumns() {
        descriptionTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getDescription()));

        oldValueTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getChangedData().oldData()));

        newValueTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getChangedData().newData()));

        whoMadeTheChangeTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getWhoChangedData()));

        dateAndTimeTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getDateAndTime().toString()));
    }

    public void manageUsersScreen() {
        ShowScreen.showAdminUserManagementScreen();
    }

    public void logOut() {
        ShowScreen.showLoginScreen();
    }

}
