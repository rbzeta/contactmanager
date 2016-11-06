package app.rbzeta.contactmanager.model;

import java.util.List;

/**
 * Created by Robyn on 05/11/2016.
 */

public interface ContactListInteractor {

    void iterateContactList(List<Contacts> contactses,OnFinishedLoadContactListener listener);

    boolean getFavoriteContacts(OnFinishedLoadContactListener listener);

    interface OnFinishedLoadContactListener {
        void onFinishedLoadingContact(List<Contact> items);

        void onErrorLoadingContact(Throwable t);

        void onFinishedLoadingContactList(List<Contacts> contactses);

        void onErrorLoadingFavoriteContact();
    }

    boolean getContacts(OnFinishedLoadContactListener listener);

    void rxUnSubscribe();

    void deleteContactDB();

    void insertContactIntoDB(Contact contact);

    List<Contact> getContactListFromDB();
}
