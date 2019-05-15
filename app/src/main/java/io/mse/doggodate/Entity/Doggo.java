package io.mse.doggodate.Entity;

import java.util.ArrayList;

public class Doggo {

    private String name;
    private String breed;
    private ArrayList<Doggo> followers = new ArrayList<>();
    private ArrayList<Doggo> followings = new ArrayList<>();
    private ArrayList<Integer> photos = new ArrayList<>();
    private int profilePic;

    public Doggo(String name, String breed, int profilePic) {
        this.name = name;
        this.breed = breed;
        this.profilePic = profilePic;
    }

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
}
