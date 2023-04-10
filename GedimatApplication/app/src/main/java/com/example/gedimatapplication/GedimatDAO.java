package com.example.gedimatapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GedimatDAO {
    private SQLiteDatabase db;
    private MySQLiteOpenHelper Helper;
    private PreVoteActivity preVote;


    public GedimatDAO(Context context){
        Helper = new MySQLiteOpenHelper(context);
        db = Helper.getWritableDatabase();
    }

    public Cursor touteLesReal() {
        Cursor curseurContact = db.rawQuery("SELECT id_real, titre, description from Realisation" , new String[] {});
        return curseurContact;
    }

    public void supprimerTous() {
        db.delete("Realisation",null,null);
    }

    // Méthode qui ajoute les réalisation de l'API dans la base de donnée SQLite
    public void ajouterReal(Realisation uneRealisation) {
        //création d'un ContentValues
        ContentValues v = new ContentValues();
        // ajout des propriétés au ContentValues
        v.put("id_real", uneRealisation.getIdReal());
        v.put("titre", uneRealisation.getTitre());
        v.put("description", uneRealisation.getDescriptif());
        db.insert("Realisation", null, v);
    }

    // Méthode qui retourne les une liste des ID des réalisations pour peupler le spinner
    public ArrayList<String> getId() {
        ArrayList<String> itemList = new ArrayList<String>();

        // Ouvrir la base de données SQLite
//        db.openDatabase("/data/data/com.example.gedimatapplication/databases/dbGedimatApp.db", null, SQLiteDatabase.OPEN_READWRITE);

        String req = "SELECT id_real FROM Realisation";
        Cursor cursor = db.rawQuery(req, null);

        // Loop through all rows and add to list
        if (cursor.moveToFirst()) {
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
                int id_real_cursor = cursor.getColumnIndex("id_real");
                String id_real = cursor.getString(id_real_cursor);
                itemList.add(id_real);
            }
        }

        // Close cursor and database connection
        cursor.close();

        return itemList;
    }

    public void insertVotant(String emailField, String codeParticipation, String vote1, String vote2, String vote3, Context context) {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        ContentValues values = new ContentValues();
        values.put("email", emailField);
        values.put("date_consentement", currentDate);
        values.put("vote_1", vote1);
        values.put("vote_2", vote2);
        values.put("vote_3", vote3);
        db.insert("Votant", null, values);
        Log.i("DATABASE", "Votant inserted");
    }


}
