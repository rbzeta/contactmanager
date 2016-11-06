package app.rbzeta.contactmanager.model;

import android.support.annotation.Nullable;

import java.io.File;

/**
 * Created by Robyn on 05/11/2016.
 */

public interface ContactAddNewInteractor {

    boolean saveContact(Contact newContact, File fileToUpload,OnFinishedCreateContactListener listener);

    interface OnValidateFinishedListener {

        void onFirstNameError();

        void onLastNameError();

        void onPhoneNumberError();

        void onEmailError();

        void onValidContact(Contact newContact);

        void onPhoneNumberLengthError();
    }

    interface OnFinishedCreateContactListener{

        void onErrorCreateContact(Throwable e);

        void onSuccessCreateContact();
    }

    void rxUnSubscribe();

    boolean validateFields(String firstName, String lastName, String phoneNumber, String email,@Nullable OnValidateFinishedListener listener);
}
