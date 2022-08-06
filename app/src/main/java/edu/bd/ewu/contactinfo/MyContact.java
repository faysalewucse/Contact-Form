package edu.bd.ewu.contactinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

public class MyContact extends AppCompatActivity {

    ImageView image;
    TextView name,email, phone_office, phone_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contact);

        SharedPreferences pref = getSharedPreferences("ContactInfo", MODE_PRIVATE);
//        String image = pref.getString("image", null);
//        String name = pref.getString("name", null);
//        String email = pref.getString("email", null);
//        String phone_home = pref.getString("phone_home", null);
//        String phone_office = pref.getString("phone_office", null);

        image = findViewById(R.id.my_image);
        name = findViewById(R.id.my_name);
        email = findViewById(R.id.my_email);
        phone_home = findViewById(R.id.my_phone_home);
        phone_office = findViewById(R.id.my_phone_office);

        if(!pref.getString("image", "").equalsIgnoreCase("") ){
            byte[] b = Base64.decode(pref.getString("image", null), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            image.setImageBitmap(bitmap);
        }
        name.setText(pref.getString("name", null));
        email.setText(pref.getString("email", null));
        phone_home.setText(pref.getString("phone_home", null));
        phone_office.setText(pref.getString("phone_office", null));
    }
}