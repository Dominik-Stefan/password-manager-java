package hr.tvz.stefan.project.passwordmanager.entities;

import java.util.Objects;

public class User extends Entity {

    private String username;

    private String password;

    private boolean active;

    private User(Builder builder) {
        super(builder.id);
        this.username = builder.username;
        this.password = builder.password;
        this.active = builder.active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return isActive() == user.isActive() && Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), isActive());
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static class Builder {
        private final long id;
        private String username;
        private String password;

        private boolean active;

        public Builder(long id, String username) {
            this.id = id;
            this.username = username;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

}
