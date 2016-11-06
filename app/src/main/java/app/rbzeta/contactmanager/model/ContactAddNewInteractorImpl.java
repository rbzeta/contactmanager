package app.rbzeta.contactmanager.model;

import java.io.File;

import app.rbzeta.contactmanager.application.MyApplication;
import app.rbzeta.contactmanager.helper.MyDBHandler;
import app.rbzeta.contactmanager.helper.StringUtils;
import app.rbzeta.contactmanager.rest.NetworkService;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * Created by Robyn on 04/11/2016.
 */

public class ContactAddNewInteractorImpl implements ContactAddNewInteractor {
    private NetworkService service;
    private MyDBHandler dbHandler;
    private Subscription subscription;

    public ContactAddNewInteractorImpl() {
        this.service = MyApplication.getInstance().getNetworkService();
        this.dbHandler = MyApplication.getInstance().getDBHandler();
    }

    @Override
    public boolean saveContact(Contact newContact, File fileToUpload, OnFinishedCreateContactListener listener) {

        //no documented end point about where to upload the image file
        /*RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),fileToUpload);

        MultipartBody.Part image = MultipartBody.Part
                .createFormData("picture",
                        fileToUpload.getName(),
                        requestFile);*/

        Observable<Contact> contactResponseObservable = (Observable<Contact>)
                service.getPreparedObservable(
                        service.getNetworkAPI().insertContact(newContact),
                        Contact.class,
                        false,
                        false
                );

        subscription = contactResponseObservable.subscribe(new Observer<Contact>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                rxUnSubscribe();
                listener.onErrorCreateContact(e);
            }

            @Override
            public void onNext(Contact contact) {
                dbHandler.insertContact(contact);
                listener.onSuccessCreateContact();
            }
        });
    return true;
    }

    @Override
    public void rxUnSubscribe() {
        if(subscription!=null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    @Override
    public boolean validateFields(String firstName, String lastName, String phoneNumber, String email,OnValidateFinishedListener listener) {

        if (!StringUtils.validateFirstName(firstName)){
            listener.onFirstNameError();
            return false;
        }else if (!StringUtils.validateLastName(lastName)){
            listener.onLastNameError();
            return false;
        }else if (!StringUtils.validatePhoneLength(phoneNumber)){
            listener.onPhoneNumberLengthError();
            return false;
        }else if (!StringUtils.validatePhoneNumber(phoneNumber)){
            listener.onPhoneNumberError();
            return false;
        }else if (!StringUtils.validateEmailAddress(email)) {
            listener.onEmailError();
            return false;
        }else{
            listener.onValidContact(createNewContact(firstName, lastName, phoneNumber, email));
            return true;
        }
    }

    private Contact createNewContact(String firstName, String lastName, String phoneNumber, String email) {
        Contact c = new Contact();
        c.setFirstName(firstName);
        c.setLastName(lastName);
        c.setPhoneNumber(phoneNumber);
        c.setEmail(email);
        return c;

    }
}
