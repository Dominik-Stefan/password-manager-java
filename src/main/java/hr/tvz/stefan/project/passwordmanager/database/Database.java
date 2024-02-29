package hr.tvz.stefan.project.passwordmanager.database;

import hr.tvz.stefan.project.passwordmanager.Launcher;
import hr.tvz.stefan.project.passwordmanager.entities.Account;
import hr.tvz.stefan.project.passwordmanager.entities.User;
import hr.tvz.stefan.project.passwordmanager.exceptions.DatabaseException;
import hr.tvz.stefan.project.passwordmanager.exceptions.DecryptionException;
import hr.tvz.stefan.project.passwordmanager.exceptions.EncryptionException;
import hr.tvz.stefan.project.passwordmanager.util.FileNames;
import hr.tvz.stefan.project.passwordmanager.util.PasswordEncryptor;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public interface Database {

    private static Connection connectToDatabase() throws SQLException, IOException {
        Properties databaseProperties = new Properties();

        databaseProperties.load(new FileReader(FileNames.FILENAME_DATABASE_PROPERTIES.getFileName()));

        String databaseURL = databaseProperties.getProperty("databaseUrl");
        String username = databaseProperties.getProperty("username");
        String password = databaseProperties.getProperty("password");

        return DriverManager.getConnection(databaseURL, username, password);
    }

    static void createNewTableForUser(User user) throws DatabaseException {
        String tmpUsername = user.getUsername().replaceAll("[., ]", "_").toUpperCase();

        String sql = "CREATE TABLE " + tmpUsername + "(" +
                "account_id LONG NOT NULL GENERATED always AS IDENTITY, " +
                "description VARCHAR(255) NOT NULL, " +
                "username VARCHAR(255) NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "PRIMARY KEY(account_id))";

        try(Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            Launcher.logger.error("Database error! Failed to create a table for the new user!", ex);
            throw new DatabaseException(ex);
        }
    }

    static void deleteTableForUser(User user) throws DatabaseException {
        String tmpUsername = user.getUsername().replaceAll("[., ]", "_").toUpperCase();

        String sql = "DROP TABLE " + tmpUsername;

        try(Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            Launcher.logger.error("Database error! Failed to delete a table for the user!", ex);
            throw new DatabaseException(ex);
        }
    }

    static List<Account> loadAccountsForUser(User user) throws DatabaseException {
        List<Account> accountList = new ArrayList<>();

        String tmpUsername = user.getUsername().replaceAll("[., ]", "_").toUpperCase();

        String sql = "SELECT * FROM " + tmpUsername + " WHERE 1 = 1";

        try(Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("account_id");
                String description = resultSet.getString("description");
                String username = resultSet.getString("username");
                String encryptedPassword = resultSet.getString("password");
                String password = PasswordEncryptor.decryptPassword(encryptedPassword);

                Account newAccount = new Account(id, description, username, password);

                accountList.add(newAccount);
            }
        } catch (SQLException | IOException | DecryptionException ex) {
            Launcher.logger.error("Database error or decryption failed! Failed to load accounts for current user!", ex);
            throw new DatabaseException(ex);
        }

        return accountList;
    }

    static void saveNewAccountForUser(User user, Account account) throws DatabaseException {
        String tmpUsername = user.getUsername().replaceAll("[., ]", "_").toUpperCase();

        String sql = "INSERT INTO " + tmpUsername + "(description, username, password) VALUES (?, ?, ?)";

        try(Connection connection = connectToDatabase()) {
            String encryptedPassword = PasswordEncryptor.encryptPassword(account.getPassword());

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getDescription());
            preparedStatement.setString(2, account.getUsername());
            preparedStatement.setString(3, encryptedPassword);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException | EncryptionException ex) {
            Launcher.logger.error("Database error or encryption failed! Failed to save new account to database for current user!", ex);
            throw new DatabaseException(ex);
        }
    }

    static void deleteAccountForUser(User user, Account account) throws DatabaseException {
        String tmpUsername = user.getUsername().replaceAll("[., ]", "_").toUpperCase();

        String sql = "DELETE FROM " + tmpUsername + " WHERE ACCOUNT_ID = ?";

        try(Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, account.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            Launcher.logger.error("Database error! Failed to delete selected account from database for current user!", ex);
            throw new DatabaseException(ex);
        }
    }

    static void editAccountForUser(User user, Account oldAccount, Account newAccount) throws DatabaseException {
        String tmpUsername = user.getUsername().replaceAll("[., ]", "_").toUpperCase();

        String sql = "UPDATE " + tmpUsername + " SET DESCRIPTION = ?, USERNAME = ?, PASSWORD = ? WHERE ACCOUNT_ID = ?";

        try(Connection connection = connectToDatabase()) {
            String encryptedPassword = PasswordEncryptor.encryptPassword(newAccount.getPassword());

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newAccount.getDescription());
            preparedStatement.setString(2, newAccount.getUsername());
            preparedStatement.setString(3, encryptedPassword);
            preparedStatement.setLong(4, oldAccount.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException | EncryptionException ex) {
            Launcher.logger.error("Database error or encryption failed! Failed to edit selected account from database for current user!", ex);
            throw new DatabaseException(ex);
        }
    }

}
