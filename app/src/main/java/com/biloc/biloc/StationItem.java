package com.biloc.biloc;

import com.google.android.gms.maps.model.LatLng;

public class StationItem {
    private String name;
    private String stationCity;
    private LatLng coordinates;
    private int availableBikeNumber;
    private int freeSlotNumber;
    private String address;
    private double distance;
    private boolean favorite=false;

    String getStationName(){
        return name;
    }

    void setStationName(String stationName){
        this.name=stationName;
    }

    String getStationCity(){
        return stationCity;
    }

    void setStationCity(String stationCity){
        this.stationCity=stationCity;
    }

    int getNumberOfBike(){
        return availableBikeNumber;
    }

    void setNumberOfBike(int numberOfBike){
        this.availableBikeNumber=numberOfBike;
    }

    LatLng getCoordinates() { return coordinates; }

    void setCoordinates(LatLng coordinates) { this.coordinates = coordinates; }

    int getFreeSlotNumber() {
        return freeSlotNumber;
    }

    void setFreeSlotNumber(int freeSlotNumber) {
        this.freeSlotNumber = freeSlotNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    double getDistance() {
        return distance;
    }

    void setDistance(double distance) {
        this.distance = distance;
    }

    void setFavorite(boolean isFavorite) {
        this.favorite = isFavorite;
    }

    boolean getFavorite() { return this.favorite; }

}
