package com.inf8405.projet_final.marathon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ISignUp extends AppCompatActivity {

    // UI references.
    private EditText fEdTxtEmail;
    private EditText fEdTxtPassword;
    private EditText fEdTxtFirstName;
    private EditText fEdTxtLastName;
    private EditText fEdTxtComfirmPassword;
    private Button fBtnCreateAccount = null;
    private Button fBtnBackMainMenu = null;
    private Button fBtnSetImage = null;
    private ImageView fImageView = null;
    private Bitmap fImageEnBitmap = null;

    // Declare the fields
    private double fCurrentLatitude;
    private double fCurrentLongitude;
    private String fEmail;
    private String fPassword;
    private String fConfirmPass;
    private String fFirstName;
    private String fLastName;
    private boolean fUserInsertSuccessful = false;
    private boolean fPassCaseEmpty = false;
    private boolean fEmailCaseEmpty = false;
    private boolean fEmailInvalide = false;
    private boolean fConfirmPassCaseEmpty = false;
    private boolean fPassNotConfirm = false;
    private boolean fPassInvalide = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fBtnCreateAccount = (Button) findViewById(R.id.btnCreateAccountSU);
        fBtnBackMainMenu = (Button) findViewById(R.id.btnBackToMainMenuSU);
        fBtnSetImage = (Button) findViewById(R.id.btnSetImgSU);

        fEdTxtFirstName = (EditText) findViewById(R.id.edtTxtFNSU);
        fEdTxtLastName = (EditText) findViewById(R.id.edtTxtLNSU);
        fEdTxtEmail = (EditText) findViewById(R.id.edtTxtEmailSU);
        fEdTxtPassword = (EditText) findViewById(R.id.edtTxtPasswordSU);
        fEdTxtComfirmPassword = (EditText) findViewById(R.id.edtTxtConfirmPasswordSU);

        fBtnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ISignUp.this, "Longitude : " + fCurrentLongitude + "Latitude : " + fCurrentLatitude, Toast.LENGTH_LONG).show();
                attemptLogin();
            }
        });

        fImageView = (ImageView) findViewById(R.id.imgVwProfileImgSU);

        fBtnSetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        if (savedInstanceState != null) {
            fImageEnBitmap = savedInstanceState.getParcelable("bitmap");
        }

        if (fImageEnBitmap != null) {
            fImageView.setImageBitmap(fImageEnBitmap);
        }

        fBtnBackMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ISignIn.class);
                startActivity(intent);
                finish();
            }
        });
        getActualLocation();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("bitmap", fImageEnBitmap);
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ISignUp.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                fImageEnBitmap = Bitmap.createScaledBitmap(thumbnail, 64, 64, false);
                Log.w("Image alpha: ", fImageView.getImageAlpha() + "");
                fImageView.setImageBitmap(fImageEnBitmap);
                Log.w("Image alpha: ", fImageView.getImageAlpha() + "");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                fImageEnBitmap = Bitmap.createScaledBitmap(thumbnail, 64, 64, false);
                fImageView.setImageBitmap(fImageEnBitmap);
            } else if (requestCode == 3) {
                fCurrentLatitude = Double.valueOf(data.getStringExtra("latitude"));
                fCurrentLongitude = Double.valueOf(data.getStringExtra("longitude"));
            }
        }
    }

    private void getActualLocation() {
        Intent i = new Intent(this, MFindLocation.class);
        startActivityForResult(i, 3);
    }


    private void attemptLogin() {
        View focusView = null;
        boolean cancel = false;
        fEmailCaseEmpty = fPassCaseEmpty = fPassInvalide = fEmailInvalide = fConfirmPassCaseEmpty = fPassNotConfirm = fUserInsertSuccessful = false;

        // Reset errors.
        fEdTxtEmail.setError(null);
        fEdTxtPassword.setError(null);
        fEdTxtComfirmPassword.setError(null);

        fFirstName = fEdTxtFirstName.getText().toString();
        fLastName = fEdTxtLastName.getText().toString();
        fEmail = fEdTxtEmail.getText().toString();
        fPassword = fEdTxtPassword.getText().toString();
        fConfirmPass = fEdTxtComfirmPassword.getText().toString();

        // Check for a not null password, if the user entered one.
        if (TextUtils.isEmpty(fPassword)) {
            fEdTxtPassword.setError(getString(R.string.ERROR_FIELD_REQUIRED));
            focusView = fEdTxtPassword;
            cancel = true;
            fPassCaseEmpty = true;
        }
        // Check for a valid password.
        if (!isPasswordValid(fPassword)) {
            fEdTxtPassword.setError(getString(R.string.ERROR_INVALID_PASSWORD));
            focusView = fEdTxtPassword;
            cancel = true;
            fPassInvalide = true;
        }
        // Check for a not null confirm password, if the user entered one.
        if (TextUtils.isEmpty(fConfirmPass)) {
            fEdTxtComfirmPassword.setError(getString(R.string.ERROR_FIELD_REQUIRED));
            focusView = fEdTxtComfirmPassword;
            cancel = true;
            fConfirmPassCaseEmpty = true;
        }
        // Check for a valid confirm password .
        if (!isPasswordConfirmed(fPassword, fConfirmPass)) {
            fEdTxtComfirmPassword.setError(getString(R.string.ERROR_PASSWORD_NOT_MATCHES));
            focusView = fEdTxtComfirmPassword;
            cancel = true;
            fPassNotConfirm = true;
        }
        // Check for a not null email address.
        if (TextUtils.isEmpty(fEmail) && !isEmailValid(fEmail)) {
            fEdTxtEmail.setError(getString(R.string.ERROR_FIELD_REQUIRED));
            focusView = fEdTxtEmail;
            cancel = true;
            fEmailCaseEmpty = true;
        }
        // Check for a valid email address.
        if (!isEmailValid(fEmail)) {
            fEdTxtEmail.setError(getString(R.string.ERROR_INVALID_EMAIL));
            focusView = fEdTxtEmail;
            cancel = true;
            fEmailInvalide = true;
        }

        if (!fPassCaseEmpty && !fPassInvalide && !fConfirmPassCaseEmpty && !fPassNotConfirm
                && !fEmailCaseEmpty && !fEmailInvalide) {
            // Calling async task to get json
            try {
                new ConnectionCode().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (fUserInsertSuccessful) {
//                Toast.makeText(getApplicationContext(),
//                        "Sign up successful!",
//                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(getBaseContext(), IMainMenu.class);
                startActivity(i);
                finish();

            }
            if (cancel) {
                // There was an error; don't attempt login and
                // focus the first form field with an error.
                if (focusView == null)
                    focusView = fEdTxtEmail;
                focusView.requestFocus();
            } else if (!fUserInsertSuccessful) {
                Toast.makeText(getApplicationContext(),
                        "Registration failed! try again, please!",
                        Toast.LENGTH_LONG).show();
                if (focusView == null)
                    focusView = fEdTxtEmail;
                focusView.requestFocus();
            }
        }

    }

    private boolean isPasswordConfirmed(String password, String confirmPass) {
        return password.matches(confirmPass);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private class ConnectionCode extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            // Créer un nouveau utilisateur pour valider sign in
//            MUser newUser = new MUser();
//            newUser.setEmail(fEmail);
//            newUser.setPassword(fPassword);
//            newUser.setPosition(new MPosition(fCurrentLatitude, fCurrentLongitude));
//            if (fImageEnBitmap != null)
//                newUser.setPhotoEnBitmap(fImageEnBitmap);
//
//            newUser.setfLastName(fLastName);
//            newUser.setfFirstName(fFirstName);
//
//            String response = MDataBaseContent.getInstance().CreateNewUser(newUser);
//            if (response.contentEquals(MConstants.USER_ADDED)) {
                fUserInsertSuccessful = true;
//            }

            return null;

        }
    }

}
