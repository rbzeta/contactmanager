package app.rbzeta.contactmanager.view;

import android.net.Uri;

import java.io.File;

/**
 * Created by Robyn on 05/11/2016.
 */
public interface IContactAddNewView {
    void onBackPressed();

    void takePictureFromGallery();

    String getRealPathFromURI(Uri uri);

    int getImageProfileWidth();

    int getImageProfileHeight();

    void reloadProfilePicture();

    void takePictureFromCamera();

    void deleteImageFromCamera();

    void setTextFirstNameError();

    void setTextLastNameError();

    void setTextPhoneNumberError();

    void setTextEmailError();

    void showErrorConnectionDialog();

    File getFileToUpload();

    void showToastErrorCreateContact(Throwable e);

    void onSuccessCreateContact();

    void finish();

    void addPictureToLibrary(String mCurrentPhotoPath);

    void setTextPhoneNumberLengthError();

    void setActivityResult(boolean b);
}
