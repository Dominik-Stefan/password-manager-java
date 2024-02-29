package hr.tvz.stefan.project.passwordmanager.data;

import hr.tvz.stefan.project.passwordmanager.Launcher;
import hr.tvz.stefan.project.passwordmanager.database.*;
import hr.tvz.stefan.project.passwordmanager.dialogs.AlertDialog;
import hr.tvz.stefan.project.passwordmanager.entities.Account;
import hr.tvz.stefan.project.passwordmanager.entities.Change;
import hr.tvz.stefan.project.passwordmanager.entities.ChangedData;
import hr.tvz.stefan.project.passwordmanager.entities.Entity;
import hr.tvz.stefan.project.passwordmanager.exceptions.DatabaseException;
import hr.tvz.stefan.project.passwordmanager.threads.AddNewAccountChangeThread;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

public class ManageAccounts {

    private List<Account> accountList;

    public ManageAccounts() {
        accountList = new ArrayList<>();
    }

    public void loadAccounts() {
        try {
            accountList = Database.loadAccountsForUser(Launcher.getCurrentUser());
        } catch (DatabaseException ex) {
            String errorMsg = "Failed to load accounts for current user!\nPlease contact technical support!";

            AlertDialog.showErrorDialog(errorMsg);

            accountList = new ArrayList<>();
        }
    }

    public boolean saveNewAccount(Account account) {
        try {
            Database.saveNewAccountForUser(Launcher.getCurrentUser(), account);
        } catch (DatabaseException ex) {
            String errorMsg = "Failed to save new account to database for current user!\nPlease contact technical support!";

            AlertDialog.showErrorDialog(errorMsg);

            return false;
        }

        accountList.add(account);

        Thread addNewAccountChangeThread = new Thread(new AddNewAccountChangeThread(new Change<>(
                Launcher.getChanges().getMaxId() + 1,
                "CREATED NEW ACCOUNT",
                new ChangedData<>(null, account),
                Launcher.getCurrentUser(),
                LocalDateTime.now()
        )));
        addNewAccountChangeThread.start();

        String infoMsg = "A new account has been created\ndescription = " + account.getDescription();

        AlertDialog.showInformationDialog(infoMsg);

        return true;
    }

    public boolean editAccount(Account oldAccount, Account newAccount) {
        String confMsg = "Are you sure you want to edit this account?\ndescription = " + oldAccount.getDescription();

        boolean isAccepted = AlertDialog.showConfirmationDialog(confMsg);

        if(isAccepted) {
            try {
                Database.editAccountForUser(Launcher.getCurrentUser(), oldAccount, newAccount);
            } catch (DatabaseException ex) {
                String errorMsg = "Failed to edit selected account from database for current user!\nPlease contact technical support!";

                AlertDialog.showErrorDialog(errorMsg);

                return false;
            }
            accountList.set(accountList.indexOf(oldAccount), newAccount);

            return true;
        }

        return false;
    }

    public boolean deleteAccount(Account account) {
        String confMsg = "Are you sure you want to delete this account?\ndescription = " + account.getDescription();

        boolean isAccepted = AlertDialog.showConfirmationDialog(confMsg);

        if(isAccepted) {
            try {
                Database.deleteAccountForUser(Launcher.getCurrentUser(), account);
            } catch (DatabaseException ex) {
                String errorMsg = "Failed to delete selected account from database for current user!\nPlease contact technical support!";

                AlertDialog.showErrorDialog(errorMsg);

                return false;
            }
            accountList.remove(account);

            return true;
        }

        return false;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public long getMaxId() {
        OptionalLong tempMaxId = accountList.stream()
                .mapToLong(Entity::getId).max();

        if(tempMaxId.isEmpty()) {
            return 0;
        }

        return tempMaxId.getAsLong();
    }

}
