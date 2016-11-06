package app.rbzeta.contactmanager.presenter;

import android.support.annotation.Nullable;

import app.rbzeta.contactmanager.model.Contact;
import app.rbzeta.contactmanager.model.ContactDetailInteractor;
import app.rbzeta.contactmanager.model.ContactDetailInteractorImpl;
import app.rbzeta.contactmanager.view.IContactDetailView;

/**
 * Created by Robyn on 05/11/2016.
 */

public class ContactDetailPresenterImpl implements ContactDetailPresenter
        ,ContactDetailInteractor.OnFinishedUpdateContactListener {
    private IContactDetailView view;
    private ContactDetailInteractor interactor;

    public ContactDetailPresenterImpl(IContactDetailView view) {
        this.view = view;
        this.interactor = new ContactDetailInteractorImpl();
    }

    public ContactDetailPresenterImpl(IContactDetailView view,ContactDetailInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onBackPressed() {
        view.onBackPressed();
    }

    @Override
    public void rxUnSubscribe() {
        interactor.rxUnSubscribe();
    }

    @Override
    public void openDialerActivity() {
        view.openDialerActivity();
    }

    @Override
    public void updateContactAsFavorite(Contact contact,@Nullable ContactDetailInteractor.OnFinishedUpdateContactListener listener) {
        interactor.updateContactAsFavorite(contact,this);
    }

    @Override
    public void openSendTextMessageActivity() {
        view.openSendTextMessageActivity();
    }

    @Override
    public void loadContactDetail(int contactId) {
        view.loadContactDetail(interactor.getContactFromDBById(contactId));
    }

    @Override
    public void openShareContactActivity() {
        view.openShareContactActivity();
    }

    @Override
    public void onFinishedUpdateContact(Contact contact) {

        if (interactor.updateContactDB(contact)){
            view.setFavoriteIcon(contact.getFavorite());
            view.showToastSuccessUpdateContact(contact);
        }else
            view.showToastErrorUpdate("");
    }

    @Override
    public void onErrorUpdateContact(Throwable t) {
        String message = t.getLocalizedMessage();
        view.showToastErrorUpdate(message);
    }
}
