package com.cswala.cswala.Models;

import com.google.gson.annotations.SerializedName;

public class Results {
    @SerializedName("name")
    private String name;

    @SerializedName("url")
    private String url;

    @SerializedName("start_time")
    private String start_time;

    @SerializedName("end_time")
    private String end_time;

    @SerializedName("status")
    private String status;

    @SerializedName("duration")
    private String  duration;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getStatus() {
        return status;
    }

    public String  getDuration() {
        return duration;
    }

}
