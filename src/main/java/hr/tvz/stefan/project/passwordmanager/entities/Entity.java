package hr.tvz.stefan.project.passwordmanager.entities;

import java.io.Serializable;

public abstract class Entity implements Serializable {

    private final long id;

    public Entity(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
