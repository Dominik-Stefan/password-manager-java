package hr.tvz.stefan.project.passwordmanager.threads;

import hr.tvz.stefan.project.passwordmanager.Launcher;

public class StoreChangesThread implements Runnable {

    @Override
    public void run() {
        Launcher.getChanges().storeChanges();
    }

}
