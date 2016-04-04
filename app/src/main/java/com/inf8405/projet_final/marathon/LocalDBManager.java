package com.inf8405.projet_final.marathon;

/**
 * Created by youssef on 03/04/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class LocalDBManager extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="Marathon.db";
    private static final String TABLE_RUNNNERS="participants";

    public static final String COLUMN1="";
    public static final String COLUMN2="";
    public static final String COLUMN3="";

    // list des elements
    List<Participant> listParticipant=new ArrayList<Participant>();


    public LocalDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        // charger les listes pour ne pas a faire beaucoup d'appel
        this.getListParticipant();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creation de table
        // TODO creatyion des autres tables et verifications des noms des colomns
        String createRunnersTable="CREATE TABLE IF NOT EXISTS" +
                TABLE_RUNNNERS + "(" + COLUMN1 + " TEXT," + COLUMN2 + " TEXT," + COLUMN3+" TEXT)";

        db.execSQL(createRunnersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // ajouter element pour tous, facilite les ajouts, par overload le compilateur choisira les bonnes methodes
    public void ajouterElement(Participant participant)
    {
        ContentValues contentValues=new ContentValues();

        contentValues.put(COLUMN1,"");
        contentValues.put(COLUMN2,"");
        contentValues.put(COLUMN3, "");

        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(TABLE_RUNNNERS, null, contentValues);
        // l'ajouter a la list actuelle
        listParticipant.add(participant);
        db.close();

    }

    public boolean supprimerElement(Participant participant)
    {   // todo colonne a choisir
        String query="Select * FROM " + TABLE_RUNNNERS + " WHERE " + COLUMN1 + " = \"" + participant.getId()+ "\"";
        boolean result=false;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query, null);

        if(cursor.moveToFirst())
        {
            String idElement=cursor.getString(0);
            db.delete(TABLE_RUNNNERS, COLUMN1 + " = ?", new String[]{idElement});
            // le supprimer de la liste actuelle
            listParticipant.remove(participant);
            cursor.close();
            result=true;
        }
        db.close();
        return result;
    }

    public List<Participant> getListParticipant()
    {
        // partie appele apres l'initialisation,
        // ajout et suppression deja pris en consideration dans les autres methodes
        if(!listParticipant.isEmpty())
            return listParticipant;

        // partie appeler a l'initialisation
        String query="Select * FROM " + TABLE_RUNNNERS;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query,null);

        if(cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                Participant participant= new Participant();
                participant.setId(cursor.getString(0));
                listParticipant.add(participant);
            }
        }
        return listParticipant;
    }
}
