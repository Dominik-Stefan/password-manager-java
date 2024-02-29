package hr.tvz.stefan.project.passwordmanager.entities;

import java.io.Serializable;

public record ChangedData<T>(T oldData, T newData) implements Serializable {

    public ChangedData(T oldData, T newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public T oldData() {
        return oldData;
    }

    @Override
    public T newData() {
        return newData;
    }

}
