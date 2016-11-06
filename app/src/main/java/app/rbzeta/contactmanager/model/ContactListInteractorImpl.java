package app.rbzeta.contactmanager.model;

import java.util.List;

import app.rbzeta.contactmanager.application.MyApplication;
import app.rbzeta.contactmanager.helper.MyDBHandler;
import app.rbzeta.contactmanager.rest.NetworkService;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * Created by Robyn on 05/11/2016.
 */

public class ContactListInteractorImpl implements ContactListInteractor {
    private NetworkService service;
    private MyDBHandler dbHandler;
    private Subscription subscription;

    public ContactListInteractorImpl() {
        this.service = MyApplication.getInstance().getNetworkService();
        this.dbHandler = MyApplication.getInstance().getDBHandler();
    }

    @Override
    public void iterateContactList(List<Contacts> contactses, OnFinishedLoadContactListener listener) {

        Observable<Contact> contactObservable = Observable.from(contactses)
                .flatMap(contacts -> ((Observable<Contact>)service.getPreparedObservable(service.getNetworkAPI().getContact(contacts.getId()),Contact.class, false, false))
                        .doOnError(throwable -> {
                            rxUnSubscribe();
                            listener.onErrorLoadingContact(throwable);
                        })
                        .doOnNext(this::insertContactIntoDB))
                .doOnCompleted(() -> listener.onFinishedLoadingContact(getContactListFromDB()));

        rxUnSubscribe();

        subscription = contactObservable.subscribe();
    }

    @Override
    public boolean getFavoriteContacts(OnFinishedLoadContactListener listener) {
        List<Contact> contacts = dbHandler.getFavoriteContacts();
        dbHandler.close();

        if (contacts != null) {
            listener.onFinishedLoadingContact(contacts);
        }else
            listener.onErrorLoadingFavoriteContact();

        return true;
    }

    @Override
    public boolean getContacts(OnFinishedLoadContactListener listener) {
        Observable<List<Contacts>> contactListObservable = (Observable<List<Contacts>>)
                service.getPreparedObservable(
                        service.getNetworkAPI().fetchContactList(),
                        ResponseBody.class,
                        false,
                        false
                );

        //to much work, sockettimeout exception on slow connection
        /*subscription =
                contactListObservable
                        .doOnNext(contactses -> System.out.println("Total Contacts : "+contactses.size()))
                        .doOnCompleted(()->deleteContactDB())
                        .flatMapIterable(contactses -> contactses)
                        .flatMap(contacts -> ((Observable<Contact>)service
                                .getPreparedObservable(
                                        service.getNetworkAPI()
                                        .getContact(contacts.getId()),
                                        Contact.class, false, false))
                                .doOnNext(this::insertContactIntoDB))
                        .doOnCompleted(() -> listener.onFinishedLoadingContact(getContactListFromDB()))
                        .doOnError(throwable -> {
                            listener.onErrorLoadingContact(throwable);
                        })
                        .subscribe();*/

        subscription = contactListObservable.subscribe(new Observer<List<Contacts>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                rxUnSubscribe();
                listener.onErrorLoadingContact(e);

            }

            @Override
            public void onNext(List<Contacts> contactses) {
                deleteContactDB();
                listener.onFinishedLoadingContactList(contactses);
            }
        });
        return true;
    }

    public List<Contact> getContactListFromDB() {
        List<Contact> contactList = dbHandler.getContactList();
        dbHandler.close();

        return contactList;
    }

    @Override
    public void rxUnSubscribe() {
        if(subscription!=null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    @Override
    public void deleteContactDB() {
        dbHandler.deleteContactList();
        dbHandler.close();
    }

    @Override
    public void insertContactIntoDB(Contact contact) {
        dbHandler.insertContact(contact);
        dbHandler.close();
    }
}
