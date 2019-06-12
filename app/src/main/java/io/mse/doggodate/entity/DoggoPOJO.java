package io.mse.doggodate.entity;



import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.ArrayList;
import java.util.HashMap;

public class DoggoPOJO extends BaseObservable {

    private String id;
    private String name;
    private String breed;
    private String profilePic;
    private boolean active;
    private ArrayList<String> photos;

    public DoggoPOJO(String name, String breed, String profilePic) {
        this.name = name;
        this.breed = breed;
        this.profilePic = profilePic;
        this.active = false;
        this.photos = new ArrayList<>();
    }


    @Bindable
    public ArrayList<String> getPhotos() {
        return photos;
    }

    @Bindable
    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    @Bindable
    public String getId() {
        return id;
    }

    @Bindable
    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public String getBreed() {
        return breed;
    }

    @Bindable
    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Bindable
    public String getProfilePic() {
        return profilePic;
    }

    @Bindable
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @Bindable
    public boolean isActive() {
        return active;
    }

    @Bindable
    public void setActive(boolean active) {
        this.active = active;
    }
}
