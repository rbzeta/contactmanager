package app.rbzeta.contactmanager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import app.rbzeta.contactmanager.model.ContactAddNewInteractor;
import app.rbzeta.contactmanager.presenter.ContactAddNewPresenter;
import app.rbzeta.contactmanager.presenter.ContactAddNewPresenterImpl;
import app.rbzeta.contactmanager.view.IContactAddNewView;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Created by Robyn on 06/11/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/src/main/AndroidManifest.xml")
public class ContactAddNewPresenterTest{
    private ContactAddNewPresenter presenter;
    private ContactAddNewInteractor interactor;
    private IContactAddNewView mockView;

    @Before
    public void setup() {

        mockView = Mockito.mock( IContactAddNewView.class );
        interactor = Mockito.mock( ContactAddNewInteractor.class, RETURNS_DEEP_STUBS );

        presenter = new ContactAddNewPresenterImpl( mockView,interactor );
        reset(mockView);
    }

    @Test
    public void testSubmitContact() {
        when(interactor.validateFields(eq("First"),eq("Last"),eq("0927282722"),eq("test.email@email.com"),any()))
                .thenReturn(true);

        presenter.submitContact(eq("First"),eq("Last"),eq("0927282722"),eq("test.email@email.com"),any());
    }
}
