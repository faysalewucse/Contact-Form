package edu.bd.ewu.contactinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ListView contact_list;
    TextView add_contact;
    ArrayList<ContactInfo> contactInfos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contact_list = findViewById(R.id.contact_list);
        add_contact = findViewById(R.id.addContactBtn);

        System.out.println(contactInfos);
        ContactListAdapter adapter=new ContactListAdapter(getApplicationContext(), contactInfos);
        contact_list.setAdapter(adapter);

        add_contact.setOnClickListener(v->{
            startActivity(new Intent(this, AddContact.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ContactsDB db = new ContactsDB(this);
        Cursor c = db.getAllKeyValues();
        String id="", name="", email="", phone="", address="",image ="";
        while(c.moveToNext()){
            id = c.getString(0);
            name = c.getString(1);
            email = c.getString(2);
            phone = c.getString(3);
            address = c.getString(4);
            image = c.getString(5);

            ContactInfo contactInfo = new ContactInfo(id, name, email, phone, address, image);
            contactInfos.add(contactInfo);
        }
    }
}