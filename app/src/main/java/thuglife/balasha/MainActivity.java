package thuglife.balasha;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import thuglife.balasha.Fragments.AboutUsFragment;
import thuglife.balasha.Fragments.AdoptionFragment;
import thuglife.balasha.Fragments.AwarenessTrainingFragment;
import thuglife.balasha.Fragments.ChildrenHomeFragment;
import thuglife.balasha.Fragments.DevelopmentCentreFragment;
import thuglife.balasha.Fragments.DomesticAdoptionFragment;
import thuglife.balasha.Fragments.Donate;
import thuglife.balasha.Fragments.EducationSponsorshipFragment;
import thuglife.balasha.Fragments.HomeFragment;
import thuglife.balasha.Fragments.InCountryAdoptionFragment;
import thuglife.balasha.Fragments.InterCountryAdoption;
import thuglife.balasha.Fragments.TestimonialsFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ActionMenuView amvMenu;
    private TextView actionBarTitle;
    private boolean firstTime = true;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        actionBarTitle = (TextView)findViewById(R.id.action_bar_text);
        amvMenu = (ActionMenuView)findViewById(R.id.amvMenu);
        amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
        }

        mDrawerTitle = mTitle = getTitle();

        // load slide menu items, populate them properly first
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<>();
        for(int i = 0; i != 9; i++)
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[i],navMenuIcons.getResourceId(i,-1)));

        // Recycle the typed array
        navMenuIcons.recycle();

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                actionBarTitle.setText(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                actionBarTitle.setText(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        if(savedInstanceState == null) {
            displayView(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, amvMenu.getMenu());
        return true;
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if(f instanceof HomeFragment)
            super.onBackPressed();
        else if(f instanceof DomesticAdoptionFragment || f instanceof InCountryAdoptionFragment || f instanceof InterCountryAdoption) {
            displayView(2);
            amvMenu.getMenu().getItem(0).setVisible(false);
        }
        else
            displayView(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.back_button:
                displayView(2);
                amvMenu.getMenu().getItem(0).setVisible(false);
                if(getSupportActionBar()!=null)
                    actionBarTitle.setText("Adoption");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        actionBarTitle.setText(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //add proper string urls here
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.facebook_image:
                startBrowser(null);
                break;
            case R.id.youtube_image:
                startBrowser(Uri.parse("http://youtube.com/balashatrust"));
                break;
            case R.id.instagram_image:
                startBrowser(null);
                break;
            case R.id.domestic_adoption:
                displayView(9);
                break;
            case R.id.in_country_adoption:
                displayView(10);
                break;
            case R.id.inter_country_adoption:
                displayView(11);
                break;
            case R.id.uk_donor_link:
                startBrowser(Uri.parse(((TextView)view).getText().toString()));
                break;
            case R.id.india_donor_link:
                startBrowser(Uri.parse(((TextView)view).getText().toString()));
                break;
            case R.id.us_donor_link:
                startBrowser(Uri.parse(((TextView)view).getText().toString()));
                break;
            default:
                break;
        }

    }

    private void startBrowser(Uri uri){
        if(uri != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }


    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new ChildrenHomeFragment();
                break;
            case 2:
                fragment = new AdoptionFragment();
                break;
            case 3:
                fragment = new DevelopmentCentreFragment();
                break;
            case 4:
                fragment = new EducationSponsorshipFragment();
                break;
            case 5:
                fragment = new AwarenessTrainingFragment();
                break;
            case 6:
                fragment = new Donate();
                break;
            case 7:
                fragment = new TestimonialsFragment();
                break;
            case 8:
                fragment = new AboutUsFragment();
                break;
            case 9:
                fragment = new DomesticAdoptionFragment();
                actionBarTitle.setText("Domestic Adoption");
                break;
            case 10:
                fragment = new InCountryAdoptionFragment();
                actionBarTitle.setText("Selection of Child");
                break;
            case 11:
                fragment = new InterCountryAdoption();
                actionBarTitle.setText("Inter Country Adoption");
                break;

            default:
                break;
        }

        if (fragment != null && position < 9) {
            if(!firstTime && amvMenu.getMenu().getItem(0).isVisible())
                amvMenu.getMenu().getItem(0).setVisible(false);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container,fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
            firstTime = false;
        } else if(fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container,fragment).commit();
            amvMenu.getMenu().getItem(0).setVisible(true);
        }
        else{
            Log.e("Avi","Error creating fragment");
        }
    }
}

