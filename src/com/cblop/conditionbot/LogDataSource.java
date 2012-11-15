package com.cblop.conditionbot;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LogDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_TYPE, MySQLiteHelper.COLUMN_PRACTICE, MySQLiteHelper.COLUMN_REPS };

    public LogDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Log createLog(String name, String type, String practice, int reps) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_TYPE, type);
        values.put(MySQLiteHelper.COLUMN_PRACTICE, practice);
        values.put(MySQLiteHelper.COLUMN_REPS, reps);

        long insertId = database.insert(MySQLiteHelper.TABLE_LOG, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_LOG,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Log newLog = cursorToLog(cursor);
        cursor.close();
        return newLog;
    }

    public void deleteLog(Log log) {
        long id = log.getId();
        System.out.println("Log deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_LOG, MySQLiteHelper.COLUMN_ID
            + " = " + id, null);
    }

    public List<Log> getAllLogs() {
        List<Log> logs = new ArrayList<Log>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_LOG, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log log = cursorToLog(cursor);
            logs.add(log);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return logs;
    }

    public List<Log> getLogsByType(String type) {

        List<Log> logs = new ArrayList<Log>();
        String[] column = {MySQLiteHelper.COLUMN_TYPE};
        String whereString = "type = '" + type + "'";

        Cursor cursor = database.query(MySQLiteHelper.TABLE_LOG, allColumns, whereString, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log log = cursorToLog(cursor);
            logs.add(log);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return logs;


    }

    private Log cursorToLog(Cursor cursor) {
        Log log = new Log();
        log.setId(cursor.getLong(0));
        log.setName(cursor.getString(1));
        log.setType(cursor.getString(2));
        log.setPractice(cursor.getString(3));
        log.setReps(cursor.getInt(4));
        return log;
    }
}
