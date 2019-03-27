package com.example.hmod_.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText name;
    private EditText age;
    private EditText mail;
    private Button save;
    private ImageView pic;

    private int PICK_IMAGE_FROM_GALLERY_REQUEST = 1;
    private Intent intentActivity;
    private Uri photoUri;
    private Bitmap selectedImage;
    private SharedPreferences data;
    SharedPreferences.Editor editor;
    private static final String TAG = "MainActivity";


    Activity activity;
    final int code = 111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        name = findViewById(R.id.EditName);
        age = findViewById(R.id.EditAge);
        mail = findViewById(R.id.EditMail);
        save = findViewById(R.id.Save);
        pic = findViewById(R.id.MyImg);
        activity = this;

        SharedPreferences data = activity.getPreferences(Context.MODE_PRIVATE);
        String UserName = data.getString("Name", null);
        String UserAge = data.getString("Age", null);
        String UserMail = data.getString("Mail", null);


        name.setText(UserName);
        age.setText(UserAge);
        mail.setText(UserMail);
        name.setTextColor(Color.WHITE);
        age.setTextColor(Color.WHITE);
        mail.setTextColor(Color.WHITE);

        SharedPreferences SavedFile = activity.getPreferences(Context.MODE_PRIVATE);
        editor = SavedFile.edit();

        String mImageUri = SavedFile.getString("image", null);

        if (mImageUri != null) {
            pic.setImageURI(Uri.parse(mImageUri));
        } else {
            pic.setImageResource(R.drawable.ic_launcher_background);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("Name", name.getText().toString());
                editor.putString("Age", age.getText().toString());
                editor.putString("Mail", mail.getText().toString());

                editor.apply();
                Toast.makeText(activity, "Saved", Toast.LENGTH_SHORT).show();
            }

        });

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSelect();
            }
        });
    }

    public void imageSelect() {
        permissionsCheck();
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_FROM_GALLERY_REQUEST);
    }

    public void permissionsCheck() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a image.
                // The Intent's data Uri identifies which item was selected.
                if (data != null) {

                    // This is the key line item, URI specifies the name of the data
                    photoUri = data.getData();

                    // Saves image URI as string to Default Shared Preferences

                    editor.putString("image", String.valueOf(photoUri));
                    editor.commit();

                    // Sets the ImageView with the Image URI
                    pic.setImageURI(photoUri);
                    pic.invalidate();
                }
            }
        }
    }

}


