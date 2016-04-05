package com.inf8405.projet_final.marathon;

import java.util.UUID;

/**
 * Created by youssef on 03/04/2016.
 */
public class Participant {

    private String id;
    private String nom_=new String();
    private String courriel_=new String();
    private String password_=new String();
    private UUID uidFormat_ = UUID.fromString("91c83b36-e25c-11e5-9730-9a79f06e9478");
    private Position position_=new Position();
    private double distanceParcourue_=0.0;
    private double vitesse_=0.0;
    private double vitesseMoyenne_=0.0;
    private int classement_=0;

    public Participant()
    {
        id=uidFormat_.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Position getPosition() {
        return position_;
    }

    public void setPosition(Position position) {
        this.position_ = position;
    }

    public double getDistanceParcourue() {
        return distanceParcourue_;
    }

    public void setDistanceParcourue(double distanceParcourue) {
        this.distanceParcourue_ = distanceParcourue;
    }

    public double getVitesse() {
        return vitesse_;
    }

    public void setVitesse(double vitesse) {
        this.vitesse_ = vitesse;
    }

    public double getVitesseMoyenne() {
        return vitesseMoyenne_;
    }

    public void setVitesseMoyenne(double vitesseMoyenne) {
        this.vitesseMoyenne_ = vitesseMoyenne;
    }

    public String getNom() {
        return nom_;
    }

    public void setNom(String nom) {
        this.nom_ = nom;
    }

    public String getCourriel() {
        return courriel_;
    }

    public void setCourriel(String courriel) {
        this.courriel_ = courriel;
    }

    public String getPassword() {
        return password_;
    }

    public void setPassword(String password) {
        this.password_ = password;
    }

    public int getClassement() {
        return classement_;
    }

    public void setClassement(int classement) {
        this.classement_ = classement;
    }
}
