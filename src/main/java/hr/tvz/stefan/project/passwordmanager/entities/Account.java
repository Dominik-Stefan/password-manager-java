package hr.tvz.stefan.project.passwordmanager.entities;

import java.util.Objects;

public class Account extends Entity {

    private String description;

    private String username;

    private String password;

    public Account(long id, String description, String username, String password) {
        super(id);
        this.description = description;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(getDescription(), account.getDescription()) && Objects.equals(getUsername(), account.getUsername()) && Objects.equals(getPassword(), account.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getUsername(), getPassword());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
