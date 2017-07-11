package com.sras.sras_androidclient.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ServerDBHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "servers.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SERVERS_TABLE_NAME = "servers";
    private static final String SERVERS_COLUMN_ID = "_id";
    private static final String SERVERS_COLUMN_NAME = "server_name";
    private static final String SERVERS_COLUMN_ADDRESS = "server_address";
    private static final String SERVERS_COLUMN_PORT = "server_port";
    private static final String SERVERS_COLUMN_USERNAME = "server_username";
    private static final String SERVERS_COLUMN_PASSWORD = "server_password";


    public ServerDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + SERVERS_TABLE_NAME + "(" +
                SERVERS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                SERVERS_COLUMN_NAME + " TEXT, " +
                SERVERS_COLUMN_ADDRESS + " TEXT, " +
                SERVERS_COLUMN_PORT + " INT, " +
                SERVERS_COLUMN_USERNAME + " TEXT, " +
                SERVERS_COLUMN_PASSWORD + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop existing route table and add the new one when upgrading
        db.execSQL("DROP TABLE IF EXISTS " + SERVERS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertServer(String name, String address, int port, String username, String password)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SERVERS_COLUMN_NAME, name);
        contentValues.put(SERVERS_COLUMN_ADDRESS, address);
        contentValues.put(SERVERS_COLUMN_PORT, port);
        contentValues.put(SERVERS_COLUMN_USERNAME, username);
        contentValues.put(SERVERS_COLUMN_PASSWORD, password);
        db.insert(SERVERS_TABLE_NAME, null, contentValues);

        return true;
    }

    public boolean updateServer(Integer id, String name, String address, int port, String username, String password)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SERVERS_COLUMN_NAME, name);
        contentValues.put(SERVERS_COLUMN_ADDRESS, address);
        contentValues.put(SERVERS_COLUMN_PORT, port);
        contentValues.put(SERVERS_COLUMN_USERNAME, username);
        contentValues.put(SERVERS_COLUMN_PASSWORD, password);
        db.update(SERVERS_TABLE_NAME, contentValues, SERVERS_COLUMN_ID + " = ? ", new String[]
                {Integer.toString(id)});

        return true;
    }

    public boolean updateServerWithUserAndPass(Integer id, String username, String password)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SERVERS_COLUMN_USERNAME, username);
        contentValues.put(SERVERS_COLUMN_PASSWORD, password);
        db.update(SERVERS_TABLE_NAME, contentValues, SERVERS_COLUMN_ID + " = ? ", new String[]
                {Integer.toString(id)});

        return true;
    }

    public Cursor getServer(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "SELECT * FROM " + SERVERS_TABLE_NAME + " WHERE "
                + SERVERS_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
    }

    public Cursor getAllServers()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "SELECT * FROM " + SERVERS_TABLE_NAME, null );
    }

    public Integer deleteServer(Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SERVERS_TABLE_NAME, SERVERS_COLUMN_ID
                + " = ? ", new String[] { Integer.toString(id)} );
    }
}