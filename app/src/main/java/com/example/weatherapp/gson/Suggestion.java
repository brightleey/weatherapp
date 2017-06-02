package com.example.weatherapp.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/5/12.
 */

public class Suggestion {
    @SerializedName("comf")
    public ComfortIndex comfortIndex;
    @SerializedName("cw")
    public CarWashIndex carWashIndex;
    @SerializedName("drsg")
    public DressIndex dressIndex;
    @SerializedName("flu")
    public ColdIndex coldIndex;
    @SerializedName("sport")
    public SportIndex sportIndex;
    @SerializedName("trav")
    public TravelIndex travelIndex;
    @SerializedName("uv")
    public UVIndex uvIndex;
    public class ComfortIndex{
        @SerializedName("brf")
        public String shortDes;
        @SerializedName("txt")
        public String detailTxt;
    }
    public class CarWashIndex{
        @SerializedName("brf")
        public String shortDes;
        @SerializedName("txt")
        public String detailTxt;
    }
    public class AirIndex{
        @SerializedName("brf")
        public String shortDes;
        @SerializedName("txt")
        public String detailTxt;
    }
    public class DressIndex{
        @SerializedName("brf")
        public String shortDes;
        @SerializedName("txt")
        public String detailTxt;
    }
    public class ColdIndex{
        @SerializedName("brf")
        public String shortDes;
        @SerializedName("txt")
        public String detailTxt;
    }
    public class SportIndex{
        @SerializedName("brf")
        public String shortDes;
        @SerializedName("txt")
        public String detailTxt;
    }
    public class TravelIndex{
        @SerializedName("brf")
        public String shortDes;
        @SerializedName("txt")
        public String detailTxt;
    }
    public class UVIndex{
        @SerializedName("brf")
        public String shortDes;
        @SerializedName("txt")
        public String detailTxt;
    }
}
