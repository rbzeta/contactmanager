package app.rbzeta.contactmanager.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.IOException;

import app.rbzeta.contactmanager.R;
import app.rbzeta.contactmanager.application.AppConfig;
import app.rbzeta.contactmanager.custom.CircleTransform;
import app.rbzeta.contactmanager.helper.PermissionHelper;
import app.rbzeta.contactmanager.presenter.ContactAddNewPresenter;
import app.rbzeta.contactmanager.presenter.ContactAddNewPresenterImpl;
import app.rbzeta.contactmanager.view.IContactAddNewView;

public class ContactAddNewActivity extends AppCompatActivity implements IContactAddNewView, View.OnClickListener{

    public static final String MSG_KEY_ADD_CONTACT_RESULT = "app.rbzeta.contactmanager.activity.MSG_KEY_ADD_CONTACT_RESULT";

    private EditText textFirstName,textLastName,textEmail,textPhoneNumber;
    private Button buttonSave;
    private ImageView imageProfile;

    private ContactAddNewPresenter presenter;
    private PermissionHelper permissionHelper;

    private static final int REQUEST_IMAGE_FROM_CAMERA = 10;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 11;
    public static final int REQUEST_USE_CAMERA = 1;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    private File fileToUpload = null;
    private File imageFromCamera = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add_new);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.activity_title_contact_add));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> presenter.onBackPressed());

        textFirstName = (EditText) findViewById(R.id.edit_contact_first_name);
        textLastName = (EditText) findViewById(R.id.edit_contact_last_name);
        textEmail = (EditText) findViewById(R.id.edit_contact_email_address);
        textPhoneNumber = (EditText) findViewById(R.id.edit_contact_phone_number);
        buttonSave = (Button)findViewById(R.id.button_save_new_contact);
        imageProfile = (ImageView)findViewById(R.id.img_contact_add);
        imageProfile.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

        presenter = new ContactAddNewPresenterImpl(this);
        permissionHelper = new PermissionHelper(this);

    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId){
            case R.id.img_contact_add:
                showDialogImagePicker(v);
                break;
            case R.id.button_save_new_contact:
                presenter.submitContact(textFirstName.getText().toString().trim(),
                        textLastName.getText().toString().trim(),
                        textPhoneNumber.getText().toString().trim(),
                        textEmail.getText().toString().trim(),null);
                break;
        }
    }

    public void showDialogImagePicker(View v) {

        if(!permissionHelper.mayRequestCamera(v))
            return;
        if(!permissionHelper.mayRequestWriteExternalStorage(v))
            return;

        final CharSequence[] items = {getString(R.string.text_camera),getString(R.string.text_gallery),
                getString(R.string.action_cancel)};
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(getString(R.string.dialog_title_select_image_from));
        alertBuilder.setItems(items, (dialog, which) -> {
            if (items[which].equals(getString(R.string.text_camera))){
                presenter.takePictureFromCamera();
            }else if (items[which].equals(getString(R.string.text_gallery))){
                presenter.takePictureFromGallery();
            }else
                dialog.dismiss();
        });
        alertBuilder.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_USE_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showDialogImagePicker(imageProfile);
            }
        }

        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showDialogImagePicker(imageProfile);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (fileToUpload != null)
                fileToUpload.delete();

            if (requestCode == REQUEST_IMAGE_FROM_CAMERA){

                fileToUpload = presenter.prepareImageCameraForUpload();

                if (fileToUpload != null) {
                    presenter.reloadProfilePicture();
                }
            }else if(requestCode == REQUEST_IMAGE_FROM_GALLERY){

                fileToUpload = presenter.prepareImageGalleryForUpload(data);

                if (fileToUpload != null) {
                    presenter.reloadProfilePicture();
                }
            }
        }else if (resultCode == RESULT_CANCELED){
            if (requestCode == REQUEST_IMAGE_FROM_CAMERA) {

            }
        }
    }

    @Override
    public void takePictureFromGallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.dialog_title_select_picture))
                , REQUEST_IMAGE_FROM_GALLERY);

    }


    @Override
    public void takePictureFromCamera() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                try {
                    imageFromCamera = presenter.createImageFile();
                } catch (IOException ex) {

                }

                if (imageFromCamera != null) {

                    Uri photoURI = FileProvider.getUriForFile(this,AppConfig.STR_FILE_PROVIDER,imageFromCamera);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_FROM_CAMERA);
                }
            }
        } else {
            Snackbar.make(imageProfile, R.string.msg_device_no_camera, Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public void deleteImageFromCamera() {
        if (imageFromCamera != null) {
            imageFromCamera.delete();
        }
    }

    @Override
    public void setTextFirstNameError() {
        textFirstName.requestFocus();
        textFirstName.setError(getString(R.string.err_first_name_length));
    }

    @Override
    public void setTextLastNameError() {
        textLastName.requestFocus();
        textLastName.setError(getString(R.string.err_last_name_length));
    }

    @Override
    public void setTextPhoneNumberError() {
        textPhoneNumber.requestFocus();
        textPhoneNumber.setError(getString(R.string.err_phone_number_not_valid));
    }

    @Override
    public void setTextEmailError() {
        textEmail.requestFocus();
        textEmail.setError(getString(R.string.err_email_not_valid));
    }

    @Override
    public void showErrorConnectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_msg_network_error));
        builder.setPositiveButton(getString(R.string.button_retry), (dialog, id) -> {
            presenter.submitContact(textFirstName.getText().toString().trim(),
                    textLastName.getText().toString().trim(),
                    textPhoneNumber.getText().toString().trim(),
                    textEmail.getText().toString().trim(),null);
            dialog.dismiss();
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public File getFileToUpload() {
        return fileToUpload;
    }

    @Override
    public void showToastErrorCreateContact(Throwable e) {
        Toast.makeText(this,
                getString(R.string.err_create_contact),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessCreateContact() {
        Toast.makeText(ContactAddNewActivity.this,
                getString(R.string.msg_save_contact_successfull),Toast.LENGTH_LONG).show();

        presenter.setActivityResult();
    }

    @Override
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = null;
        try {
            String[] projection = { MediaStore.Images.Media.DATA };
            cursor = this.getContentResolver().query(uri,  projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public int getImageProfileWidth() {
        return imageProfile.getWidth();
    }

    @Override
    public int getImageProfileHeight() {
        return imageProfile.getHeight();
    }

    @Override
    public void reloadProfilePicture() {
        Glide.with(this).load(fileToUpload.getAbsolutePath())
                .crossFade()
                .thumbnail(0.2f)
                .centerCrop()
                .error(R.drawable.ic_account_circle_black_24dp)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageProfile);
    }

    @Override
    public void onBackPressed() {
        if (fileToUpload != null) {
            fileToUpload.delete();
        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        presenter.rxUnSubscribe();
        if (fileToUpload != null) {
            fileToUpload.delete();
        }
        super.onDestroy();
    }

    @Override
    public void finish(){
        super.finish();
    }

    @Override
    public void addPictureToLibrary(String mCurrentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void setTextPhoneNumberLengthError() {
        textPhoneNumber.requestFocus();
        textPhoneNumber.setError(getString(R.string.err_phone_number_length));
    }

    @Override
    public void setActivityResult(boolean b) {
        Intent data = new Intent();
        data.putExtra(ContactAddNewActivity.MSG_KEY_ADD_CONTACT_RESULT,b);
        setResult(RESULT_OK,data);
        finish();
    }
}
