package app.rbzeta.contactmanager.model;

/**
 * Created by Robyn on 05/11/2016.
 */

public interface ContactDetailInteractor {

    boolean updateContactDB(Contact contact);

    interface OnFinishedUpdateContactListener {

        void onFinishedUpdateContact(Contact contact);

        void onErrorUpdateContact(Throwable t);
    }
    void rxUnSubscribe();

    Contact getContactFromDBById(int contactId);

    boolean updateContactAsFavorite(Contact contact,OnFinishedUpdateContactListener listener);
}
