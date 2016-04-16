//package com.inf8405.projet_final.marathon;
//
///**
// * Created by youssef on 03/04/2016.
// */
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import java.security.PrivateKey;
//import java.util.ArrayList;
//import java.util.List;
//
//public class LocalDBManager extends SQLiteOpenHelper{
//    private static final int DATABASE_VERSION=1;
//    private static final String DATABASE_NAME="Marathon.db";
//    private static final String TABLE_PARTICIPANT="participant";
//    private static final String TABLE_MARATHON="marathon";
//    private static final String TABLE_MARATHON_HAS_PARTICIPANT="marathon_has_participant";
//    private static final String TABLE_POSITION= "position";
//
//    // table marathon colonne
//    public static final String idmarathon="idmarathon";
//    public static final String nom="nom";
//    public static final String marathon_date="marathon_date";
//    public static final String distance="distance";
//    public static final String position_depart="position_depart";
//    public static final String position_arrivee="position_arrivee";
//    /*public static final String temperature="temperature";
//    public static final String humidity="humidity";*/
//
//    // table has participant colonne
//
//
//    public static final String marathon_idmarathon="marathon_idmarathon";
//    public static final String participant_idparticipant="participant_idparticipant";
//    public static final String rank="rank";
//    public static final String average_speed="average_speed";
//    public static final String distance_run="distance_run";
//
//    // table participant
//    public static final String idparticipant="idparticipant";
//    public static final String courriel="courriel";
//    public static final String photo="photo";
//    public static final String position_idposition="position_idposition";
//    public static final String password="password";
//
//    // table position
//    public static final String idposition="idposition";
//    public static final String latitude="latitude";
//    public static final String longitude="longitude";
//    public static final String radius="radius";
//    public static final String position_time="position_time";
//    public static final String temperature="temperature";
//    public static final String humidity="humidity";
//    public static final String speed="speed";
//    public static final String date_position="date_position";
//
//    // element constant
//
//    public static final String PRIMARY_KEY="PRIMARY KEY";
//
//    // list des elements todo sera possiblement supprimer
//    private List<Participant> listParticipant=new ArrayList<Participant>();
//
//    // cette liste sera utilise pour mettre a jour la db apres reconnection
//    // pour ne pas avoir a prendre une list de chaque categorie a la fois
//    private List<Object> listAllElement = new ArrayList<Object>();
//    private List<String> listRequests=new ArrayList<String>();
//
//    public LocalDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
//        // charger les listes pour ne pas a faire beaucoup d'appel
//        this.getListParticipant();
//    }
//
//    public List<Object> getListAllElement() {
//        return listAllElement;
//    }
//
//    @Override
//
//    public void onCreate(SQLiteDatabase db) {
//        // creation de table
//
//        String createMarathonTable="CREATE TABLE IF NOT EXISTS" +
//                TABLE_MARATHON + "(" +
//                idmarathon + " varchar(255) NOT NULL," +
//                nom + " varchar(255) NOT NULL," +
//                marathon_date + " date NOT NULL,"+
//                distance + " double DEFAULT NULL,"+
//                position_depart+" text NOT NULL,"+
//                position_arrivee+" text NOT NULL,"+
//                temperature+" double NOT NULL,"+
//                humidity+" double NOT NULL,"+
//                PRIMARY_KEY+ " ("+ idmarathon+")" +
//                "UNIQUE KEY `idmarathon_UNIQUE` ("+idmarathon+")" +
//                ")";
//
//        String createMarathonHasParticipantTable= "CREATE TABLE IF NOT EXISTS" +
//                TABLE_MARATHON_HAS_PARTICIPANT + "(" +
//                marathon_idmarathon + " varchar(255) NOT NULL," +
//                participant_idparticipant + " varchar(255) NOT NULL," +
//                rank + " double DEFAULT NULL,"+
//                average_speed+" varchar(45) DEFAULT NULL,"+
//                distance_run+" double NOT NULL,"+
//                PRIMARY_KEY + "(" +marathon_idmarathon + "," + participant_idparticipant +"),"+
//                "KEY `fk_marathon_has_participant_participant1_idx` ("+participant_idparticipant+"),"+
//                "KEY `fk_marathon_has_participant_marathon1_idx` ("+marathon_idmarathon+")"+
//                ")";
//
//        String createParticipantTable="CREATE TABLE IF NOT EXISTS" +
//                TABLE_PARTICIPANT + "(" +
//                idparticipant + " varchar(255) NOT NULL," +
//                courriel + " varchar(255) NOT NULL," +
//                photo + " longtext,"+
//                position_idposition + " varchar(255) NOT NULL,"+
//                password+" varchar(255) NOT NULL,"+
//                PRIMARY_KEY+ " ("+ idparticipant+")" +
//                "UNIQUE KEY `icourriel_UNIQUE` ("+ idmarathon+ ")" +
//                "KEY `fk_participant_position_idx` ("+position_idposition+")"+
//                ")";
//        String createPositionTable = "CREATE TABLE IF NOT EXISTS" +
//                TABLE_POSITION + "(" +
//                idposition + " varchar(255) NOT NULL," +
//                latitude + " double NOT NULL," +
//                longitude + " double NOT NULL,"+
//                radius + " double NOT NULL,"+
//                position_time+" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"+
//                temperature + " double DEFAULT NULL,"+
//                humidity + " double DEFAULT NULL,"+
//                speed+" double DEFAULT NULL,"+
//                date_position + " date NOT NULL,"+
//                PRIMARY_KEY+ " ("+ idposition+")" +
//                ")";
//
//
//        db.execSQL(createMarathonTable);
//        db.execSQL(createMarathonHasParticipantTable);
//        db.execSQL(createParticipantTable);
//        db.execSQL(createPositionTable);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
//
//    // ajouter element pour tous, facilite les ajouts, par overload le compilateur choisira les bonnes methodes
//    public void ajouterElement(Participant participant)
//    {
//        ContentValues contentValues=new ContentValues();
//
//        contentValues.put(idparticipant, participant.getId());
//        contentValues.put(courriel, participant.getCourriel());
//        contentValues.put(photo,participant.getPhotoEn64());
//        contentValues.put(position_idposition, participant.getPosition().getId());
//        contentValues.put(password, participant.getPassword());
//
//        SQLiteDatabase db=this.getWritableDatabase();
//        db.insert(TABLE_PARTICIPANT, null, contentValues);
//        String requette="INSERT INTO `participant` (`idparticipant`, `courriel`, `photo`, `position_idposition`, `password`) VALUES " +
//                "('" + participant.getId() + "', '"
//                +participant.getCourriel()+  "', '"
//                +participant.getPhotoEn64() + "', '"
//                +participant.getPosition().getId()+"', '"
//                +participant.getPassword()+"')";
//        listRequests.add(requette);
//
//        db.close();
//
//    }
//
//    public void ajouterElement(Position position)
//    {
//        ContentValues contentValues=new ContentValues();
//
//        contentValues.put(idposition, position.getId());
//        contentValues.put(latitude, position.getLatitude());
//        contentValues.put(longitude, position.getLongitude());
//        contentValues.put(radius, position.getRadius());
//        contentValues.put(position_time,position.getTime());
//        contentValues.put(temperature,position.getTemperature());
//        contentValues.put(humidity,position.getHumidity());
//        contentValues.put(speed,position.getSpeed());
//        contentValues.put(date_position, position.getDay());
//
//        SQLiteDatabase db=this.getWritableDatabase();
//        db.insert(TABLE_POSITION, null, contentValues);
//        String requette="INSERT INTO `position` (`idposition`, `latitude`, `longitude`, `radius`, `position_time`, `temperature`, `humidity`, `speed`, `date_position`) VALUES " +
//                "('" + position.getId() + "', '"
//                +position.getLatitude()+ "', '"
//                +position.getLongitude()+  "', '"
//                +position.getRadius()+  "', '"
//                +position.getTime()+  "', '"
//                +position.getTemperature()+  "', '"
//                +position.getHumidity() + "', '"
//                +position.getSpeed()+"', '"
//                +position.getDay()+"')";
//        listRequests.add(requette);
//
//        db.close();
//
//    }
//
//    public boolean supprimerElement(Participant participant)
//    {   // todo colonne a choisir
//        String query="Select * FROM " + TABLE_RUNNNERS + " WHERE " + COLUMN1 + " = \"" + participant.getId()+ "\"";
//        boolean result=false;
//        SQLiteDatabase db=this.getWritableDatabase();
//        Cursor cursor=db.rawQuery(query, null);
//
//        if(cursor.moveToFirst())
//        {
//            String idElement=cursor.getString(0);
//            db.delete(TABLE_RUNNNERS, COLUMN1 + " = ?", new String[]{idElement});
//            // le supprimer de la liste actuelle
//            listParticipant.remove(participant);
//            listAllElement.remove(participant);
//            cursor.close();
//            result=true;
//        }
//        db.close();
//        return result;
//    }
//
//    public List<Participant> getListParticipant()
//    {
//        // partie appele apres l'initialisation,
//        // ajout et suppression deja pris en consideration dans les autres methodes
//        if(!listParticipant.isEmpty())
//            return listParticipant;
//
//        // partie appeler a l'initialisation
//        String query="Select * FROM " + TABLE_RUNNNERS;
//        SQLiteDatabase db=this.getWritableDatabase();
//        Cursor cursor=db.rawQuery(query,null);
//
//        if(cursor.moveToFirst())
//        {
//            while (!cursor.isAfterLast())
//            {
//                Participant participant= new Participant();
//                participant.setId(cursor.getString(0));
//                listParticipant.add(participant);
//                listAllElement.add(participant);
//            }
//        }
//        return listParticipant;
//    }
//}
