package hr.tvz.stefan.project.passwordmanager.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Change<T1, T2> extends Entity implements Serializable {

    private String description;

    private ChangedData<T1> changedData;

    private T2 whoChangedData;

    private LocalDateTime dateAndTime;

    public Change(long id, String description,ChangedData<T1> changedData, T2 whoChangedData, LocalDateTime dateAndTime) {
        super(id);
        this.description = description;
        this.changedData = changedData;
        this.whoChangedData = whoChangedData;
        this.dateAndTime = dateAndTime;
    }

    public ChangedData<T1> getChangedData() {
        return changedData;
    }

    public void setChangedData(ChangedData<T1> changedData) {
        this.changedData = changedData;
    }

    public T2 getWhoChangedData() {
        return whoChangedData;
    }

    public void setWhoChangedData(T2 whoChangedData) {
        this.whoChangedData = whoChangedData;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
