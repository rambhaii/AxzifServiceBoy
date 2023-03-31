package com.app.axzifserviceboy.GetLocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeometryData
{
    @SerializedName("location")
    @Expose
    public LocationData location;
    public String location_type;

    public LocationData getLocation() {
        return location;
    }

    public void setLocation(LocationData location) {
        this.location = location;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }
}
