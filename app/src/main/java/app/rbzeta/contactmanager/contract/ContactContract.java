package app.rbzeta.contactmanager.contract;

import android.provider.BaseColumns;

/**
 * Created by Robyn on 01/11/2016.
 */

public class ContactContract {
    public static final  int    DATABASE_VERSION   = 2;
    public static final  String DATABASE_NAME      = "contact_manager.db";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String COMMA_SEP          = ",";

    private ContactContract(){

    }

    public static class Contact implements BaseColumns {


        private Contact(){}

        public static final String TABLE_NAME = "Contact";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_PHONE = "phone_number";
        public static final String COLUMN_PROFILE_PICTURE_URL = "profile_pic";
        public static final String COLUMN_FAVORITE = "favorite";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_ID + " INTEGER " + COMMA_SEP +
                        COLUMN_FIRST_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_LAST_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_EMAIL + TEXT_TYPE + COMMA_SEP +
                        COLUMN_PHONE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_PROFILE_PICTURE_URL + TEXT_TYPE + COMMA_SEP +
                        COLUMN_FAVORITE + " INTEGER " + COMMA_SEP +
                        COLUMN_CREATED_AT + TEXT_TYPE + COMMA_SEP +
                        COLUMN_UPDATED_AT + TEXT_TYPE +

                         " )";

        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}
