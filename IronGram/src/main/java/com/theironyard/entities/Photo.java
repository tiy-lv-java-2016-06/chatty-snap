package com.theironyard.entities;

import javafx.beans.DefaultProperty;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * Created by jeff on 7/28/16.
 */
@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User recipient;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private boolean photoPublic;

    @Column(nullable = false)
    private long lifeDuration;

    public Photo() {
    }

    public Photo(User sender, User recipient, String filename) {
        this.sender = sender;
        this.recipient = recipient;
        this.filename = filename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isPhotoPublic() {
        return photoPublic;
    }

    public void setPhotoPublic(boolean photoPublic) {
        this.photoPublic = photoPublic;
    }

    public long getLifeDuration() {
        return lifeDuration;
    }

    public void setLifeDuration(long lifeDuration) {
        this.lifeDuration = lifeDuration;
    }
}
