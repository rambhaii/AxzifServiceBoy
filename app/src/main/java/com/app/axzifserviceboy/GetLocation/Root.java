package com.app.axzifserviceboy.GetLocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Root
{


    @SerializedName("results")
    @Expose
    public ArrayList<ResultData> results;
    public String status;

    public ArrayList<ResultData> getResults()
    {
        return results;
    }

    public void setResults(ArrayList<ResultData> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
