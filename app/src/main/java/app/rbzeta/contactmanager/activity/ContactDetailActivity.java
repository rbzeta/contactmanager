package app.rbzeta.contactmanager.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import app.rbzeta.contactmanager.R;
import app.rbzeta.contactmanager.custom.CircleTransform;
import app.rbzeta.contactmanager.helper.StringUtils;
import app.rbzeta.contactmanager.model.Contact;
import app.rbzeta.contactmanager.presenter.ContactDetailPresenter;
import app.rbzeta.contactmanager.presenter.ContactDetailPresenterImpl;
import app.rbzeta.contactmanager.view.IContactDetailView;

public class ContactDetailActivity extends AppCompatActivity implements IContactDetailView,
View.OnClickListener{

    private ImageView imageProfile,imgFavoriteIcon;
    private TextView textName,textPhone,textEmail,textSMS,textShare;

    private ContactDetailPresenter presenter;
    private Contact mContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.activity_title_contact_detail));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> presenter.onBackPressed());

        imageProfile = (ImageView) findViewById(R.id.img_contact_detail);
        imgFavoriteIcon = (ImageView) findViewById(R.id.img_favorite_icon);
        textName = (TextView) findViewById(R.id.text_contact_detail_name);
        textPhone = (TextView) findViewById(R.id.text_contact_detail_phone_number);
        textEmail = (TextView) findViewById(R.id.text_contact_detail_email);
        textSMS = (TextView) findViewById(R.id.text_contact_detail_sms);
        textShare = (TextView)findViewById(R.id.text_contact_detail_share);
        textName.setOnClickListener(this);
        textPhone.setOnClickListener(this);
        textSMS.setOnClickListener(this);
        textShare.setOnClickListener(this);

        presenter = new ContactDetailPresenterImpl(this);

        presenter.loadContactDetail(getIntent().getExtras().getInt(ContactListActivity.EXTRA_CONTACT_ID));

    }

    @Override
    public void openSendTextMessageActivity() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.fromParts("sms",textPhone.getText().toString(),null));
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }

    }

    @Override
    public void loadContactDetail(Contact contact) {
        if (contact != null) {
            mContact = contact;

            Glide.with(this).load(contact.getProfilePic())
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .placeholder(R.drawable.ic_account_circle_black_24dp)
                    .error(R.drawable.ic_account_circle_black_24dp)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageProfile);

            setFavoriteIcon(contact.getFavorite());

            textName.setText(contact.getFirstName()+ " " +contact.getLastName());
            textPhone.setText(contact.getPhoneNumber());
            textEmail.setText(contact.getEmail());

        }

    }

    @Override
    public void showToastErrorUpdate(String message) {
        Toast.makeText(ContactDetailActivity.this,
                getString(R.string.err_update_contact),Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToastSuccessUpdateContact(Contact contact) {
        Toast.makeText(this,
                getString(R.string.msg_success_update_contact),Toast.LENGTH_LONG).show();
    }

    @Override
    public void openShareContactActivity() {
        String text = StringUtils.createContactAsText(textName.getText().toString(),
                textPhone.getText().toString(),
                textEmail.getText().toString());

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void setFavoriteIcon(boolean favorite) {
        if (favorite){
            imgFavoriteIcon.setImageResource(R.drawable.ic_favorite_black_24dp);
        }else
            imgFavoriteIcon.setImageResource(R.drawable.ic_favorite_border_black_24dp);
    }

    @Override
    public void openDialerActivity() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+textPhone.getText()));
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }

    }

    @Override
    public void updateContactAsFavorite() {
        presenter.updateContactAsFavorite(mContact,null);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();

        switch (vId){
           case R.id.text_contact_detail_phone_number:
                presenter.openDialerActivity();
                break;
            case R.id.text_contact_detail_name:
                presenter.updateContactAsFavorite(mContact,null);
                break;
            case R.id.text_contact_detail_sms:
                presenter.openSendTextMessageActivity();
                break;
            case R.id.text_contact_detail_share:
                presenter.openShareContactActivity();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.rxUnSubscribe();
        super.onDestroy();
    }
}
