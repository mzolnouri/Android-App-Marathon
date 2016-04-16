package com.inf8405.projet_final.marathon;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by youssef on 04/04/2016.
 */
public class Marathon {
    private String id_;
    private String nom_;
    private String positionDepart_=new String();
    private String positionArrivee_=new String();
    private Double distance_=new Double(0.0);
    private int nbParticipant_=0;
    private UUID uidFormat_ = UUID.fromString("91c83b36-e25c-11e5-9730-9a79f06e9478");

    public Date getDate_() {
        return date_;
    }
    public String getDateString() {
        DateFormat df = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        return df.format(date_);
    }
    public void setDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        try {
            date_ = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void setDate_(Date date_) {
        this.date_ = date_;
    }

    private Date date_;

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }

    private Double temperature = 0.0;
    private Double humidity = 0.0;
    private boolean actual = true;

    public Marathon()
    {
        id_=uidFormat_.randomUUID().toString();
        date_= Calendar.getInstance().getTime();
    }

    public String getId() {
        return id_;
    }

    public void setId(String id) {
        this.id_ = id;
    }

    public String getNom() {
        return nom_;
    }

    public void setNom(String nom) {
        this.nom_ = nom;
    }

    public String getPositionDepart() {
        return positionDepart_;
    }

    public void setPositionDepart(String positionDepart) {
        this.positionDepart_ = positionDepart;
    }

    public String getPositionArrivee() {
        return positionArrivee_;
    }

    public void setPositionArrivee(String positionArrivee) {
        this.positionArrivee_ = positionArrivee;
    }

    public Double getDistance() {
        return distance_;
    }

    public void setDistance(Double distance) {
        this.distance_ = distance;
    }

    public int getNbParticipant() {
        return nbParticipant_;
    }

    public void setNbParticipant(int nbParticipant) {
        this.nbParticipant_ = nbParticipant;
    }
}
