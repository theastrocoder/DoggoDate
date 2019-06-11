package io.mse.doggodate.entity;



import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.ArrayList;
import java.util.HashMap;

public class DoggoPOJO extends BaseObservable {

    private String id;
    private String name;
    private String breed;
    private int profilePic;
    private boolean active;
    private ArrayList<Integer> photos = new ArrayList<>();
    private ArrayList<DoggoPOJO> followers = new ArrayList<>();
    private ArrayList<DoggoPOJO> followings = new ArrayList<>();

    public DoggoPOJO(String name, String breed, int profilePic) {
        this.name = name;
        this.breed = breed;
        this.profilePic = profilePic;
    }
    public DoggoPOJO(String name, String breed, boolean active) {
        this.name = name;
        this.breed = breed;
        this.active = active;
    }

    public DoggoPOJO() {

    }

    public ArrayList<DoggoPOJO> getFollowings() {
        return followings;
    }

    public void setFollowings(ArrayList<DoggoPOJO> followings) {
        this.followings = followings;
    }

    public ArrayList<DoggoPOJO> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<DoggoPOJO> followers) {
        this.followers = followers;
    }

    public ArrayList<Integer> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Integer> photos) {
        this.photos = photos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Bindable
    public int getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
    }

    @Bindable
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
