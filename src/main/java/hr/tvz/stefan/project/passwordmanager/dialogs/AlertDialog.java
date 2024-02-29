package hr.tvz.stefan.project.passwordmanager.dialogs;

import hr.tvz.stefan.project.passwordmanager.Launcher;
import hr.tvz.stefan.project.passwordmanager.controllers.ShowScreen;
import hr.tvz.stefan.project.passwordmanager.entities.Account;
import hr.tvz.stefan.project.passwordmanager.entities.User;
import hr.tvz.stefan.project.passwordmanager.exceptions.HashException;
import hr.tvz.stefan.project.passwordmanager.util.Hash;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Optional;

public interface AlertDialog {

    static void showErrorDialog(String errorMsg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().getStylesheets().add(ShowScreen.class.getResource("/css/style.css").toExternalForm());
        alert.initOwner(Launcher.getMainStage());
        alert.setTitle("Error");
        alert.setHeaderText("Something went wrong :(");
        alert.setContentText(errorMsg);

        alert.showAndWait();
    }

    static void showInformationDialog(String infoMsg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().add(ShowScreen.class.getResource("/css/style.css").toExternalForm());
        alert.initOwner(Launcher.getMainStage());
        alert.setTitle("Information");
        alert.setHeaderText("You did something right :)");
        alert.setContentText(infoMsg);

        alert.showAndWait();
    }

    static boolean showConfirmationDialog(String confMsg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add(ShowScreen.class.getResource("/css/style.css").toExternalForm());
        alert.initOwner(Launcher.getMainStage());
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm your actions");
        alert.setContentText(confMsg);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    static String showPasswordConfirmationDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.getDialogPane().getStylesheets().add(ShowScreen.class.getResource("/css/style.css").toExternalForm());
        dialog.initOwner(Launcher.getMainStage());
        dialog.setTitle("Password confirmation");
        dialog.setHeaderText("Please confirm your password");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        PasswordField passwordField = new PasswordField();
        HBox content = new HBox();

        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Password:"), passwordField);

        dialog.getDialogPane().setContent(content);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return passwordField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        }

        return "";
    }

    static Account showEditAccountDialog(Account oldAccount) {
        Dialog<Account> dialog = new Dialog<>();
        dialog.getDialogPane().getStylesheets().add(ShowScreen.class.getResource("/css/style.css").toExternalForm());
        dialog.initOwner(Launcher.getMainStage());
        dialog.setTitle("Edit account");
        dialog.setHeaderText(null);

        ButtonType editButtonType = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField description = new TextField(oldAccount.getDescription());
        description.setPromptText("Description");
        TextField username = new TextField(oldAccount.getUsername());
        username.setPromptText("Username");
        TextField password = new TextField(oldAccount.getPassword());
        password.setPromptText("Password");

        grid.add(new Label("Description:"), 0, 0);
        grid.add(description, 1, 0);
        grid.add(new Label("Username:"), 0, 1);
        grid.add(username, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(password, 1, 2);

        Node editButton = dialog.getDialogPane().lookupButton(editButtonType);
        editButton.setDisable(true);

        ChangeListener<String> listener = (observable, oldValue, newValue) -> {
            editButton.setDisable(description.getText().isBlank() || username.getText().isBlank() || password.getText().isBlank());
        };

        description.textProperty().addListener(listener);
        username.textProperty().addListener(listener);
        password.textProperty().addListener(listener);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editButtonType) {
                return new Account(oldAccount.getId(), description.getText(), username.getText(), password.getText());
            }
            return null;
        });

        Optional<Account> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        }

        return oldAccount;
    }

    static Account showAddAccountDialog(long maxId) {
        Dialog<Account> dialog = new Dialog<>();
        dialog.getDialogPane().getStylesheets().add(ShowScreen.class.getResource("/css/style.css").toExternalForm());
        dialog.initOwner(Launcher.getMainStage());
        dialog.setTitle("Add account");
        dialog.setHeaderText(null);

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField description = new TextField();
        description.setPromptText("Description");
        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Description:"), 0, 0);
        grid.add(description, 1, 0);
        grid.add(new Label("Username:"), 0, 1);
        grid.add(username, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(password, 1, 2);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        ChangeListener<String> listener = (observable, oldValue, newValue) -> {
            addButton.setDisable(description.getText().isBlank() || username.getText().isBlank() || password.getText().isBlank());
        };

        description.textProperty().addListener(listener);
        username.textProperty().addListener(listener);
        password.textProperty().addListener(listener);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Account(maxId + 1, description.getText(), username.getText(), password.getText());
            }
            return null;
        });

        Optional<Account> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        }

        return null;
    }

    static User showAddUserDialog(long maxId) {
        Dialog<User> dialog = new Dialog<>();
        dialog.getDialogPane().getStylesheets().add(ShowScreen.class.getResource("/css/style.css").toExternalForm());
        dialog.initOwner(Launcher.getMainStage());
        dialog.setTitle("Add user");
        dialog.setHeaderText(null);

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        PasswordField passwordConf = new PasswordField();
        passwordConf.setPromptText("Confirm Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);
        grid.add(new Label("Confirm password:"), 0, 2);
        grid.add(passwordConf, 1, 2);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        ChangeListener<String> listener = (observable, oldValue, newValue) -> {
            addButton.setDisable(username.getText().isBlank() || password.getText().isBlank() || passwordConf.getText().isBlank() || !password.getText().equals(passwordConf.getText()));
        };

        username.textProperty().addListener(listener);
        password.textProperty().addListener(listener);
        passwordConf.textProperty().addListener(listener);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    return new User.Builder(maxId + 1, username.getText()).password(Hash.hashString(password.getText())).active(true).build();
                } catch (HashException e) {
                    String errorMsg = "Failed to create a new user!\nHashing algorithm not found!\nPlease contact technical support!";

                    AlertDialog.showErrorDialog(errorMsg);
                }
            }
            return null;
        });

        Optional<User> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        }

        return null;
    }

}
