package app.rbzeta.contactmanager.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import app.rbzeta.contactmanager.helper.FileUtils;
import app.rbzeta.contactmanager.helper.StringUtils;
import app.rbzeta.contactmanager.model.Contact;
import app.rbzeta.contactmanager.model.ContactAddNewInteractor;
import app.rbzeta.contactmanager.model.ContactAddNewInteractorImpl;
import app.rbzeta.contactmanager.receiver.ConnectivityReceiver;
import app.rbzeta.contactmanager.view.IContactAddNewView;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

/**
 * Created by Robyn on 05/11/2016.
 */

public class ContactAddNewPresenterImpl implements ContactAddNewPresenter,
        ContactAddNewInteractor.OnValidateFinishedListener,
        ContactAddNewInteractor.OnFinishedCreateContactListener {
    private IContactAddNewView view;
    private ContactAddNewInteractor interactor;
    private String mCurrentPhotoPath;

    public ContactAddNewPresenterImpl(IContactAddNewView view){
        this.view = view;
        this.interactor = new ContactAddNewInteractorImpl();
    }

    public ContactAddNewPresenterImpl(IContactAddNewView view,ContactAddNewInteractor interactor){
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void rxUnSubscribe() {
        interactor.rxUnSubscribe();
    }

    @Override
    public void onBackPressed() {
        view.onBackPressed();
    }

    @Override
    public void takePictureFromGallery() {
        view.takePictureFromGallery();
    }

    @Override
    public File prepareImageGalleryForUpload(Intent data) {
        File file = null;
        OutputStream os;
        if (data != null){

            try {
                Uri selectedImage = data.getData();
                mCurrentPhotoPath = view.getRealPathFromURI(selectedImage);

                int targetW = view.getImageProfileWidth();
                int targetH = view.getImageProfileHeight();

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

                ExifInterface ei;
                ei = new ExifInterface(mCurrentPhotoPath);

                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        bitmap = rotateImage(bitmap, 90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        bitmap = rotateImage(bitmap, 180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        bitmap = rotateImage(bitmap, 270);
                        break;
                    case ExifInterface.ORIENTATION_NORMAL:
                        break;
                    default:
                        break;
                }

                file = createImageFile();

                os = new FileOutputStream(file);

                bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
                os.flush();
                os.close();

                mCurrentPhotoPath = file.getAbsolutePath();

            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;

    }

    @Override
    public void reloadProfilePicture() {
        view.reloadProfilePicture();
    }

    @Override
    public void takePictureFromCamera() {
        view.takePictureFromCamera();
    }

    @Override
    public File createImageFile() throws IOException {
        String imageFileName = StringUtils.getImageFileName();
        File storageDir = FileUtils.getStorageDirectory();

        if (!storageDir.exists()){
            if (!storageDir.mkdirs()){
                return null;
            }
        }

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public File prepareImageCameraForUpload() {
        int targetW = view.getImageProfileWidth();
        int targetH = view.getImageProfileHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap resultBitmap;
        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                resultBitmap = rotateImage(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                resultBitmap = rotateImage(bitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                resultBitmap = rotateImage(bitmap, 270);
                break;
            case ExifInterface.ORIENTATION_NORMAL:
                resultBitmap = bitmap;
                break;
            default:
                resultBitmap = bitmap;
                break;
        }

        view.deleteImageFromCamera();

        OutputStream os;
        File file = null;
        try {
            file = File.createTempFile(
                    StringUtils.getImageFileName(),  /* prefix */
                    ".jpg",         /* suffix */
                    FileUtils.getStorageDirectory()      /* directory */
            );
            os = new FileOutputStream(file);
            resultBitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
            os.flush();
            os.close();

            mCurrentPhotoPath = file.getAbsolutePath();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    @Override
    public void submitContact(String firstName, String lastName, String phoneNumber, String email, @Nullable ContactAddNewInteractor.OnValidateFinishedListener listener) {
        interactor.validateFields(firstName,lastName,phoneNumber,email,this);
    }

    @Override
    public void setActivityResult() {
        view.setActivityResult(true);
    }

    @Override
    public void onFirstNameError() {
        view.setTextFirstNameError();
    }

    @Override
    public void onLastNameError() {
        view.setTextLastNameError();
    }

    @Override
    public void onPhoneNumberError() {
        view.setTextPhoneNumberError();
    }

    @Override
    public void onEmailError() {
        view.setTextEmailError();
    }

    @Override
    public void onValidContact(Contact newContact) {
        if(ConnectivityReceiver.isConnected()){
            interactor.saveContact(newContact,view.getFileToUpload(),this);
        }else {
            view.showErrorConnectionDialog();
        }

    }

    @Override
    public void onPhoneNumberLengthError() {
        view.setTextPhoneNumberLengthError();
    }

    @Override
    public void onErrorCreateContact(Throwable e) {
        view.showErrorConnectionDialog();
    }

    @Override
    public void onSuccessCreateContact() {
        view.onSuccessCreateContact();
    }

    private void addPictureToGallery() {
        view.addPictureToLibrary(mCurrentPhotoPath);
    }
}
