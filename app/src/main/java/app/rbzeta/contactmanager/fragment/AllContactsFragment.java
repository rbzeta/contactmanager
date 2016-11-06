package app.rbzeta.contactmanager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.rbzeta.contactmanager.R;
import app.rbzeta.contactmanager.activity.ContactAddNewActivity;
import app.rbzeta.contactmanager.activity.ContactDetailActivity;
import app.rbzeta.contactmanager.activity.ContactListActivity;
import app.rbzeta.contactmanager.adapter.ContactListAdapter;
import app.rbzeta.contactmanager.listener.RecyclerTouchListener;
import app.rbzeta.contactmanager.model.Contact;
import app.rbzeta.contactmanager.presenter.ContactListPresenter;
import app.rbzeta.contactmanager.presenter.ContactListPresenterImpl;
import app.rbzeta.contactmanager.view.IContactListView;

import static android.app.Activity.RESULT_OK;
import static app.rbzeta.contactmanager.activity.ContactListActivity.REQUEST_CODE_NEW_CONTACT;

/**
 * Created by Robyn on 05/11/2016.
 */
public class AllContactsFragment extends Fragment  implements IContactListView,
        RecyclerTouchListener.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private TextView textNoContactFound;
    private ProgressBar progress;
    private View mView;

    private ContactListAdapter mAdapter;
    private List<Contact> mContactList;
    private ContactListPresenter presenter;

    public AllContactsFragment(){

    }

    public static AllContactsFragment getInstance(Bundle bundle){
        AllContactsFragment frag = new AllContactsFragment();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new ContactListPresenterImpl(this);
        presenter.loadContactData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_contact_list,container,false);

        textNoContactFound = (TextView)view.findViewById(R.id.text_no_contact_found);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_contact_list);
        progress = (ProgressBar)view.findViewById(R.id.progress_bar);

        mContactList = new ArrayList<>();
        mAdapter = new ContactListAdapter(getContext(),mContactList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),this));

        mView = view;
        return view;
    }

    @Override
    public void setRecycleViewItems(List<Contact> contactList) {
        for (Contact contact : contactList) {
            mContactList.add(contact);
            mAdapter.notifyItemInserted(mAdapter.getItemCount());
        }
    }

    @Override
    public void showProgressBar() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showContactNotFound(boolean show) {
        textNoContactFound.setVisibility((show)?View.VISIBLE:View.GONE);
    }

    @Override
    public void showErrorConnectionSnackBar() {
        Snackbar.make(progress,
                getString(R.string.err_no_connection),
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void navigateToContactDetail(int position) {
        int contactId = mContactList.get(position).getId();
        Intent intent = new Intent(getContext(),ContactDetailActivity.class);
        intent.putExtra(ContactListActivity.EXTRA_CONTACT_ID,contactId);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
        presenter.navigateToContactDetail(position);
    }

    @Override
    public void onDestroy() {
        presenter.rxUnSubscribe();
        super.onDestroy();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_NEW_CONTACT){
            if (resultCode == RESULT_OK){
                if (data.getExtras().getBoolean(ContactAddNewActivity.MSG_KEY_ADD_CONTACT_RESULT)){
                    mContactList.clear();
                    mAdapter.notifyDataSetChanged();
                    presenter.loadContactData();
                }
            }
        }

    }
}
