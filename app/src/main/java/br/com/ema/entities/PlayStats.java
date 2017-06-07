package br.com.ema.entities;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PlayStats  extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private Date start;
    private Date end;
    private double totalPoints;
    private int userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getUserId() {
        return userId;
    }

    public void addPoint(int point){
        this.totalPoints+=point;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
