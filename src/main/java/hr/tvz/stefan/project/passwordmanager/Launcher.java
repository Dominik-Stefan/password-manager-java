package hr.tvz.stefan.project.passwordmanager;

import hr.tvz.stefan.project.passwordmanager.controllers.ShowScreen;
import hr.tvz.stefan.project.passwordmanager.data.ManageChanges;
import hr.tvz.stefan.project.passwordmanager.entities.User;
import hr.tvz.stefan.project.passwordmanager.threads.LoadChangesThread;
import hr.tvz.stefan.project.passwordmanager.threads.StoreChangesThread;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher extends Application {

    private static Stage mainStage;

    public static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    private static User currentUser;

    private static final ManageChanges changes = new ManageChanges();

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        ShowScreen.showLoginScreen();
    }

    public static void main(String[] args) {
        Thread storeChangesThread = new Thread(new  StoreChangesThread());
        Runtime.getRuntime().addShutdownHook(storeChangesThread);

        Thread loadChangesThread = new Thread(new LoadChangesThread());
        loadChangesThread.start();

        launch();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Launcher.currentUser = currentUser;
    }

    public static ManageChanges getChanges() {
        return changes;
    }

}