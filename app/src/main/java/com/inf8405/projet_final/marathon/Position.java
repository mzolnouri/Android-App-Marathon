package com.inf8405.projet_final.marathon;

/**
 * Created by youssef on 04/04/2016.
 */
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.util.UUID;

public class Position {
    private String id_=new String();
    private double longitude_=0.0;
    private double latitude_=0.0;
    private double radius_=0.0;
    private String adresse_=new String();
    private Double temperature=new Double(0.0);
    private Double humidity = new Double(0.0);
    private Double speed = new Double(0.0);
    private Date date_;

    private UUID uidFormat_ = UUID.fromString("91c83b36-e25c-11e5-9730-9a79f06e9478");

    public Position()
    {
        id_=uidFormat_.randomUUID().toString();
        date_= Calendar.getInstance().getTime();
    }
    public Position(double latitude, double longitude) {
        longitude_=longitude;
        latitude_=latitude;
        date_= Calendar.getInstance().getTime();
    }

    public String getId() {
        return id_;
    }

    public void setId(String id) {
        this.id_ = id;
    }

    public double getLongitude() {
        return longitude_;
    }

    public void setLongitude(double longitude) {
        this.longitude_ = longitude;
    }

    public double getLatitude() {
        return latitude_;
    }

    public void setLatitude(double latitude) {
        this.latitude_ = latitude;
    }

    public Date getDate()
    {
        return date_;
    }

    public void setActualDateAndTime()
    {
        date_= Calendar.getInstance().getTime();
    }
    public void setDate(String date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        try {
            date_=sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public String getDateString()
    {
        DateFormat df = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        return  df.format(date_);
    }

    public double getRadius() {
        return radius_;
    }

    public void setRadius(double radius) {
        this.radius_ = radius;
    }

    public String getAdresse() {
        return adresse_;
    }

    public void setAdresse(String adresse) {
        this.adresse_ = adresse;
    }

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

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

}
