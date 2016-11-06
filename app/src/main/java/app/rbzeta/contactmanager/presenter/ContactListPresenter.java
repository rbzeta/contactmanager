package app.rbzeta.contactmanager.presenter;

/**
 * Created by Robyn on 05/11/2016.
 */

public interface ContactListPresenter {
    void rxUnSubscribe();

    void navigateToContactDetail(int position);

    void loadContactData();

    void loadFavoriteContact();
}
