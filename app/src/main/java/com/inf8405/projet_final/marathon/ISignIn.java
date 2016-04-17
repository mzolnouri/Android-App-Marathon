package com.inf8405.projet_final.marathon;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

/**
 * Created by mahdi zolnouri on 16-03-24.
 */
public class ISignIn extends AppCompatActivity {
    // UI references.
    private EditText fEmailView;
    private EditText fPasswordView;
    private Button fBtnSignIn = null;
    private Button fBtnSignUP = null;

    /* Declare the fiels */
    private String fEmail, fPassword;
    private String fAuthentificationReponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        fAuthentificationReponse = MConstants.WRONG_PASSWORD;

        fBtnSignIn = (Button) findViewById(R.id.btnSignInMMenu);
        fBtnSignUP = (Button) findViewById(R.id.btnSignUpMMenu);
        fBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fEmailView = (EditText) findViewById(R.id.edtTxtEmailMMenu);
                fPasswordView = (EditText) findViewById(R.id.edtTxtPasswordMMenu);
                // Reset errors.
                fEmailView.setError(null);
                fPasswordView.setError(null);

                // Store values at the time of the login attempt.
                fEmail = fEmailView.getText().toString();
                fPassword = fPasswordView.getText().toString();

                View focusView = null;

                // Check for a valid password, if the user entered one.
                if (!TextUtils.isEmpty(fPassword) && !isPasswordValid(fPassword)) {
                    fPasswordView.setError(getString(R.string.ERROR_INVALID_PASSWORD));
                    focusView = fPasswordView;
                }

                // Check for a valid email address.
                if (TextUtils.isEmpty(fEmail)) {
                    fEmailView.setError(getString(R.string.ERROR_FIELD_REQUIRED));
                    focusView = fEmailView;
                } else if (!isEmailValid(fEmail)) {
                    fEmailView.setError(getString(R.string.ERROR_INVALID_EMAIL));
                    focusView = fEmailView;
                }
                // Calling async task to get json
                try {
                    new ConnectionCode().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (fAuthentificationReponse.contentEquals(MConstants.ACCESS_GRANTED)) {
                    Toast.makeText(getApplicationContext(),
                            "Password is correct :)",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getBaseContext(), IMainMenu.class);
                    startActivity(i);
                    finish();
                } else if (fAuthentificationReponse.contentEquals(MConstants.WRONG_EMAIL)) {
                    Toast.makeText(getApplicationContext(),
                            "Your email est incorrect! Create a new account. ",
                            Toast.LENGTH_LONG).show();
                    if (focusView == null)
                        focusView = fEmailView;
                    focusView.requestFocus();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Password is incorrect :(",
                            Toast.LENGTH_LONG).show();
                    if (focusView == null)
                        focusView = fPasswordView;
                    focusView.requestFocus();
                }
            }
        });
        fBtnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ISignUp.class);
                startActivity(intent);
                finish();

            }
        });
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private class ConnectionCode extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            // Créer un nouveau utilisateur pour valider sign in
            Participant participant = new Participant();
            participant.setCourriel(fEmail);
            participant.setPassword(fPassword);
            fAuthentificationReponse = DBContent.getInstance().authentification(participant.getCourriel(), participant.getPassword());
            return null;
        }
    }

}

