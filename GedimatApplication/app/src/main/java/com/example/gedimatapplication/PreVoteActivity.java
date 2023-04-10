package com.example.gedimatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import androidx.appcompat.app.AppCompatActivity;

public class PreVoteActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText codeField;
    private CheckBox termsCheckbox;
    private Button btnCheckTerm;
    private Pattern emailPattern;
    Pattern codePattern;
    private GedimatDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_vote);

        dao = new GedimatDAO(this);
        codeField = findViewById(R.id.code_participant);
        emailField = findViewById(R.id.email_field);
        termsCheckbox = findViewById(R.id.terms_checkbox);
        btnCheckTerm = findViewById(R.id.btn_check_term);
        emailPattern = Pattern.compile("^\\w+[\\w-\\.]*\\@\\w+((-\\w+)|(\\w*))\\.[a-z]{2,3}$");
        codePattern = Pattern.compile("^[A-Z0-9]{4}\\s[A-Z0-9]{4}\\s[A-Z0-9]{4}$");

        codeField.addTextChangedListener(new TextWatcher() {
            private boolean spaceDeleted = false;
            private boolean formatting = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing to do here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // nothing to do here
            }

            @Override
            public void afterTextChanged(Editable s) {
                // remove previous listeners
                codeField.removeTextChangedListener(this);

                // store cursor position
                int cursorPosition = codeField.getSelectionStart();

                // remove spaces
                String code = s.toString().replaceAll("\\s", "");

                // check maximum length without spaces
                if (code.length() > 12) {
                    code = code.substring(0, 12);
                }

                // add spaces after every group of 4 characters
                StringBuilder codeBuilder = new StringBuilder();
                for (int i = 0; i < code.length(); i++) {
                    if (i > 0 && i % 4 == 0) {
                        codeBuilder.append(" ");
                    }
                    codeBuilder.append(code.charAt(i));
                }

                // save new value with spaces
                String newCode = codeBuilder.toString();

                // if new value is different from the current one, update it
                if (!s.toString().equals(newCode)) {
                    formatting = true;
                    s.replace(0, s.length(), newCode);
                }

                // replace cursor
                if (!formatting && cursorPosition > 0 && newCode.length() >= cursorPosition && spaceDeleted) {
                    if (newCode.charAt(cursorPosition - 1) == ' ') {
                        cursorPosition++;
                    }
                    codeField.setSelection(cursorPosition);
                }

                formatting = false;
                spaceDeleted = false;

                // add new listeners
                codeField.addTextChangedListener(this);
            }
        });

        btnCheckTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                String codeParticipant= codeField.getText().toString().trim();

                // Vérifiez si le champ du code de participation est vide ou n'a pas une structure correcte
                Matcher emailMatcher = emailPattern.matcher(email);
                Matcher codeMatcher = codePattern.matcher(codeParticipant);
                if (codeParticipant.isEmpty() || !codeMatcher.matches()) {
                    Toast.makeText(PreVoteActivity.this, "Veuillez saisir un code de participation valide", Toast.LENGTH_SHORT).show();
                }
                // Vérifiez si le champ d'e-mail est vide ou n'a pas une structure correcte
                else if (email.isEmpty() || !emailMatcher.matches()) {
                    Toast.makeText(PreVoteActivity.this, "Veuillez saisir une adresse e-mail valide", Toast.LENGTH_SHORT).show();
                }
                // Vérifiez si la case à cocher des conditions est cochée
                else if (!termsCheckbox.isChecked()) {
                    Toast.makeText(PreVoteActivity.this, "Veuillez accepter les conditions", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Toutes les conditions sont bonnes, l'email et le code de participation sont envoyés dans l'activité VoteActivity
                    Intent sendVotant = new Intent(PreVoteActivity.this, VoteActivity.class);
                    sendVotant.putExtra("emailField", email);
                    sendVotant.putExtra("codeField", codeParticipant);
                    // Lance l'activité du vote
                    startActivity(sendVotant);
                }
            }
        });
    }
}