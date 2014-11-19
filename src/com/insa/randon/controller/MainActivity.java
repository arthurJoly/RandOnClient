package com.insa.randon.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.insa.randon.R;

public class MainActivity extends BaseActivity {

	private List<NavigationItem> navigationList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Context context;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        
        mTitle = mDrawerTitle = getTitle();
        
        navigationList = populateNavigationMenu();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<NavigationItem>(this,R.layout.drawer_list_item, navigationList));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        //initialize the navigationDrawer to the fragment asked
        Intent intent = getIntent();
        String fragmentName = intent.getStringExtra(HomeActivity.FRAGMENT_EXTRA);
        if (fragmentName != null){
        	FragmentManager fragmentManager = getFragmentManager();
        	Fragment newFragment = Fragment.instantiate(this, fragmentName);
        	fragmentManager.beginTransaction().replace(R.id.content_frame, newFragment).commit();
        }       
        
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
	    super.onPostCreate(savedInstanceState);
	    // Sync the toggle state after onRestoreInstanceState has occurred.
	    mDrawerToggle.syncState();
	}
	
	/* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	//          Fragment fragment = new DrawerFragment();
        	//          Bundle args = new Bundle();
        	//          args.putInt(DrawerFragment.ARG_LIST_NUMBER, position);
        	//          fragment.setArguments(args);

        	FragmentManager fragmentManager = getFragmentManager();

        	//Identify the fragment to show
        	NavigationItem navItem = (NavigationItem) parent.getItemAtPosition(position);
        	Fragment newFragment = Fragment.instantiate(context, navItem.getFragmentName());
        	fragmentManager.beginTransaction().replace(R.id.content_frame, newFragment).commit();

        	// update selected item and title, then close the drawer
        	mDrawerList.setItemChecked(position, true);
        	setTitle(navItem.getTitle());
        	mDrawerLayout.closeDrawer(mDrawerList);          
        }
    }
    
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    
    
    private List<NavigationItem> populateNavigationMenu (){
    	List<NavigationItem> items = new ArrayList<NavigationItem>();
    	items.add(new NavigationItem(NewHikeFragment.class.getName(), getResources().getString(R.string.new_hike_title)));
    	items.add(new NavigationItem(HikeSearchFragment.class.getName(), getResources().getString(R.string.hike_search_title)));
    	items.add(new NavigationItem(HikeListFragment.class.getName(), getResources().getString(R.string.hike_list_title)));
    	return items;
    }


    private class NavigationItem {
    	private String fragmentName;
    	private String title;
    	
    	public String getFragmentName() {
    		return fragmentName;
    	}
    	
    	public String getTitle() {
    		return title;
    	}
    	
    	public NavigationItem(String fragmentName, String title) {
    		this.fragmentName = fragmentName;
    		this.title = title;
    	}
    	
    	@Override
    	public String toString() {
    		return title;
    	}
    }
}
    
    