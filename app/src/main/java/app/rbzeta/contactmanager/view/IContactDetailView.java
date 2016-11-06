package app.rbzeta.contactmanager.view;

import app.rbzeta.contactmanager.model.Contact;

/**
 * Created by Robyn on 05/11/2016.
 */
public interface IContactDetailView {
    void onBackPressed();

    void openDialerActivity();

    void updateContactAsFavorite();

    void openSendTextMessageActivity();

    void loadContactDetail(Contact contact);

    void showToastErrorUpdate(String message);

    void setFavoriteIcon(boolean favorite);

    void showToastSuccessUpdateContact(Contact contact);

    void openShareContactActivity();
}
