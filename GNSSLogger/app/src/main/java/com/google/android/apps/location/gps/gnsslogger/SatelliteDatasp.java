package com.sgac.abdelmoula.skyplot;

import java.io.Serializable;

/**
 * Created by uiseok on 2016-09-25.
 */

public class SatelliteDatasp implements Serializable{

    private String prn;
    private String elev_in_degree;
    private String azimuth;
    private int snr;

    public String getPrn() {
        return prn;
    }

    public void setPrn(String prn) {
        this.prn = prn;
    }

    public String getElev_in_degree() {
        return elev_in_degree;
    }

    public void setElev_in_degree(String elev_in_degree) {
        this.elev_in_degree = elev_in_degree;
    }

    public String getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(String azimuth) {
        this.azimuth = azimuth;
    }

    public int getSnr() {
        return snr;
    }

    public void setSnr(int snr) {
        this.snr = snr;
    }
}
