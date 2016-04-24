package com.inf8405.projet_final.marathon;

// INF8405 - Projet final
//Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class Participant {

    private String id;
    private String nom_=new String();
    private String courriel_=new String();
    private String password_=new String();
    private UUID uidFormat_ = UUID.fromString("91c83b36-e25c-11e5-9730-9a79f06e9478");
    private Position position_=new Position();
    private double distanceParcourue_=0.0;
    private int classement_=0;

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    private double averageSpeed =0;
    private Bitmap photoEnBitMap_;
    private String photoEn64_ = new String();

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    private int rank =99;

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
    public String getIdPosition() {
        return position_.getId();
    }

    public void setPosition(Position position) {
        this.position_ = position;
    }

    public void setIdPosition(String idPosition) {
        this.position_.setId(idPosition);
    }
    public double getDistanceParcourue() {
        return distanceParcourue_;
    }

    public void setDistanceParcourue(double distanceParcourue) {
        this.distanceParcourue_ = distanceParcourue;
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

    public Bitmap getPhotoEnBitmap(){
        return photoEnBitMap_;
    }

    public void setPhotoEnBitmap(Bitmap photo){
        this.photoEnBitMap_ = photo;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        photoEn64_= Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    public void setPhotoEn64(String photoEn64) {
        byte[] decodedString = Base64.decode(photoEn64, Base64.DEFAULT);
        photoEnBitMap_ = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        this.photoEn64_ = photoEn64;
    }
    public String getPhotoEn64() {
        return photoEn64_;
    }
}
