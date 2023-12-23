package com.example.contact;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class addEditContact extends AppCompatActivity {

    private ImageView profileIv;
    private EditText nameEt, phoneEt, emailEt, noteEt;
    private FloatingActionButton fab;

    private String name, phone, email, note;

    private ActionBar actionBar;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 200;
    private static final int IMAGE_FROM_GALLERY_CODE = 300;
    private static final int IMAGE_FROM_CAMERA_CODE = 400;

    private String[] cameraPermission;
    private String[] storagePermission;

    private Uri imageUri;


    // data base helper
    private DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        //init db
        dbHelper = new DbHelper(this);

        // Initialisation des permissions
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // Initialisation de la barre d'action
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add Contact");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Initialisation des vues
        profileIv = findViewById(R.id.profileIv);
        nameEt = findViewById(R.id.nameEt);
        phoneEt = findViewById(R.id.phoneEt);
        emailEt = findViewById(R.id.emailEt);
        noteEt = findViewById(R.id.noteEt);
        fab = findViewById(R.id.fab);

        // Gestionnaire de clic sur le bouton fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Méthode pour enregistrer les données
                saveData();
            }

            // Méthode pour enregistrer les données
            private void saveData() {
                // Récupérer les données de l'utilisateur
                name = nameEt.getText().toString();
                email = emailEt.getText().toString();
                note = noteEt.getText().toString();

                String timeStamp = ""+System.currentTimeMillis();

                // Vérifier si au moins l'un des champs n'est pas vide
                if (!name.isEmpty() || !phone.isEmpty() || !email.isEmpty() || !note.isEmpty()) {
                    // Enregistrer les données (implémenter en fonction de vos besoins)
                    // Vous pouvez enregistrer les données dans une base de données ou effectuer d'autres actions ici

                   long id = dbHelper.insertContact(""+imageUri, ""+name, "phone", "email", "note", "timeStamp", "timeStamp");
                    // to check data insert data successfully
                    Toast.makeText(getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();

                } else {
                    // Afficher un message si rien n'est à enregistrer
                    Toast.makeText(getApplicationContext(), "Nothing to save..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Gestionnaire de clic sur l'image du profil
        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Méthode pour afficher la boîte de dialogue de sélection d'image
                showImagePickerDialog();
            }

            // Méthode pour afficher la boîte de dialogue de sélection d'image
            private void showImagePickerDialog() {
                // Options pour la boîte de dialogue
                String[] options = {"Camera", "Gallery"};

                // Création de la boîte de dialogue
                AlertDialog.Builder builder = new AlertDialog.Builder(addEditContact.this);
                builder.setTitle("Choose an option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Gérer le clic sur un élément de la boîte de dialogue
                        if (which == 0) { // Caméra sélectionnée
                            if (!checkCameraPermission()) {
                                // Demander la permission de la caméra
                                requestCameraPermission();
                            } else {
                                // Capturer une image depuis la caméra
                                pickFromCamera();
                            }
                        } else if (which == 1) { // Galerie sélectionnée
                            if (!checkStoragePermission()) {
                                // Demander la permission de stockage
                                requestStoragePermission();
                            } else {
                                // Sélectionner une image depuis la galerie
                                pickFromGallery();
                            }
                        }
                    }
                }).create().show();
            }



            // Méthode pour vérifier la permission de la caméra
            private boolean checkCameraPermission() {
                boolean result = ContextCompat.checkSelfPermission(addEditContact.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                boolean result1 = ContextCompat.checkSelfPermission(addEditContact.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                return result && result1;
            }

            // Méthode pour demander la permission de stockage
            private boolean checkStoragePermission() {
                boolean result1 = ContextCompat.checkSelfPermission(addEditContact.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                return result1;
            }

            // Méthode pour demander la permission de stockage
            private void requestStoragePermission() {
                ActivityCompat.requestPermissions(addEditContact.this, storagePermission, STORAGE_PERMISSION_CODE);
            }

            // Méthode pour demander la permission de la caméra
            private void requestCameraPermission() {
                ActivityCompat.requestPermissions(addEditContact.this, cameraPermission, CAMERA_PERMISSION_CODE);
            }
        });


        }

        // Gérer le résultat de la demande de permission
        @Override
        public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            switch (requestCode) {
                case CAMERA_PERMISSION_CODE:
                    if (grantResults.length > 0) {
                        boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        if (cameraAccepted && storageAccepted) {
                            // Permission accordée, capturer une image depuis la caméra
                            pickFromCamera();
                        } else {
                            // Permission refusée, afficher un message
                            Toast.makeText(getApplicationContext(), "Camera and storage permissions are required", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

                case STORAGE_PERMISSION_CODE:
                    if (grantResults.length > 0) {
                        boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        if (storageAccepted) {
                            // Permission accordée, sélectionner une image depuis la galerie
                            pickFromGallery();
                        } else {
                            // Permission refusée, afficher un message
                            Toast.makeText(getApplicationContext(), "Storage permission is required", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    // Méthode pour sélectionner une image depuis la galerie
    private void pickFromGallery() {
        // Intent pour sélectionner une image depuis la galerie
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_FROM_GALLERY_CODE);
    }


    // Méthode pour capturer une image depuis la caméra
    private void pickFromCamera() {
        // ContentValues pour les informations sur l'image
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "ImageTitle");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image detail");
        // Enregistrer l'URL de l'image
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // Intent pour ouvrir la caméra
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_FROM_CAMERA_CODE);
    }

    /////
        // Gérer le clic sur le bouton de retour
        @Override
        public boolean onSupportNavigateUp () {
            onBackPressed();
            return super.onSupportNavigateUp();
        }

        // Gérer le résultat de l'activité (sélection d'image)
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {
                if (requestCode == IMAGE_FROM_GALLERY_CODE) {
                    if (data != null) {
                        // Récupérer l'URI de l'image sélectionnée
                        imageUri = data.getData();
                        // Afficher l'image dans ImageView ou effectuer d'autres actions en fonction de vos besoins
                        profileIv.setImageURI(imageUri);
                    }
                } else if (requestCode == IMAGE_FROM_CAMERA_CODE) {
                    // Afficher l'image dans ImageView ou effectuer d'autres actions en fonction de vos besoins
                    profileIv.setImageURI(imageUri);
                }
            }
        }
    }

