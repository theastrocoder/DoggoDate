package io.mse.doggodate.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DoggoEvent {

    private LocalDateTime dateTime;
    private DoggoZone zone;
    private Doggo creator;


    private ArrayList<Doggo> doggosJoining = new ArrayList<>();

    public DoggoEvent(LocalDateTime dateTime, DoggoZone zone, Doggo creator) {
        this.dateTime = dateTime;
        this.zone = zone;
        this.creator=creator;
    }


    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public DoggoZone getZone() {
        return zone;
    }

    public Doggo getCreator() {
        return creator;
    }

    public void setCreator(Doggo creator) {
        this.creator = creator;
    }

    public void setZone(DoggoZone zone) {
        this.zone = zone;
    }

    public ArrayList<Doggo> getDoggosJoining() {
        return doggosJoining;
    }

    public void setDoggosJoining(ArrayList<Doggo> doggosJoining) {
        this.doggosJoining = doggosJoining;
    }

    public String joiningDoggosToString() {
        String doggos = "";
        for (int i = 0; i < this.doggosJoining.size(); i++) {
            doggos += this.doggosJoining.get(i).getName() + "\n";
        }
        return doggos;

    }
}
