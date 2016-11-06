package app.rbzeta.contactmanager.presenter;

import app.rbzeta.contactmanager.model.Contact;
import app.rbzeta.contactmanager.model.ContactDetailInteractor;

/**
 * Created by Robyn on 05/11/2016.
 */

public interface ContactDetailPresenter {
    void onBackPressed();

    void rxUnSubscribe();

    void openDialerActivity();

    void updateContactAsFavorite(Contact mContact,ContactDetailInteractor.OnFinishedUpdateContactListener listener);

    void openSendTextMessageActivity();

    void loadContactDetail(int contactId);

    void openShareContactActivity();
}
