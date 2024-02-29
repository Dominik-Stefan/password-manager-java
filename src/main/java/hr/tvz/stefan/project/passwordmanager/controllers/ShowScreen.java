package hr.tvz.stefan.project.passwordmanager.controllers;

import hr.tvz.stefan.project.passwordmanager.Launcher;
import hr.tvz.stefan.project.passwordmanager.dialogs.AlertDialog;
import hr.tvz.stefan.project.passwordmanager.threads.ShowNewWindowThread;
import hr.tvz.stefan.project.passwordmanager.util.FileNames;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public interface ShowScreen {

    private static void showScreen(String fxmlFileName, double height, double width) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(fxmlFileName));
        Scene scene = new Scene(fxmlLoader.load(), height, width);
        scene.getStylesheets().add(ShowScreen.class.getResource("/css/style.css").toExternalForm());
        KeyCombination keyCombination = new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN);
        scene.getAccelerators().put(keyCombination, new ShowNewWindowThread(createNewWindowPasswordGenerator()));
        Launcher.getMainStage().setTitle("Password Manager");
        Launcher.getMainStage().getIcons().add(new Image(ShowScreen.class.getResourceAsStream("/images/password_manager_icon.png")));
        Launcher.getMainStage().setScene(scene);
        Launcher.getMainStage().show();
    }

    private static Stage createNewWindowPasswordGenerator() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("password-generator-screen.fxml"));
            Stage newStage = new Stage();
            newStage.setTitle("Password generator");
            newStage.getIcons().add(new Image(ShowScreen.class.getResourceAsStream("/images/password_manager_icon.png")));
            Scene newScene = new Scene(fxmlLoader.load(), 300, 125);
            newScene.getStylesheets().add(ShowScreen.class.getResource("/css/style.css").toExternalForm());
            newStage.setScene(newScene);
            return newStage;
        } catch (IOException ex) {
            Launcher.logger.error("Faield to load password generator!", ex);
            return null;
        }
    }

    static void showLoginScreen() {
        try {
            ShowScreen.showScreen(FileNames.FILENAME_LOGIN_SCREEN_FXML.getFileName(), 400, 300);
        } catch (IOException | RuntimeException ex) {
            Launcher.logger.error(FileNames.FILENAME_LOGIN_SCREEN_FXML.getFileName() + " not found!", ex);

            String errorMsg = FileNames.FILENAME_LOGIN_SCREEN_FXML.getFileName() + " not found!";

            AlertDialog.showErrorDialog(errorMsg);

            System.exit(1);
        }
    }

    static void showAdminHomeScreen() {
        try {
            ShowScreen.showScreen(FileNames.FILENAME_ADMIN_HOME_SCREEN_FXML.getFileName(), 700, 400);
        } catch (IOException | RuntimeException ex) {
            Launcher.logger.error(FileNames.FILENAME_ADMIN_HOME_SCREEN_FXML.getFileName() + " not found!", ex);

            String errorMsg = FileNames.FILENAME_ADMIN_HOME_SCREEN_FXML.getFileName() + " not found!";

            AlertDialog.showErrorDialog(errorMsg);
        }
    }

    static void showUserHomeScreen() {
        try {
            ShowScreen.showScreen(FileNames.FILENAME_USER_HOME_SCREEN_FXML.getFileName(), 700, 400);
        } catch (IOException | RuntimeException ex) {
            Launcher.logger.error(FileNames.FILENAME_USER_HOME_SCREEN_FXML.getFileName() + " not found!", ex);

            String errorMsg = FileNames.FILENAME_USER_HOME_SCREEN_FXML.getFileName() + " not found!";

            AlertDialog.showErrorDialog(errorMsg);
        }
    }

    static void showAdminUserManagementScreen() {
        try {
            ShowScreen.showScreen(FileNames.FILENAME_ADMIN_USER_MANAGEMENT_FXML.getFileName(), 700, 400);
        } catch (IOException | RuntimeException ex) {
            Launcher.logger.error(FileNames.FILENAME_ADMIN_USER_MANAGEMENT_FXML.getFileName() + " not found!", ex);

            String errorMsg = FileNames.FILENAME_ADMIN_USER_MANAGEMENT_FXML.getFileName() + " not found!";

            AlertDialog.showErrorDialog(errorMsg);
        }
    }

}
