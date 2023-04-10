package com.example.gedimatapplication;

import static com.loopj.android.http.AsyncHttpClient.log;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoteActivity extends AppCompatActivity {

    private Spinner spinReal1 = null;
    private Spinner spinReal2 = null;
    private Spinner spinReal3 = null;
    private ArrayAdapter<String> adapterCandidate1;
    private ArrayAdapter<String> adapterCandidate2;
    private ArrayAdapter<String> adapterCandidate3;
    private GedimatDAO dao;
    private ArrayList<String> listID;
    private String vote_1;
    private String vote_2;
    private String vote_3;
    private Spinner ratingSpinner1;
    private Spinner ratingSpinner2;
    private Spinner ratingSpinner3;
    private ImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        // lien vers l'activity main
        this.back = findViewById(R.id.back_button);
        Spinner ratingSpinner1 = findViewById(R.id.rating_spinner_1);
        Spinner ratingSpinner2 = findViewById(R.id.rating_spinner_2);
        Spinner ratingSpinner3 = findViewById(R.id.rating_spinner_3);

        // action de passage a la vue importReal
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainReturn = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(MainReturn);
                finish();
            }
        });

        dao = new GedimatDAO(this);

        // Initialisation des spinners
        spinReal1 = findViewById(R.id.spin_real_1);
        spinReal2 = findViewById(R.id.spin_real_2);
        spinReal3 = findViewById(R.id.spin_real_3);

        // Récupération des données de la base de données
        listID = dao.getId();

        // Initialisation des adapters pour chaque spinner
        adapterCandidate1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(listID));
        adapterCandidate2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(listID));
        adapterCandidate3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(listID));

        // Spécification du layout pour les dropdowns
        adapterCandidate1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCandidate2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCandidate3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Assignation des adapters aux spinners
        spinReal1.setAdapter(adapterCandidate1);
        spinReal2.setAdapter(adapterCandidate2);
        spinReal3.setAdapter(adapterCandidate3);

        // Validation pour empêcher la sélection du même choix sur plusieurs spinners
        View.OnClickListener voteButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinReal1.getSelectedItem().equals(spinReal2.getSelectedItem()) ||
                        spinReal1.getSelectedItem().equals(spinReal3.getSelectedItem()) ||
                        spinReal2.getSelectedItem().equals(spinReal3.getSelectedItem())) {
                    Toast.makeText(VoteActivity.this, "Les choix des trois réalisations doivent être différents", Toast.LENGTH_SHORT).show();
                } else {
                    // Récuperation de l'email et du code de participation resseignés dans l'activité PreVoteActivity
                    Intent sendVotant = getIntent();
                    String email = sendVotant.getStringExtra("emailField");
                    String codeParticipant = sendVotant.getStringExtra("codeField");

                    // Enregistrer le vote dans la base de données
                    String vote1 = spinReal1.getSelectedItem().toString();
                    String vote2 = spinReal2.getSelectedItem().toString();
                    String vote3 = spinReal3.getSelectedItem().toString();
                    dao.insertVotant(email, codeParticipant, vote1, vote2, vote3, null);

                    int selectedRating1 = (int) ratingSpinner1.getSelectedItem();
                    int selectedRating2 = (int) ratingSpinner2.getSelectedItem();
                    int selectedRating3 = (int) ratingSpinner3.getSelectedItem();


                    Toast.makeText(VoteActivity.this, "Vote enregistré avec succès !", Toast.LENGTH_SHORT).show();

                }
            }
        };


        Button voteButton = findViewById(R.id.vote);
        voteButton.setOnClickListener(voteButtonClickListener);
    }
}