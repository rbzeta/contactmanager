package app.rbzeta.contactmanager.presenter;

import android.content.Intent;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;

import app.rbzeta.contactmanager.model.ContactAddNewInteractor;

/**
 * Created by Robyn on 05/11/2016.
 */

public interface ContactAddNewPresenter {
    void rxUnSubscribe();

    void onBackPressed();

    void takePictureFromGallery();

    File prepareImageGalleryForUpload(Intent data);

    void reloadProfilePicture();

    void takePictureFromCamera();

    File createImageFile() throws IOException;

    File prepareImageCameraForUpload();

    void submitContact(String fistName, String lastName, String phoneNumber, String email,@Nullable ContactAddNewInteractor.OnValidateFinishedListener listener);

    void setActivityResult();
}
