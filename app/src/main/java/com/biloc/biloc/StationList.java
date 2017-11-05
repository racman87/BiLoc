package com.biloc.biloc;

/**
 * Created by manon.racine1 on 05.11.2017.
 */

public class StationList {
    private String stationName;
    private String numberOfBike;
    private String stationDistance;

    public String getStationName(){
        return stationName;
    }

    public void setStationName(String stationName){
        this.stationName=stationName;
    }

    public String getNumberOfBike(){
        return numberOfBike;
    }

    public void setNumberOfBike(String numberOfBike){
        this.numberOfBike=numberOfBike;
    }

    public String getStationDistance(){
        return stationDistance;
    }

    public void setStationDistance(String stationDistance){
        this.stationDistance=stationDistance;
    }
}
