package com.biloc.biloc;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by manon.racine1 on 05.11.2017.
 */

public class StationItem {
    private String name;
    private String stationCity;
    private LatLng coordinates;
    private int availableBikeNumber;
    private int freeSlotNumber;
    private String address;
    private double distance;

    public String getStationName(){
        return name;
    }

    public void setStationName(String stationName){
        this.name=stationName;
    }

    public String getStationCity(){
        return stationCity;
    }

    public void setStationCity(String stationCity){
        this.stationCity=stationCity;
    }

    public int getNumberOfBike(){
        return availableBikeNumber;
    }

    public void setNumberOfBike(int numberOfBike){
        this.availableBikeNumber=numberOfBike;
    }

    public LatLng getCoordinates() { return coordinates; }

    public void setCoordinates(LatLng coordinates) { this.coordinates = coordinates; }

    public int getFreeSlotNumber() {
        return freeSlotNumber;
    }

    public void setFreeSlotNumber(int freeSlotNumber) {
        this.freeSlotNumber = freeSlotNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDistance() {

        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
