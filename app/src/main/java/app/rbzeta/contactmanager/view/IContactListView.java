package app.rbzeta.contactmanager.view;

import java.util.List;

import app.rbzeta.contactmanager.model.Contact;

/**
 * Created by Robyn on 05/11/2016.
 */
public interface IContactListView {
    void showProgressBar();

    void hideProgressBar();

    void showContactNotFound(boolean show);

    void showErrorConnectionSnackBar();

    void navigateToContactDetail(int position);

    void setRecycleViewItems(List<Contact> contactList);
}
