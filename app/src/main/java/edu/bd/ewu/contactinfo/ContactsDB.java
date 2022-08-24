package edu.bd.ewu.contactinfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ContactsDB extends SQLiteOpenHelper {
    // Contact TABLE INFORMATION
    static final String DB_NAME = "CONTACTS.DB";
    public final String CONTACT_INFO = "contact_info";
    public final String ID = "id";
    public final String NAME = "name";
    public final String EMAIL = "email";
    public final String PHONE = "phone";
    public final String ADDRESS = "address";
    public final String IMAGE = "image";

    Context context;
    public ContactsDB(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("DB@OnCreate");
        createKeyValueTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createKeyValueTable(SQLiteDatabase db){
        try {
            db.execSQL("create table " + CONTACT_INFO + " (" + ID +" TEXT ,"+ NAME
                    + " TEXT , " + EMAIL + " TEXT , "+ PHONE  + " TEXT , " + ADDRESS + " TEXT , "+ IMAGE + " TEXT )");
        } catch (Exception e) {
            Toast.makeText(context, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor execute(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        try {
            res = db.rawQuery(query, null);
        }catch (Exception e) {
            //e.printStackTrace();
            handleError(db, e);
            res = db.rawQuery(query, null);
        }
        return res;
    }

    public Boolean insertInfo(String id, String name, String email, String phone, String address, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID, id);
        cv.put(NAME, name);
        cv.put(EMAIL, email);
        cv.put(PHONE, phone);
        cv.put(ADDRESS, address);
        cv.put(IMAGE, image);
        long result;
        try{
            result = db.insert(CONTACT_INFO, null, cv);
        }catch (Exception e){
            handleError(db, e);
            result = db.insert(CONTACT_INFO, null, cv);
        }
        return result != -1;
    }

    private void handleError(SQLiteDatabase db, Exception e){
        String errorMsg = e.getMessage();
        assert errorMsg != null;
        if (errorMsg.contains("no such table")){
            if (errorMsg.contains(CONTACT_INFO)){
                createKeyValueTable(db);
            }
        }
    }

	public Cursor getAllKeyValues() {
		SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + CONTACT_INFO, null);
	}
}
