package app.rbzeta.contactmanager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import app.rbzeta.contactmanager.model.Contact;
import app.rbzeta.contactmanager.model.ContactDetailInteractor;
import app.rbzeta.contactmanager.presenter.ContactDetailPresenter;
import app.rbzeta.contactmanager.presenter.ContactDetailPresenterImpl;
import app.rbzeta.contactmanager.view.IContactDetailView;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Created by Robyn on 06/11/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/src/main/AndroidManifest.xml")
public class ContactDetailPresenterTest{
    private ContactDetailPresenter presenter;
    private ContactDetailInteractor interactor;
    private IContactDetailView mockView;

    @Before
    public void setup() {

        mockView = Mockito.mock( IContactDetailView.class );
        interactor = Mockito.mock( ContactDetailInteractor.class, RETURNS_DEEP_STUBS );

        presenter = new ContactDetailPresenterImpl( mockView,interactor );
        reset(mockView);
    }

    @Test
    public void testLoadContactDetail() {

        int contactId = 44;

        when(interactor.getContactFromDBById(contactId)).thenReturn(any(Contact.class));

        presenter.loadContactDetail(contactId);
    }

    @Test
    public void testUpdateContactAsFavorite(){
        when(interactor.updateContactAsFavorite(any(Contact.class),any())).thenReturn(true);

        presenter.updateContactAsFavorite(any(Contact.class),any());
    }
}
