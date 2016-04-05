package com.inf8405.projet_final.marathon;

import java.util.UUID;

/**
 * Created by youssef on 04/04/2016.
 */
public class Temperature {
    private String id_ = new String();
    private Double temperature_ = new Double(0.0);
    private Double humidite_ = new Double(0.0);
    private boolean celcius_ = true;
    private UUID uidFormat_ = UUID.fromString("91c83b36-e25c-11e5-9730-9a79f06e9478");

    public Temperature()
    {
        id_=uidFormat_.randomUUID().toString();
    }

    public String getId() {
        return id_;
    }

    public void setId(String id) {
        this.id_ = id;
    }

    // temperature stocke en celcius
    public Double getTemperature() {
        if(celcius_)
        return temperature_;

        return (temperature_*1.8)+32;

    }

    public void setTemperature(Double temperature) {
        if(celcius_)
        {
            this.temperature_ = temperature;
        }
        else
        {
            this.temperature_=(temperature-32)/1.8;
        }
    }

    public Double getHumidite() {
        return humidite_;
    }

    public void setHumidite(Double humidite) {
        this.humidite_ = humidite;
    }

    public boolean isCelcius() {
        return celcius_;
    }

    public void setCelcius(boolean celcius) {
        this.celcius_ = celcius;
    }
}
