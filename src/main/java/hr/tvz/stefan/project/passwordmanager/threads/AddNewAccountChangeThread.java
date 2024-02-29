package hr.tvz.stefan.project.passwordmanager.threads;

import hr.tvz.stefan.project.passwordmanager.Launcher;
import hr.tvz.stefan.project.passwordmanager.entities.Account;
import hr.tvz.stefan.project.passwordmanager.entities.Change;
import hr.tvz.stefan.project.passwordmanager.entities.ChangedData;
import hr.tvz.stefan.project.passwordmanager.entities.User;

public class AddNewAccountChangeThread implements Runnable {

    Change<Account, User> change;

    public AddNewAccountChangeThread(Change<Account, User> change) {
        this.change = change;
    }

    @Override
    public void run() {
        if(change.getChangedData().oldData() == null) {
            Launcher.getChanges().addNewChange(new Change<>(
                    change.getId(),
                    change.getDescription(),
                    new ChangedData<>("none", change.getChangedData().newData().getDescription()),
                    change.getWhoChangedData().getUsername(),
                    change.getDateAndTime()
                    ));
        } else if (change.getChangedData().newData() == null) {
            Launcher.getChanges().addNewChange(new Change<>(
                    change.getId(),
                    change.getDescription(),
                    new ChangedData<>(change.getChangedData().oldData().getDescription(), "none"),
                    change.getWhoChangedData().getUsername(),
                    change.getDateAndTime()
            ));
        } else {
            if((!change.getChangedData().oldData().getDescription().equals(change.getChangedData().newData().getDescription()))) {
                Launcher.getChanges().addNewChange(new Change<>(
                        change.getId(),
                        "EDITED DESCRIPTION",
                        new ChangedData<>(change.getChangedData().oldData().getDescription(), change.getChangedData().newData().getDescription()),
                        change.getWhoChangedData().getUsername(),
                        change.getDateAndTime()
                ));
            }

            if((!change.getChangedData().oldData().getUsername().equals(change.getChangedData().newData().getUsername()))) {
                Launcher.getChanges().addNewChange(new Change<>(
                        change.getId(),
                        "EDITED USERNAME",
                        new ChangedData<>(change.getChangedData().oldData().getUsername(), change.getChangedData().newData().getUsername()),
                        change.getWhoChangedData().getUsername(),
                        change.getDateAndTime()
                ));
            }

            if((!change.getChangedData().oldData().getPassword().equals(change.getChangedData().newData().getPassword()))) {
                Launcher.getChanges().addNewChange(new Change<>(
                        change.getId(),
                        "EDITED PASSWORD",
                        new ChangedData<>(change.getChangedData().oldData().getPassword(), change.getChangedData().newData().getPassword()),
                        change.getWhoChangedData().getUsername(),
                        change.getDateAndTime()
                ));
            }
        }
    }

}
