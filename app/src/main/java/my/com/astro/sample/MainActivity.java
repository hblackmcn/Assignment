package my.com.astro.sample;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import my.com.astro.sample.Tools.Utils;
import my.com.astro.sample.database.DatabaseCreator;
import my.com.astro.sample.database.DbHelper_Channel;
import my.com.astro.sample.fragments.ChannelsFragment;
import my.com.astro.sample.fragments.FavoritesFragment;
import my.com.astro.sample.fragments.TvGuidFragment;
import my.com.astro.sample.singletones.SharedData;

import static my.com.astro.sample.R.id.frame_no_network;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public NavigationView navigationView;
    Toolbar toolbar;

    DbHelper_Channel db_Channel;
    FrameLayout frame_no_network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //represent the No Netwrok layout
        frame_no_network = (FrameLayout) findViewById(R.id.frame_no_network);

        CreateSqLiteDatabseIfNotExist();
        SharedData.initInstance();


        if (Utils.isNetworkAvailable(this)){
            frame_no_network.setVisibility(View.INVISIBLE);

            db_Channel = new DbHelper_Channel(this);
            int count = db_Channel.getDataCount("isFavorite = 1");
            if (count>0) {
                navigationView.getMenu().getItem(1).setChecked(true);
                loadFragment(R.id.nav_favorite);
            }else{
                navigationView.getMenu().getItem(0).setChecked(true);
                loadFragment(R.id.nav_channel);
            }
        }else{
            frame_no_network.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        loadFragment(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void loadFragment(int id) {


        //if there is no netwrk then ignore loading pages
        if (!Utils.isNetworkAvailable(this)){
            frame_no_network.setVisibility(View.VISIBLE);
            return;
        }else{
            frame_no_network.setVisibility(View.INVISIBLE);
        }

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.

            Fragment fragment = null;
            String toolbarTitle = "";
            if (id == R.id.nav_channel) {
                fragment = new ChannelsFragment();
                toolbarTitle = getString(R.string.menu_channels);
            } else if (id == R.id.nav_favorite) {
                fragment = new FavoritesFragment();
                toolbarTitle = getString(R.string.menu_favorites);
            } else if (id == R.id.nav_tv_guid) {
                fragment = new TvGuidFragment();
                toolbarTitle = getString(R.string.menu_tv_guid);
            }

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            fragment.setArguments(getIntent().getExtras());


            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();

            toolbar.setTitle(toolbarTitle);
        }




    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        item.setChecked(item.isChecked());

        switch (item.getItemId()) {
            //case R.id.menu_option_sortby_name:
            //case R.id.menu_option_sortby_no:
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void CreateSqLiteDatabseIfNotExist(){
        DatabaseCreator DC = new DatabaseCreator(this);
        DC.CreateDatabaseIfNotExist();
    }
}
