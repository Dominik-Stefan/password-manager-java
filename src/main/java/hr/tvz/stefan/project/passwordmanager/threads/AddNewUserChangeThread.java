package hr.tvz.stefan.project.passwordmanager.threads;

import hr.tvz.stefan.project.passwordmanager.Launcher;
import hr.tvz.stefan.project.passwordmanager.entities.Change;

public class AddNewUserChangeThread implements Runnable {

    Change<String, String> change;

    public AddNewUserChangeThread(Change<String, String> change) {
        this.change = change;
    }

    @Override
    public void run() {
        Launcher.getChanges().addNewChange(change);
    }

}
