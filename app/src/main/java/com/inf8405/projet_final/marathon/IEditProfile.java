package com.inf8405.projet_final.marathon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
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

public class IEditProfile extends Activity {

    // UI references.
    private EditText mEdTxtLastPassword;
    private EditText mEdTxtNewPassword;
    private EditText mEdTxtComfirmNewPassword;
    private Button btnUpdateProfile = null;
    private Button btnBackHome = null;
    private Button btnUpdateImage = null;
    private ImageView viewImage = null;
    private Bitmap bp = null;
    private boolean fPassNotConfirm = false, fNewPassInvalide = false, fConfirmNewPassCaseEmpty = false;
    private boolean fLastPassCaseEmpty = false, fLastPassInvalide = false, fNewPassCaseEmpty = false;
    private String fLastPassword, fNewPassword, fConfirmNewPass;

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    // LogCat tag
    private static final String TAG = IEditProfile.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_edit_profile);
        mEdTxtLastPassword = (EditText) findViewById(R.id.edtTxtAncienPasswordEP);
        mEdTxtNewPassword = (EditText) findViewById(R.id.edtTxtNewPasswordEP);
        mEdTxtComfirmNewPassword = (EditText) findViewById(R.id.edtTxtConfirmNewPasswordEP);
        viewImage=(ImageView)findViewById(R.id.imgVwProfileImgEP);
        //Bitmap actualUserImage = DBContent.getInstance().getActualParticipant().getPhotoEnBitmap();
        //viewImage.setImageBitmap(actualUserImage);

        btnUpdateProfile = (Button) findViewById(R.id.btnUpdateProfileEP);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptUpdateProfile();
            }
        });
        btnBackHome = (Button) findViewById(R.id.btnBackToHomeEP);
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IMainMenu.class);
                startActivity(intent);
                finish();
            }
        });

        btnUpdateImage=(Button)findViewById(R.id.btnUpdateImgEP);
        btnUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        if (savedInstanceState != null) {
            bp = savedInstanceState.getParcelable("bitmap");
        }

        if(bp != null) {
            viewImage.setImageBitmap(bp);
        }
    }

    private void attemptUpdateProfile() {
        View focusView = null;
        boolean cancel = false;
        fPassNotConfirm = fLastPassCaseEmpty = fLastPassInvalide = fNewPassCaseEmpty = fNewPassInvalide = fConfirmNewPassCaseEmpty = false;

        // Reset errors.
        mEdTxtLastPassword.setError(null);
        mEdTxtNewPassword.setError(null);
        mEdTxtComfirmNewPassword.setError(null);

        // Store values at the time of the edit profile attempt.
        fLastPassword = mEdTxtLastPassword.getText().toString();
        fNewPassword = mEdTxtNewPassword.getText().toString();
        fConfirmNewPass = mEdTxtComfirmNewPassword.getText().toString();

        // Check for a not null last password, if the user entered one.
        if (TextUtils.isEmpty(fLastPassword)) {
            mEdTxtLastPassword.setError(getString(R.string.error_field_required));
            focusView = mEdTxtLastPassword;
            cancel = true;
            fLastPassCaseEmpty = true;
        }
        // Check for a valid last password.
        if (!isLastPasswordValid(fLastPassword)) {
            mEdTxtNewPassword.setError(getString(R.string.error_invalid_password));
            focusView = mEdTxtNewPassword;
            cancel = true;
            fLastPassInvalide = true;
        }
        // Check for a not null new password, if the user entered one.
        if (TextUtils.isEmpty(fNewPassword)) {
            mEdTxtNewPassword.setError(getString(R.string.error_field_required));
            focusView = mEdTxtNewPassword;
            cancel = true;
            fNewPassCaseEmpty = true;
        }
        // Check for a valid new  password.
        if (!isNewPasswordValid(fNewPassword)) {
            mEdTxtNewPassword.setError(getString(R.string.error_invalid_password));
            focusView = mEdTxtNewPassword;
            cancel = true;
            fNewPassInvalide = true;
        }
        // Check for a not null confirm password, if the user entered one.
        if (TextUtils.isEmpty(fConfirmNewPass)) {
            mEdTxtComfirmNewPassword.setError(getString(R.string.error_field_required));
            focusView = mEdTxtComfirmNewPassword;
            cancel = true;
            fConfirmNewPassCaseEmpty = true;
        }
        // Check for a valid confirm password .
        if (!isPasswordConfirmed(fNewPassword, fConfirmNewPass)) {
            mEdTxtComfirmNewPassword.setError(getString(R.string.error_password_not_matches));
            focusView = mEdTxtComfirmNewPassword;
            cancel = true;
            fPassNotConfirm = true;
        }

        if (!fNewPassInvalide && !fConfirmNewPassCaseEmpty && !fLastPassCaseEmpty && !fPassNotConfirm
                && !fLastPassInvalide && !fNewPassCaseEmpty) {

            /* Ici on met a jour les infos de l'utilisateur */
            if(fLastPassword.equals(DBContent.getInstance().getActualParticipant().getPassword())) {
                DBContent.getInstance().getActualParticipant().setPassword(fNewPassword);
                if(bp != null)
                    DBContent.getInstance().getActualParticipant().setPhotoEnBitmap(bp);

                DBContent.getInstance().updateParticipantInformationInRemoteContent();

                /* Après l'enregistrement  on revient  a choose group act */
                Toast.makeText(getApplicationContext(),
                        "Update profile successful!",
                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(getBaseContext(), IMainMenu.class);
                startActivity(i);
                finish();
            }
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }

    }

    private boolean isPasswordConfirmed(String NewPassword, String ConfirmNewPass) {
        return NewPassword.matches(ConfirmNewPass);
    }

    private boolean isNewPasswordValid(String NewPassword) {
        return NewPassword.length() > 4;
    }

    private boolean isLastPasswordValid(String LastPassword) {
        return LastPassword.length() > 4;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds options to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("bitmap", bp);
    }
    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(IEditProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);

                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
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
                bp = Bitmap.createScaledBitmap(thumbnail,64,64,false);
                Log.w("Image alpha: ", viewImage.getImageAlpha()   +"");
                viewImage.setImageBitmap(bp);
                Log.w("Image alpha: ", viewImage.getImageAlpha()   +"");
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
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                //Log.w("path of image from gallery......******************.........", picturePath+"");
                bp = Bitmap.createScaledBitmap(thumbnail,64,64,false);
                viewImage.setImageBitmap(bp);
            }
        }
    }
}