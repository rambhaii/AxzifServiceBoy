package com.app.axzifserviceboy.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseList {
    @SerializedName("data")
    @Expose
    private ArrayList<Datum> data;

    public ArrayList<Datum> getData()
    {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }

    @SerializedName("status")
    @Expose
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    @Expose
    private String message;
}
