package com.example.gedimatapplication;

/**
 *Create by Eliott Fertille on 14/02/2023
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button enter;
    private Button preVote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // lien vers les boutons de l'activité
        this.enter = findViewById(R.id.enter);
        this.preVote = findViewById(R.id.pre_vote);

        // action de passage a la vue importReal
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent importGo = new Intent(getApplicationContext(), ImportExportActivity.class);
                startActivity(importGo);
                finish();
            }
        });

        // action de passage temporaire a l'activité de vote
        preVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preVoteGo = new Intent(getApplicationContext(), PreVoteActivity.class);
                startActivity(preVoteGo);
                finish();
            }
        });
    }
}