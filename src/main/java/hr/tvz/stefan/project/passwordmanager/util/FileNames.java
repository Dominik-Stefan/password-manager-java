package hr.tvz.stefan.project.passwordmanager.util;

public enum FileNames {

    FILENAME_USER_TXT("files/users.txt"),
    FILENAME_ADMIN_PROPERTIES("files/admin.properties"),
    FILENAME_ADMIN_HOME_SCREEN_FXML("admin-home-screen.fxml"),
    FILENAME_USER_HOME_SCREEN_FXML("user-home-screen.fxml"),
    FILENAME_LOGIN_SCREEN_FXML("login-screen.fxml"),
    FILENAME_ADMIN_USER_MANAGEMENT_FXML("admin-user-management-screen.fxml"),
    FILENAME_DATABASE_PROPERTIES("files/database.properties"),
    FILENAME_CHANGE_DAT("files/changes.dat");

    private final String fileName;

    FileNames(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
    
}
