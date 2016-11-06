package app.rbzeta.contactmanager.presenter;

import java.util.List;

import app.rbzeta.contactmanager.model.Contact;
import app.rbzeta.contactmanager.model.ContactListInteractor;
import app.rbzeta.contactmanager.model.ContactListInteractorImpl;
import app.rbzeta.contactmanager.model.Contacts;
import app.rbzeta.contactmanager.receiver.ConnectivityReceiver;
import app.rbzeta.contactmanager.view.IContactListView;

/**
 * Created by Robyn on 04/11/2016.
 */

public class ContactListPresenterImpl implements ContactListPresenter,ContactListInteractor.OnFinishedLoadContactListener {
    private IContactListView view;
    private ContactListInteractor interactor;

    public ContactListPresenterImpl(IContactListView contactListView) {
        this.view = contactListView;
        interactor = new ContactListInteractorImpl();
    }

    public ContactListPresenterImpl(IContactListView contactListView,ContactListInteractor interactor) {
        this.view = contactListView;
        this.interactor = interactor;
    }

    public void rxUnSubscribe(){
        interactor.rxUnSubscribe();
    }

    @Override
    public void navigateToContactDetail(int position) {
        view.navigateToContactDetail(position);
    }

    @Override
    public void loadContactData() {
        if (view != null) {

            if (ConnectivityReceiver.isConnected()) {
                view.showProgressBar();
                interactor.getContacts(this);

            } else{
                view.showErrorConnectionSnackBar();
                view.hideProgressBar();
            }

        }
    }

    @Override
    public void loadFavoriteContact() {
        if (view != null) {
            interactor.getFavoriteContacts(this);
        }
    }

    @Override
    public void onFinishedLoadingContact(List<Contact> contactList) {
        if (contactList != null || contactList.size() > 0) {
            view.setRecycleViewItems(contactList);
            view.hideProgressBar();
        }else {
            view.showContactNotFound(true);
            view.hideProgressBar();
        }

    }

    @Override
    public void onErrorLoadingContact(Throwable t) {
        view.showContactNotFound(true);
        view.hideProgressBar();
        view.showErrorConnectionSnackBar();
    }

    @Override
    public void onFinishedLoadingContactList(List<Contacts> contactses) {
        interactor.iterateContactList(contactses,this);
    }

    @Override
    public void onErrorLoadingFavoriteContact() {
        view.showContactNotFound(true);
        view.hideProgressBar();
    }
}
