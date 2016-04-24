package com.inf8405.projet_final.marathon;

// INF8405 - Projet final
//Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    // creation de la base de donnees
    private static final String DATABASE_NAME = "marathon.db";
    private static final int DATABASE_VERSION = 1;

    //
    //            Structure de la table `position`
    //

    public static final String TABLE_POSITION = "position";
    public static final String IDPOSITION = "idposition";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String RADIUS = "radius";
    public static final String POSITION_TIME = "position_time";
    public static final String TEMPERATURE = "temperature";
    public static final String HUMIDITY = "humidity";
    public static final String SPEED = "speed";
    public static final String DATE_POSITION = "date_position";

    public static final String CREATE_POSITION = "CREATE TABLE IF NOT EXISTS " + TABLE_POSITION + "("
            + IDPOSITION + " varchar(255) NOT NULL,"
            + LATITUDE + "double NOT NULL,"
            + LONGITUDE + " double NOT NULL,"
            + RADIUS + " double NOT NULL,"
            + POSITION_TIME + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
            + TEMPERATURE + " double DEFAULT NULL,"
            + HUMIDITY + " double DEFAULT NULL,"
            + SPEED + " double DEFAULT NULL,"
            + DATE_POSITION + " date NOT NULL,"
            + "PRIMARY KEY (" + IDPOSITION + ")"
            + ")";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_POSITION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSITION);
        onCreate(db);
    }
}
