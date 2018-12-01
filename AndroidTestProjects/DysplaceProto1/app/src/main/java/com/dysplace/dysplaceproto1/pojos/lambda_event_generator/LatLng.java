package com.dysplace.dysplaceproto1.pojos.lambda_event_generator;

public class LatLng {

    private String lat;
    private String lng;

    public LatLng() {}

    public LatLng(String lat, String lng){
        this.setLat(lat);
        this.setLng(lng);
    }

    // Getters

    public String getLat() {
        return this.lat;
    }

    public String getLng() {
        return this.lng;
    }

    //Setters

    private void setLat(String lat) {
        this.lat = lat;
    }

    private void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Latitude: "+lat+", Longitude: "+lng;
    }

}
