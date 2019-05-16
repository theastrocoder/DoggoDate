package io.mse.doggodate.Entity;

public class DoggoZone {

    private double Latitude;
    private double Longitude;
    private String name;
    private Integer surface;
    private boolean fence;

    public DoggoZone(double latitude, double longitude, String name, Integer surface, boolean fence) {
        Latitude = latitude;
        Longitude = longitude;
        this.name = name;
        this.surface = surface;
        this.fence = fence;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSurface() {
        return surface;
    }

    public void setSurface(Integer surface) {
        this.surface = surface;
    }

    public boolean isFence() {
        return fence;
    }

    public void setFence(boolean fence) {
        this.fence = fence;
    }
}
