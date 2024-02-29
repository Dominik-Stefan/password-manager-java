package hr.tvz.stefan.project.passwordmanager.threads;

import javafx.application.Platform;
import javafx.stage.Stage;

public class ShowNewWindowThread implements Runnable {

    private final Stage stage;

    public ShowNewWindowThread(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void run() {
        if(stage != null) {
            Platform.runLater(stage::show);
        }
    }
}
