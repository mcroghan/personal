package com.rudetheology.whatcounts;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

public class ActivityMain extends FragmentActivity implements
        ActionBar.TabListener {

    public static final int COUNTERS_TAB = 0;
    public static final int GRAPH_TAB = 1;
    
    public static final String FAT = "fat";
    public static final String CARBS = "carbs";
    public static final String SWEAT = "sweat";
    public static final String COIN = "coin";
    
    public static final String DATE_FORMAT = "yyyy-MM-dd";
        
    private Date currentDate;
    
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    
    public ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        sectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
                
                if (position == GRAPH_TAB) {
                    FragmentGraph fragment = (FragmentGraph)getFragmentForTab(GRAPH_TAB);
                    if(fragment != null && fragment.getView() != null) fragment.refresh();

                } else if (position == COUNTERS_TAB) {
                    FragmentCounters cf = (FragmentCounters)getFragmentForTab(COUNTERS_TAB);                            
                    cf.setDate();
                    cf.blurAllTheThings();
                }
                dismissKeyboard();
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < sectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                    .setText(sectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }
        
        setCurrentDate(new Date());
    }
    
    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewPager.getApplicationWindowToken(), 0);       
    }
    
    public String getToday () {
        String retVal = "";
        SimpleDateFormat sdf = new SimpleDateFormat(ActivityMain.DATE_FORMAT, Locale.US);
        retVal = sdf.format(new Date());
        return retVal;
    }
    
    private Fragment getFragmentForTab (int tab) {
        Fragment retVal = getSupportFragmentManager()
                .findFragmentByTag("android:switcher:" + viewPager.getId() + ":" + tab);
        return retVal;
    }
    
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_about:
                AlertDialog.Builder builder = new Builder(this);
                PackageInfo pInfo;
                try {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                    return true;
                }
                String version = pInfo.versionName;
                String message = getString(R.string.app_name) + "\n"
                        + "version " + version + "\n\n"
                        + getString(R.string.about_text);
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { 
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction) {
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
            case COUNTERS_TAB:
                fragment = new FragmentCounters();
                break;
            case GRAPH_TAB:
                fragment = new FragmentGraph();
                break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
            case COUNTERS_TAB:
                return getString(R.string.title_section1).toUpperCase(l);
            case GRAPH_TAB:
                return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }    
}
