package app.rbzeta.contactmanager.rest;

import java.util.List;

import app.rbzeta.contactmanager.application.AppConfig;
import app.rbzeta.contactmanager.model.Contact;
import app.rbzeta.contactmanager.model.Contacts;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Robyn on 05/11/2016.
 */

public interface NetworkApi {

    @GET(AppConfig.CONTACT_LIST_URL)
    Observable<List<Contacts>> fetchContactList();

    @GET(AppConfig.CONTACT_URL)
    Observable<Contact> getContact(@Path("id") int id);

    @PUT(AppConfig.UPDATE_CONTACT_URL)
    Observable<ResponseBody> updateContact(@Path("id") int id, @Body Contact contact);

    @POST(AppConfig.INSERT_CONTACT_URL)
    Observable<Contact> insertContact(@Body Contact contact);
}
