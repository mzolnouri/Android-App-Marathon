package com.inf8405.projet_final.marathon;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by youssef on 04/04/2016.
 */
public class Marathon {
    private String id_;
    private String nom_;
    private Position positionDepart_=new Position();
    private Position positionArrivee_=new Position();
    private Double distance_=new Double(0.0);
    private int nbParticipant_=0;
    private UUID uidFormat_ = UUID.fromString("91c83b36-e25c-11e5-9730-9a79f06e9478");
    private Date date_;

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

    public Position getPositionDepart() {
        return positionDepart_;
    }

    public void setPositionDepart(Position positionDepart) {
        this.positionDepart_ = positionDepart;
    }

    public Position getPositionArrivee() {
        return positionArrivee_;
    }

    public void setPositionArrivee(Position positionArrivee) {
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
