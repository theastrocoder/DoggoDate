package io.mse.doggodate.entity;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DoggoEvent extends BaseObservable {

    private LocalDateTime time;
    private DoggoZone zone;
    private Doggo creator;


    private ArrayList<Doggo> doggosJoining = new ArrayList<>();

    public DoggoEvent(LocalDateTime time, DoggoZone zone, Doggo creator2) {
        this.time = time;
        this.zone = zone;
        this.creator = creator2;
    }
public DoggoEvent() {

}
    @Bindable
    public LocalDateTime getTime() {
        return time;
    }

    @Bindable
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Bindable
    public DoggoZone getZone() {
        return zone;
    }

    @Bindable
    public Doggo getCreator() {
        return creator;
    }

    @Bindable("doggoID")
    public void setCreator(Doggo creator3) {
        Log.i("sfds", "sdgsgdfghdhgdhghfgdhfgfcghzbedfhzbdhgv dxfdcxjcdfgx");
        this.creator = creator3;
    }

    @Bindable
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
