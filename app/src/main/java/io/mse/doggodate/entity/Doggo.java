package io.mse.doggodate.entity;



import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import java.util.ArrayList;

public class Doggo extends BaseObservable {

    private String name;
    private String breed;
    private String id;
    private static Context context;
    private int profilePicInt;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private ArrayList<String> photos = new ArrayList<>();
    private ArrayList<Long> photosLong = new ArrayList<>();
    private String profilePic;
    private boolean active;

    public Doggo(String name, String breed, String profilePic) {
        this.name = name;
        this.breed = breed;
        this.profilePic = profilePic;
        photos = new ArrayList<>();
    }
    public Doggo(String name, String breed, boolean active) {
        this.name = name;
        this.breed = breed;
        this.active = active;
        photos = new ArrayList<>();
    }

    public Doggo() {
        photos = new ArrayList<>();
    }

    public ArrayList<DoggoEvent> getEvents() {

        return new ArrayList<DoggoEvent>();
    }

    public void setEvents(ArrayList<DoggoEvent> events) {

        //this.events = events;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static void setContextStatic(Context c){
        Doggo.context = c;
    }
    @Bindable
    public int getProfilePicInt() {

        return this.profilePicInt;
    }

    @BindingAdapter("android:src")
    public void setProfilePicInt(ImageView imageView, int resource) {
        this.profilePicInt = resource;

        imageView.setImageResource(resource);


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

        return new ArrayList<Doggo>();
    }

    public void setFollowers(ArrayList<Doggo> followers) {

        //this.followers = followers;
    }

    public ArrayList<Doggo> getFollowings() {

        return new ArrayList<Doggo>();
    }

    public void setFollowings(ArrayList<Doggo> followings) {

        //this.followings = followings;
    }

    @Bindable
    public ArrayList<String> getPhotos() {
        return photos;
    }

    @Bindable
    public String getProfilePic() {
        return profilePic;
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
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
        this.profilePicInt = context.getResources().getIdentifier( profilePic, "drawable", "io.mse.doggodate");

    }

    @Bindable
    public void setPhotos(ArrayList<String> photos) {
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
