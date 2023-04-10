package com.example.gedimatapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ImportExportActivity extends AppCompatActivity {

    private Button btnIMPORT = null;
    private Button btnExport = null;
    private ImageButton back;
    MySQLiteOpenHelper mySQLiteOpenHelper;
    GedimatDAO dao;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        this.back = findViewById(R.id.back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainReturn = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(MainReturn);
                finish();
            }
        });

        // obtention des références sur les vues de l'activité
        btnIMPORT = (Button) findViewById(R.id.boutonImport);
        btnExport = (Button) findViewById(R.id.boutonExport);

        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);

        btnIMPORT.setOnClickListener(EcouteurBouton);
        btnExport.setOnClickListener(EcouteurBouton);

    }

    public View.OnClickListener EcouteurBouton = new View.OnClickListener() {
        @SuppressLint("Range")
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.boutonImport:
                    // Requête HTTP GET
                    String urlI = "http://10.0.2.2/APIgedimat/importReal.php";
                    AsyncHttpClient requestI = new AsyncHttpClient();
                    requestI.get(urlI, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            // Deserialisation du flux JSON
                            dao = new GedimatDAO(ImportExportActivity.this);
                            dao.supprimerTous();
                            Log.i("Donnée", response.toString());
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    Integer id_real = response.getJSONObject(i).getInt("id_real");
                                    String titre = response.getJSONObject(i).getString("titre");
                                    String descriptif = response.getJSONObject(i).getString("descriptif");
                                    Realisation R = new Realisation();
                                    R.setIdRealisation(id_real);
                                    R.setTitre(titre);
                                    R.setDescriptif(descriptif);
                                    Log.i("info", R.toString());
                                    dao.ajouterReal(R);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Toast.makeText(getApplicationContext(), "Imporation terminée", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            Log.i("Erreur", String.valueOf(statusCode) + "Erreur = " + responseString);
                            Toast.makeText(getApplicationContext(), "Echec de l'importation", Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                case R.id.boutonExport:
                    // Construction flux JSON
                    JSONArray jsonArray = new JSONArray();
                    dao = new GedimatDAO(ImportExportActivity.this);
                    Cursor curseurTous = dao.touteLesReal();
                    for (curseurTous.moveToFirst(); !curseurTous.isAfterLast(); curseurTous.moveToNext()) {
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("dossard", curseurTous.getString(curseurTous.getColumnIndex("dossard")));
                            obj.put("natation", curseurTous.getFloat(curseurTous.getColumnIndex("natation")));
                            obj.put("cyclisme", curseurTous.getFloat(curseurTous.getColumnIndex("cyclisme")));
                            obj.put("course", curseurTous.getFloat(curseurTous.getColumnIndex("course")));
                            jsonArray.put(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    JSONObject fluxJSON = new JSONObject();
                    try {
                        fluxJSON.put("Concurrents", jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Requête HTTP POST
                    String urlE = "http://10.0.2.2/APItriathlon/concurrents.php";
                    AsyncHttpClient requestE = new AsyncHttpClient();
                    try {
                        StringEntity entity = new StringEntity(fluxJSON.toString());
                        requestE.put(ImportExportActivity.this, urlE, entity, "application/json", new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Log.i("Code response = ",String.valueOf(statusCode)+ "Données envoyées = "+fluxJSON.toString());
                                Toast.makeText(getApplicationContext(), "Exportation terminée ", Toast.LENGTH_LONG).show();
                            }
                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Log.i("Erreur", String.valueOf(statusCode) + "Erreur = " + error.toString());
                                Toast.makeText(getApplicationContext(),"Echec de l'exportation",Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };
}