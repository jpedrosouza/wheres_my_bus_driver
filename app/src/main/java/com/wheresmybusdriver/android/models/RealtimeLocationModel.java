package com.wheresmybusdriver.android.models;

public class RealtimeLocationModel {
    public String company_id;
    public String driver_id;
    public String lat;
    public String lng;
    public String route_id;

    public RealtimeLocationModel() {

    }

    public RealtimeLocationModel(String company_id, String driver_id, String lat, String lng,
                                 String route_id) {
        this.company_id = company_id;
        this.driver_id = driver_id;
        this.lat = lat;
        this.lng = lng;
        this.route_id = route_id;
    }
}
