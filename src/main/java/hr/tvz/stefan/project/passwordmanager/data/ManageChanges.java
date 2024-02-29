package hr.tvz.stefan.project.passwordmanager.data;

import hr.tvz.stefan.project.passwordmanager.Launcher;
import hr.tvz.stefan.project.passwordmanager.entities.Change;
import hr.tvz.stefan.project.passwordmanager.entities.Entity;
import hr.tvz.stefan.project.passwordmanager.util.ChangesFile;
import hr.tvz.stefan.project.passwordmanager.util.FileNames;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

public class ManageChanges {

    private List<Change<String, String>> changeList;

    public ManageChanges() {
        changeList = new ArrayList<>();
    }

    public synchronized void loadChanges() {
        try {
            changeList = ChangesFile.read();
        } catch (IOException | ClassNotFoundException ex) {
            Launcher.logger.error("Failed to read file " + FileNames.FILENAME_CHANGE_DAT.getFileName(), ex);
        }
    }

    public synchronized void storeChanges() {
        try {
            ChangesFile.write(changeList);
        } catch (IOException ex) {
            Launcher.logger.error("Failed to write to file " + FileNames.FILENAME_CHANGE_DAT, ex);
        }
    }

    public synchronized void addNewChange(Change<String, String> change) {
        changeList.add(change);
    }

    public synchronized List<Change<String, String>> getChangeList() {
        return changeList;
    }

    public synchronized long getMaxId() {
       OptionalLong tempMaxId = changeList.stream()
               .mapToLong(Entity::getId).max();

       if(tempMaxId.isEmpty()) {
           return 0;
       }

       return tempMaxId.getAsLong();
    }

}
