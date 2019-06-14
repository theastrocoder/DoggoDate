package io.mse.doggodate.entity;

import com.google.firebase.firestore.GeoPoint;

public class DoggoZone {

    private GeoPoint location;
    private String name;
    private String area;
    private String fence;
    private String typ;
    private String id;

    public DoggoZone(GeoPoint location, String name, String area, String fence, String typ) {
        this.location = location;
        this.name = name;
        this.area = area;
        this.fence = fence;
        this.typ = typ;
    }

    public DoggoZone(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFence() {
        return fence;
    }

    public void setFence(String fence) {
        this.fence = fence;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }
}
