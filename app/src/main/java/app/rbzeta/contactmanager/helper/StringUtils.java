package app.rbzeta.contactmanager.helper;

import android.util.Patterns;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.rbzeta.contactmanager.application.AppConfig;

/**
 * Created by Robyn on 05/11/2016.
 */

public class StringUtils {

    public static final String getStringTimeStamp(){
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    public static String getImageFileName() {
        return AppConfig.PREFIX_IMG_FILE_NAME + getStringTimeStamp();
    }

    public static boolean validateFirstName(String firstName) {
        if (firstName.length() < 3)
            return false;
        else
            return true;
    }

    public static boolean validateLastName(String lastName) {
        if (lastName.length() < 2)
            return false;
        else
            return true;
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        if (!Patterns.PHONE.matcher(phoneNumber).matches()){
            return false;
        }
        return true;
    }

    public static boolean validateEmailAddress(String email) {
        if (email.length() < 1) {
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return false;
        }
        return true;
    }

    public static boolean validatePhoneLength(String phoneNumber) {
        if (phoneNumber.length() < 10) {
            return false;
        }
        return true;
    }

    public static String createContactAsText(String name, String phone, String email) {
        String text = "Name : "+name;
        text+=        "\nPhone  :"+phone;
        text+=        "\nEmail  :"+email;

        return text;
    }
}
