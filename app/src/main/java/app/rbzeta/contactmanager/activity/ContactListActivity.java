package app.rbzeta.contactmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import app.rbzeta.contactmanager.R;
import app.rbzeta.contactmanager.adapter.ViewPagerAdapter;
import app.rbzeta.contactmanager.fragment.AllContactsFragment;
import app.rbzeta.contactmanager.fragment.FavoriteContacsFragment;

public class ContactListActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String EXTRA_CONTACT_ID = "app.rbzeta.contactmanager.activity.EXTRA_CONTACT_ID";
    public static final int REQUEST_CODE_NEW_CONTACT = 1 ;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.activity_title_contact_list));

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpagerHome);
        tabLayout = (TabLayout) findViewById(R.id.tabHome);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add_contact) {
            navigateToAddNewContact();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {

        int vId = v.getId();

        switch (vId){
            case R.id.fab:
                navigateToAddNewContact();
                break;
        }

    }

    public void navigateToAddNewContact() {
        Intent intent = new Intent(this,ContactAddNewActivity.class);
        startActivityForResult(intent,ContactListActivity.REQUEST_CODE_NEW_CONTACT);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(AllContactsFragment.getInstance(null),
                getString(R.string.title_fragment_all_contact));
        adapter.addFragment(new FavoriteContacsFragment(),
                getString(R.string.title_fragment_favorite_contact));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof AllContactsFragment){
                fragment.onActivityResult(requestCode,resultCode,data);
            }
        }
    }
}
