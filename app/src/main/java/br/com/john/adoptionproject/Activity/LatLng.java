package br.com.john.adoptionproject.Activity;

/**
 * Created by Juliana on 08/12/2017.
 */

public class LatLng {

    private Double latitude;
    private Double longitude;

    public LatLng() {}

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LatLng{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

}
