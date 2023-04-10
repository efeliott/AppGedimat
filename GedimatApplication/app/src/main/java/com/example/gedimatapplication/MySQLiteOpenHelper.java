package com.example.gedimatapplication;

/**
 * Create by Eliott Fertille on 14/02/2023
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "dbGedimatApp.db";
    private static final int DB_VERSION = 1;

    private static MySQLiteOpenHelper instance;

    public MySQLiteOpenHelper(@Nullable Context context) {
        super(context, "dbGedimatApp.db", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // création de la base de donnée embarquée
        // création de la table Realisation
        String req = "CREATE TABLE Realisation("
                +"id_real TEXT NOT NULL PRIMARY KEY, "
                +"titre TEXT NOT NULL, "
                +"description TEXT NOT NULL,"
                +"nb_gaimes INTEGER DEFAULT 0"
                +")";

        db.execSQL( req );

        // création de la table Votant
        req = "CREATE TABLE Votant("
                +"id_votant INTEGER NOT NULL PRIMARY KEY,"
                +"email TEXT NOT NULL,"
                +"codeParticipant TEXT NOT NULL,"
                +"date_consentement TEXT NOT NULL,"
                +"vote_1 TEXT NOT NULL,"
                +"vote_2 TEXT NOT NULL,"
                +"vote_3 TEXT NOT NULL,"
                +"numero_ticket TEXT NOT NULL"
                +")";

        db.execSQL(req);
        Log.i("DATABASE", "onCreate invoked");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Realisation");
        db.execSQL("DROP TABLE IF EXISTS Votant");
        onCreate(db);
        Log.i("DATABASE", "onUpgrade invoked");
    }
}