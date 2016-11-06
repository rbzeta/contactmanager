package app.rbzeta.contactmanager.model;

import app.rbzeta.contactmanager.application.MyApplication;
import app.rbzeta.contactmanager.helper.MyDBHandler;
import app.rbzeta.contactmanager.rest.NetworkService;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * Created by Robyn on 04/11/2016.
 */

public class ContactDetailInteractorImpl implements ContactDetailInteractor{
    private NetworkService service;
    private MyDBHandler dbHandler;
    private Subscription subscription;

    public ContactDetailInteractorImpl() {
        this.service = MyApplication.getInstance().getNetworkService();
        this.dbHandler = MyApplication.getInstance().getDBHandler();
    }

    @Override
    public boolean updateContactDB(Contact contact) {
       return dbHandler.updateContact(contact);
    }

    @Override
    public void rxUnSubscribe() {
        if(subscription!=null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    @Override
    public Contact getContactFromDBById(int contactId) {
        Contact contact;
        contact = dbHandler.getContactById(String.valueOf(contactId));
        dbHandler.close();
        return contact;
    }

    @Override
    public boolean updateContactAsFavorite(Contact contact,
                                        OnFinishedUpdateContactListener listener) {

        contact.setFavorite(true);

        Observable<ResponseBody> updateContactObservable = (Observable<ResponseBody>)
                service.getPreparedObservable(
                        service.getNetworkAPI().updateContact(contact.getId(),contact),
                        ResponseBody.class,
                        false,
                        false);

        subscription = updateContactObservable.subscribe(new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onErrorUpdateContact(e);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                listener.onFinishedUpdateContact(contact);
            }
        });
    return true;
    }
}
