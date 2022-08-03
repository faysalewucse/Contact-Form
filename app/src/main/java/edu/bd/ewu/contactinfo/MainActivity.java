package edu.bd.ewu.contactinfo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextView cancel_btn, save_btn;
    EditText name, email, phone_home, phone_office;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cancel_btn = findViewById(R.id.cancel_btn);
        save_btn = findViewById(R.id.save_btn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone_home = findViewById(R.id.phone_home);
        phone_office = findViewById(R.id.phone_office);
        image = findViewById(R.id.contact_image);

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
            if(error_msg.equals("")){
                showDialogue("Do You Want to Save the Contact Info?", "Confirmation!", "Save", "Cancel");
            }
            else{
                showDialogue(error_msg, "Error Occured!", "Back", "OK");
            }

        });
    }

    private void showDialogue(String msg, String title, String btn1, String btn2){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle(title);

        builder.setCancelable(false)
                .setPositiveButton(btn1, (dialog, which) -> {
                    //Util.getInstance().deleteByKey(MainActivity.this, key);
                    if(btn1.equals("Save")) {
                        SharedPreferences pref = getSharedPreferences("ContactInfo", MODE_PRIVATE);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor myEdit = pref.edit();

                        myEdit.putString("name", name.getText().toString());
                        myEdit.putString("email", email.getText().toString());
                        myEdit.putString("phone_home", phone_home.getText().toString());
                        myEdit.putString("phone_office", phone_office.getText().toString());

                        myEdit.apply();
                        Toast.makeText(getApplicationContext(),"Successfully Saved Contact Info",Toast.LENGTH_LONG).show();
                    }
                    else dialog.cancel();
                    //loadData();
                    //adapter.notifyDataSetChanged()
                })
                .setNegativeButton(btn2, (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }
}