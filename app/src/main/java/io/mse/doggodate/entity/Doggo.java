package io.mse.doggodate.entity;



import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import java.util.ArrayList;

public class Doggo extends BaseObservable {

    private String name;
    private String breed;
    private ArrayList<Doggo> followers = new ArrayList<>();
    private ArrayList<Doggo> followings = new ArrayList<>();
    private ArrayList<Integer> photos = new ArrayList<>();
    private ArrayList<Long> photosLong = new ArrayList<>();
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

    @Bindable
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

    @Bindable
    public ArrayList<Integer> getPhotos() {
        return photos;
    }

    @Bindable
    public int getProfilePic() {
        return profilePic;
    }

    @BindingAdapter("android:src")
    public void setProfilePic(ImageView imageView, int resource){
        imageView.setImageResource(resource);

    }
    @Bindable
    public ArrayList<Long> getPhotosLong() {
        return photosLong;
    }
    @Bindable
    public void setPhotosLong(ArrayList<Long> photoLong) {
        this.photosLong = photoLong;
    }

    @Bindable
    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
    }

    @Bindable
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
