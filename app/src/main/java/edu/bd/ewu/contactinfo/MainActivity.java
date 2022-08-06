package edu.bd.ewu.contactinfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextView cancel_btn, save_btn, upload_Photo, image_string;
    EditText name, email, phone_home, phone_office;
    ImageView image;
    String encodedImage;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        upload_Photo = findViewById(R.id.upload_photo);
        cancel_btn = findViewById(R.id.cancel_btn);
        save_btn = findViewById(R.id.save_btn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone_home = findViewById(R.id.phone_home);
        phone_office = findViewById(R.id.phone_office);
        image = findViewById(R.id.contact_image);
        image_string = findViewById(R.id.image_string);

        cancel_btn.setOnClickListener(v -> {
            finish();
            System.exit(0);
        });
        save_btn.setOnClickListener(v -> {
            String error_msg = "";
            if(name.getText().toString().length() == 0){
                error_msg += "Name Field is Empty\n";
            }
            else if(name.getText().toString().length() < 5){
                error_msg += "Name is too short!. Length should be more than 5\n";
            }
            if(email.getText().toString().isEmpty()){
                error_msg += "Email Field is Empty\n";
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                error_msg += "Email is not valid. Please enter a valid email address\n";
            }
            if(phone_home.getText().toString().isEmpty()){
                error_msg += "Phone(Home) Field is Empty\n";
            }
            else if(phone_home.getText().toString().length() != 11){
                error_msg += "Phone number is not valid. Please enter a valid phone number with 11 digits.\n";
            }
            if(phone_office.getText().toString().length() != 0){
                if(phone_office.getText().toString().length() != 11)
                    error_msg += "Phone number(Office) is not valid. Please enter a valid phone number(office) with 11 digits or keep empty.\n";
            }
            if(image.getDrawable() == null){
                error_msg += "Please Upload a Photo";
            }
            if(error_msg.equals("")){
                showDialogue("Do you want to save the Contact Info?", "Confirmation!", "Save", "Cancel");
            }
            else{
                showDialogue(error_msg, "Error Occured!", "Back", "OK");
            }

        });

        image.setOnClickListener(v->{
            if(checkAndRequestPermissions(MainActivity.this)){
                chooseImage(MainActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        image.setImageBitmap(selectedImage);
                        upload_Photo.setVisibility(View.INVISIBLE);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();
                        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        Log.d("IMAGE", encodedImage+"");
                        image_string.setText(encodedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();

                        try {
                            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
                            ByteArrayOutputStream stream=new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
                            byte[] bytes=stream.toByteArray();
                            encodedImage = Base64.encodeToString(bytes,Base64.DEFAULT);
                            image_string.setText(encodedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                upload_Photo.setVisibility(View.INVISIBLE);
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void showDialogue(String msg, String title, String btn1, String btn2){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle(title);

        builder.setCancelable(false)
                .setPositiveButton(btn1, (dialog, which) -> {
                    if(btn1.equals("Save")) {
                        SharedPreferences pref = getSharedPreferences("ContactInfo", MODE_PRIVATE);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor myEdit = pref.edit();

                        myEdit.putString("name", name.getText().toString());
                        myEdit.putString("email", email.getText().toString());
                        myEdit.putString("phone_home", phone_home.getText().toString());
                        myEdit.putString("phone_office", phone_office.getText().toString());
                        System.out.println(encodedImage);
                        myEdit.putString("image", image_string.getText().toString());

                        myEdit.apply();
                        Intent i = new Intent(getApplicationContext(), MyContact.class);
                        startActivity(i);
                        Toast.makeText(getApplicationContext(),"Successfully Saved Contact Info",Toast.LENGTH_LONG).show();
                    }
                    else dialog.cancel();
                })
                .setNegativeButton(btn2, (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),
                                "FlagUp Requires Access to Camera.", Toast.LENGTH_SHORT)
                        .show();
            } else if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),
                        "FlagUp Requires Access to Your Storage.",
                        Toast.LENGTH_SHORT).show();
            } else {
                chooseImage(MainActivity.this);
            }
        }
    }

    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[0]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}