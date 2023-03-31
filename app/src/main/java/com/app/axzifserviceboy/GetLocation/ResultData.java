package com.app.axzifserviceboy.GetLocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultData {
    public String formatted_address;
    @SerializedName("geometry")
    @Expose
    public GeometryData geometry;
    public String place_id;

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public GeometryData getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryData geometry) {
        this.geometry = geometry;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }
}
