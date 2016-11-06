package app.rbzeta.contactmanager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import app.rbzeta.contactmanager.model.ContactListInteractor;
import app.rbzeta.contactmanager.presenter.ContactListPresenter;
import app.rbzeta.contactmanager.presenter.ContactListPresenterImpl;
import app.rbzeta.contactmanager.view.IContactListView;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Created by Robyn on 06/11/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/src/main/AndroidManifest.xml")
public class ContactListPresenterTest{
    private ContactListPresenter presenter;
    private ContactListInteractor interactor;
    private IContactListView mockView;

    @Before
    public void setup() {

        mockView = Mockito.mock( IContactListView.class );
        interactor = Mockito.mock( ContactListInteractor.class, RETURNS_DEEP_STUBS );

        presenter = new ContactListPresenterImpl( mockView,interactor );
        reset(mockView);
    }

    @Test
    public void testLoadContact() {

        when(interactor.getContacts(any())).thenReturn(true);

        presenter.loadContactData();
    }

    @Test
    public void testLoadFavoriteContact(){
        when(interactor.getFavoriteContacts(any())).thenReturn(true);

        presenter.loadFavoriteContact();
    }
}
