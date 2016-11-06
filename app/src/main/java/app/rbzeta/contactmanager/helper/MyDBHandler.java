package app.rbzeta.contactmanager.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import app.rbzeta.contactmanager.contract.ContactContract;
import app.rbzeta.contactmanager.model.Contact;

/**
 * Created by Robyn on 01/11/2016.
 */

public class MyDBHandler extends SQLiteOpenHelper {

    public static MyDBHandler sInstance;

    public static synchronized MyDBHandler getInstance(Context context){

        if (sInstance == null) {
            sInstance = new MyDBHandler(context);
        }
        return sInstance;
    }

    private MyDBHandler(Context context) {
        super(context, ContactContract.DATABASE_NAME, null, ContactContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContactContract.Contact.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ContactContract.Contact.DELETE_TABLE);
        onCreate(db);

    }

    public boolean insertContact(Contact contact){

        ContentValues values = new ContentValues();
        values.put(ContactContract.Contact.COLUMN_ID,contact.getId());
        values.put(ContactContract.Contact.COLUMN_FIRST_NAME,contact.getFirstName());
        values.put(ContactContract.Contact.COLUMN_LAST_NAME,contact.getLastName());
        values.put(ContactContract.Contact.COLUMN_EMAIL,contact.getEmail());
        values.put(ContactContract.Contact.COLUMN_PHONE,contact.getPhoneNumber());
        values.put(ContactContract.Contact.COLUMN_PROFILE_PICTURE_URL,contact.getProfilePic());
        values.put(ContactContract.Contact.COLUMN_FAVORITE,contact.getFavorite());
        values.put(ContactContract.Contact.COLUMN_CREATED_AT,contact.getCreatedAt());
        values.put(ContactContract.Contact.COLUMN_UPDATED_AT,contact.getUpdatedAt());

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.insert(ContactContract.Contact.TABLE_NAME,null,values);

        }catch (SQLiteException e){
            return false;
        }finally {
            db.close();
        }
        return true;
    }

    public boolean importContactFromServer(List<Contact> listContact){

        for (Contact contact: listContact) {
            ContentValues values = new ContentValues();
            values.put(ContactContract.Contact.COLUMN_ID,contact.getId());
            values.put(ContactContract.Contact.COLUMN_FIRST_NAME,contact.getFirstName());
            values.put(ContactContract.Contact.COLUMN_LAST_NAME,contact.getLastName());
            values.put(ContactContract.Contact.COLUMN_EMAIL,contact.getEmail());
            values.put(ContactContract.Contact.COLUMN_PHONE,contact.getPhoneNumber());
            values.put(ContactContract.Contact.COLUMN_PROFILE_PICTURE_URL,contact.getProfilePic());
            values.put(ContactContract.Contact.COLUMN_FAVORITE,contact.getFavorite());
            values.put(ContactContract.Contact.COLUMN_CREATED_AT,contact.getCreatedAt());
            values.put(ContactContract.Contact.COLUMN_UPDATED_AT,contact.getUpdatedAt());

            SQLiteDatabase db = this.getWritableDatabase();

            try {
                db.insert(ContactContract.Contact.TABLE_NAME,null,values);

            }catch (SQLiteException e){
                return false;
            }finally {
                db.close();
            }
        }

        return true;
    }

    public Contact getContactById(String contactId){
        Contact contact = new Contact();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                ContactContract.Contact._ID,
                ContactContract.Contact.COLUMN_ID,
                ContactContract.Contact.COLUMN_FIRST_NAME,
                ContactContract.Contact.COLUMN_LAST_NAME,
                ContactContract.Contact.COLUMN_EMAIL,
                ContactContract.Contact.COLUMN_PHONE,
                ContactContract.Contact.COLUMN_PROFILE_PICTURE_URL,
                ContactContract.Contact.COLUMN_FAVORITE,
                ContactContract.Contact.COLUMN_CREATED_AT,
                ContactContract.Contact.COLUMN_UPDATED_AT
        };

        String selection = ContactContract.Contact.COLUMN_ID + " = ?";
        String[] selectionArgs = { contactId };

        String sortOrder = ContactContract.Contact.COLUMN_FIRST_NAME + " ASC";

        Cursor c = db.query(
                ContactContract.Contact.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        try{
            while (c.moveToNext()){
                c.moveToFirst();

                boolean favorite;
                if (c.getInt(7) == 1)
                    favorite = true;
                else
                    favorite = false;

                contact.setId(c.getInt(1));
                contact.setFirstName(c.getString(2));
                contact.setLastName(c.getString(3));
                contact.setEmail(c.getString(4));
                contact.setPhoneNumber(c.getString(5));
                contact.setProfilePic(c.getString(6));
                contact.setFavorite(favorite);
                contact.setCreatedAt(c.getString(8));
                contact.setUpdatedAt(c.getString(9));
            }


        }catch (SQLiteException e){

        }
        finally {
            c.close();
            db.close();
        }
        return contact;
    }

    public List<Contact> getContactList(){
        List<Contact> listContact = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                ContactContract.Contact._ID,
                ContactContract.Contact.COLUMN_ID,
                ContactContract.Contact.COLUMN_FIRST_NAME,
                ContactContract.Contact.COLUMN_LAST_NAME,
                ContactContract.Contact.COLUMN_EMAIL,
                ContactContract.Contact.COLUMN_PHONE,
                ContactContract.Contact.COLUMN_PROFILE_PICTURE_URL,
                ContactContract.Contact.COLUMN_FAVORITE,
                ContactContract.Contact.COLUMN_CREATED_AT,
                ContactContract.Contact.COLUMN_UPDATED_AT
        };

        String selection = ContactContract.Contact._ID + " = ?";
        String[] selectionArgs = { null };

        String sortOrder = ContactContract.Contact.COLUMN_FIRST_NAME + " ASC";

        Cursor c = db.query(
                ContactContract.Contact.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        c.moveToFirst();
        try{
            while (c.moveToNext()){
                boolean favorite;
                if (c.getInt(7) == 1)
                    favorite = true;
                else
                    favorite = false;
                Contact contact = new Contact();
                contact.setId(c.getInt(1));
                contact.setFirstName(c.getString(2));
                contact.setLastName(c.getString(3));
                contact.setEmail(c.getString(4));
                contact.setPhoneNumber(c.getString(5));
                contact.setProfilePic(c.getString(6));
                contact.setFavorite(favorite);
                contact.setCreatedAt(c.getString(8));
                contact.setUpdatedAt(c.getString(9));
                listContact.add(contact);
            }


        }catch (SQLiteException e){

        }
        finally {
            c.close();
            db.close();
        }
        return listContact;
    }

    public boolean updateContact(Contact contact){

        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactContract.Contact.COLUMN_FAVORITE, contact.getFavorite());

        String selection = ContactContract.Contact.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(contact.getId()) };

        try{
            db.update(ContactContract.Contact.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

        }catch (SQLiteException e){
            return false;
        }finally {
            db.close();
        }

        return true;
    }

    public void deleteContactList(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ContactContract.Contact.TABLE_NAME, null, null);
        db.close();
    }

    public List<Contact> getFavoriteContacts() {
        List<Contact> listContact = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                ContactContract.Contact._ID,
                ContactContract.Contact.COLUMN_ID,
                ContactContract.Contact.COLUMN_FIRST_NAME,
                ContactContract.Contact.COLUMN_LAST_NAME,
                ContactContract.Contact.COLUMN_EMAIL,
                ContactContract.Contact.COLUMN_PHONE,
                ContactContract.Contact.COLUMN_PROFILE_PICTURE_URL,
                ContactContract.Contact.COLUMN_FAVORITE,
                ContactContract.Contact.COLUMN_CREATED_AT,
                ContactContract.Contact.COLUMN_UPDATED_AT
        };

        String selection = ContactContract.Contact.COLUMN_FAVORITE + " = ?";
        String[] selectionArgs = { String.valueOf(1) };

        String sortOrder = ContactContract.Contact.COLUMN_FIRST_NAME + " ASC";

        Cursor c = db.query(
                ContactContract.Contact.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        c.moveToFirst();
        try{
            while (c.moveToNext()){
                boolean favorite;
                if (c.getInt(7) == 1)
                    favorite = true;
                else
                    favorite = false;
                Contact contact = new Contact();
                contact.setId(c.getInt(1));
                contact.setFirstName(c.getString(2));
                contact.setLastName(c.getString(3));
                contact.setEmail(c.getString(4));
                contact.setPhoneNumber(c.getString(5));
                contact.setProfilePic(c.getString(6));
                contact.setFavorite(favorite);
                contact.setCreatedAt(c.getString(8));
                contact.setUpdatedAt(c.getString(9));
                listContact.add(contact);
            }


        }catch (SQLiteException e){

        }
        finally {
            c.close();
            db.close();
        }
        return listContact;
    }
}