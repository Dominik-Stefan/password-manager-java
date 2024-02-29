package hr.tvz.stefan.project.passwordmanager.util;

import hr.tvz.stefan.project.passwordmanager.entities.Change;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public interface ChangesFile {

    static List<Change<String, String>> read() throws IOException, ClassNotFoundException {
        String fileName = FileNames.FILENAME_CHANGE_DAT.getFileName();

        List<Change<String, String>> changes = new ArrayList<>();

        try (ObjectInputStream oIs = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)))) {

            Object tmp;

            while (!((tmp = oIs.readObject()) instanceof EndOfFile)) {
                changes.add((Change<String, String>) tmp);
            }

        }

        return changes;
    }

    static void write(List<Change<String, String>> changes) throws IOException {
        String fileName = FileNames.FILENAME_CHANGE_DAT.getFileName();

        try (ObjectOutputStream oOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)))) {

            for (Change<String, String> change : changes) {
                oOut.writeObject(change);
            }

            oOut.writeObject(new EndOfFile());

        }
    }

}
