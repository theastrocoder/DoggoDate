package io.mse.doggodate.entity;



import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.ArrayList;

public class Doggo extends BaseObservable {

    private String name;
    private String breed;
    private ArrayList<Doggo> followers = new ArrayList<>();
    private ArrayList<Doggo> followings = new ArrayList<>();
    private ArrayList<Integer> photos = new ArrayList<>();
    private ArrayList<DoggoEvent> events = new ArrayList<>();
    private int profilePic;
    private boolean active;

    public Doggo(String name, String breed, int profilePic) {
        this.name = name;
        this.breed = breed;
        this.profilePic = profilePic;
    }
    public Doggo(String name, String breed, boolean active) {
        this.name = name;
        this.breed = breed;
        this.active = active;
    }

    public Doggo() {

    }

    public ArrayList<DoggoEvent> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<DoggoEvent> events) {
        this.events = events;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public ArrayList<Doggo> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<Doggo> followers) {
        this.followers = followers;
    }

    public ArrayList<Doggo> getFollowings() {
        return followings;
    }

    public void setFollowings(ArrayList<Doggo> followings) {
        this.followings = followings;
    }

    public ArrayList<Integer> getPhotos() {
        return photos;
    }

    public int getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
    }

    public void setPhotos(ArrayList<Integer> photos) {
        this.photos = photos;
    }

    @Bindable
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
