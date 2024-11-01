package com.rp_grf.jrmadeiras.Utils;

/**
 * Autor: André Castro
 * Data de criação: 04/02/2021
 */

public class Coordenadas {

    Double latitude;
    Double longitude;
    String registro;

    public Coordenadas() {
    }

    public Coordenadas(Double latitude, Double longitude, String registro) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.registro = registro;
    }

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

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }
}
