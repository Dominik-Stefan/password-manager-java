package hr.tvz.stefan.project.passwordmanager.entities;

import hr.tvz.stefan.project.passwordmanager.Launcher;
import hr.tvz.stefan.project.passwordmanager.util.FileNames;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Admin {

    private String usrename = "";

    private String password = "";

    public Admin() {
        try {
            Properties adminProperties = new Properties();

            adminProperties.load(new FileReader(FileNames.FILENAME_ADMIN_PROPERTIES.getFileName()));

            this.usrename = adminProperties.getProperty("username");
            this.password = adminProperties.getProperty("password");

            if (this.usrename.isBlank() || this.password.isBlank()) {
                throw new IOException();
            }
        } catch (IOException ex) {
            Launcher.logger.error(FileNames.FILENAME_ADMIN_PROPERTIES.getFileName() + " not found or the information is not correctly stored!", ex);
        }
    }

    public String getUsrename() {
        return usrename;
    }

    public String getPassword() {
        return password;
    }

}
