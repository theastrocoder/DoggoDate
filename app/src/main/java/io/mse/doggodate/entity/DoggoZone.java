package io.mse.doggodate.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseMethod;

import java.io.Serializable;

import io.mse.doggodate.R;

public class DoggoZone extends BaseObservable {

    private String name;
    private String area;
    private String fence;
    private String typ;
    private boolean isFavorite;
    private String id;

    public DoggoZone(String name, String area, String fence, String typ, boolean isFavorite) {
        this.name = name;
        this.area = area;
        this.fence = fence;
        this.typ = typ;
        this.isFavorite = isFavorite;
    }

    public DoggoZone(){

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

    @Bindable
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Bindable
    public String getFence() {
        return fence;
    }

    public void setFence(String fence) {
        this.fence = fence;
    }

    @Bindable
    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
