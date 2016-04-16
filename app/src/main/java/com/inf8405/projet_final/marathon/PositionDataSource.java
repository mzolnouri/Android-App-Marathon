package com.inf8405.projet_final.marathon;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PositionDataSource {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = {SQLiteHelper.IDPOSITION,
            SQLiteHelper.LATITUDE,
            SQLiteHelper.LONGITUDE,
            SQLiteHelper.RADIUS,
            SQLiteHelper.POSITION_TIME,
            SQLiteHelper.TEMPERATURE,
            SQLiteHelper.HUMIDITY,
            SQLiteHelper.SPEED,
            SQLiteHelper.DATE_POSITION
    };


    public PositionDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public int UpdatePosition(Position position) {
        ContentValues cv = new ContentValues();
        cv.put("idposition", position.getId());
        cv.put("latitude", position.getLatitude());
        cv.put("longitude", position.getLongitude());
        cv.put("radius", position.getRadius());
        cv.put("position_time", position.getTime());
        cv.put("temperature", position.getTemperature());
        cv.put("humidity", position.getHumidity());
        cv.put("speed", position.getSpeed());
        cv.put("date_position", position.getDay());

        String[] selectionArgs = {String.valueOf(position.getId())};
        int count = database.update(SQLiteHelper.TABLE_POSITION, cv, "idposition=" + position.getId(), selectionArgs);
        return count;

    }


    public long InsertPosition(Position position) {
        ContentValues cv = new ContentValues();
        cv.put("idposition", position.getId());
        cv.put("latitude", position.getLatitude());
        cv.put("longitude", position.getLongitude());
        cv.put("radius", position.getRadius());
        cv.put("position_time", position.getTime());
        cv.put("temperature", position.getTemperature());
        cv.put("humidity", position.getHumidity());
        cv.put("speed", position.getSpeed());
        cv.put("date_position", position.getDay());
        long newRowId;

        newRowId = database.insert(SQLiteHelper.TABLE_POSITION, null, cv);
        return newRowId;

    }


    public List<Position> getAllComments() {
        List<Position> positions = new ArrayList<Position>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_POSITION,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Position position = cursorToPosition(cursor);
            positions.add(position);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return positions;
    }

    private Position cursorToPosition(Cursor cursor) {
        Position position = new Position();

        position.setId(cursor.getString(0));
        position.setLatitude(cursor.getDouble(1));
        position.setLongitude(cursor.getDouble(2));
//        position.setRadius(cursor.getString(3));
        position.setRadius(10);
        position.setTime(cursor.getString(4));
        position.setTemperature(cursor.getDouble(5));
        position.setHumidity(cursor.getDouble(6));
        position.setSpeed(cursor.getDouble(7));
        position.setDay(cursor.getString(8));
        return position;
    }


}
