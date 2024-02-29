package hr.tvz.stefan.project.passwordmanager.util;

import hr.tvz.stefan.project.passwordmanager.entities.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public interface UsersFile {

    static List<User> read() throws IOException {
        String fileName = FileNames.FILENAME_USER_TXT.getFileName();

        List<User> users = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Path.of(fileName), StandardCharsets.UTF_8)) {
            String line;

            long id = 0;
            String username = "";
            String password = "";
            boolean active = false;

            for (int i = 1; (line = reader.readLine()) != null; i++) {
                switch (i % 4) {
                    case 1 -> id = Long.parseLong(line);
                    case 2 -> username = line;
                    case 3 -> password = line;
                    case 0 -> active = Boolean.parseBoolean(line);
                }

                if(i % 4 == 0) {
                    users.add(new User.Builder(id, username).password(password).active(active).build());
                }
            }
        }

        return users;
    }

    static void write(List<User> users) throws IOException {
        String fileName = FileNames.FILENAME_USER_TXT.getFileName();

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(fileName), StandardCharsets.UTF_8)) {
            for (User user : users) {
                long id = user.getId();
                String username = user.getUsername();
                String password = user.getPassword();
                boolean active = user.isActive();

                writer.write(Long.toString(id));
                writer.newLine();
                writer.write(username);
                writer.newLine();
                writer.write(password);
                writer.newLine();
                writer.write(Boolean.toString(active));
                writer.newLine();
            }
        }
    }

}
