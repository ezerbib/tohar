package com.software.tohar;




import static android.provider.CalendarContract.EXTRA_EVENT_ALL_DAY;
import static android.provider.CalendarContract.EXTRA_EVENT_BEGIN_TIME;
import static android.provider.CalendarContract.EXTRA_EVENT_END_TIME;


import java.util.List;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract.Attendees;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SearchView;
import android.widget.SearchView.OnSuggestionListener;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class ToharActivity extends Activity implements 
OnSharedPreferenceChangeListener, SearchView.OnQueryTextListener, ActionBar.TabListener,
ActionBar.OnNavigationListener, OnSuggestionListener {
	 private static final String EVENT_INFO_FRAGMENT_TAG = "EventInfoFragment";
	private static final String BUNDLE_KEY_RESTORE_TIME = "key_restore_time";
	private static final String BUNDLE_KEY_EVENT_ID = "key_event_id";
	private static final String BUNDLE_KEY_RESTORE_VIEW = "key_restore_view";
	private static final String BUNDLE_KEY_CHECK_ACCOUNTS = "key_check_for_accounts";
	  // Indices of buttons for the drop down menu (tabs replacement)
    // Must match the strings in the array buttons_list in arrays.xml and the
    // OnNavigationListener
    private static final int BUTTON_DAY_INDEX = 0;
    private static final int BUTTON_WEEK_INDEX = 1;
    private static final int BUTTON_MONTH_INDEX = 2;
    private static final int BUTTON_AGENDA_INDEX = 3;
    
    private static boolean mIsMultipane;
    private static boolean mIsTabletConfig;
	  private ActionBar mActionBar;
	  private String mTimeZone;
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.activity_tohar);
		 // Get time from intent or icicle
        long timeMillis = -1;
        int viewType = -1;
        final Intent intent = getIntent();
        if (icicle != null) {
            timeMillis = icicle.getLong(BUNDLE_KEY_RESTORE_TIME);
            viewType = icicle.getInt(BUNDLE_KEY_RESTORE_VIEW, -1);
        } else {
            String action = intent.getAction();
            if (Intent.ACTION_VIEW.equals(action)) {
                // Open EventInfo later
                timeMillis = parseViewAction(intent);
            }

            if (timeMillis == -1) {
          //      timeMillis = Utils.timeFromIntentInMillis(intent);
            }
        }

      //  if (viewType == -1 || viewType > ViewType.MAX_VALUE) {
      //      viewType = Utils.getViewTypeFromIntentAndSharedPref(this);
      //  }
      //  mTimeZone = Utils.getTimeZone(this, mHomeTimeUpdater);
        //Time t = new Time(mTimeZone);
        Time t = new Time();
        t.set(timeMillis);
		 // configureActionBar auto-selects the first tab you add, so we need to
        // call it before we set up our own fragments to make sure it doesn't
        // overwrite us
        configureActionBar(viewType);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.

	}

	@Override
	public boolean onSuggestionSelect(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSuggestionClick(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
	}
	 private void createButtonsSpinner(int viewType, boolean tabletConfig) {
	        // If tablet configuration , show spinner with no dates
	       //EZ-TODO mActionBarMenuSpinnerAdapter = new CalendarViewAdapter (this, viewType, !tabletConfig);
	        mActionBar = getActionBar();
	        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	        //mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	        
	    //    mActionBar.setListNavigationCallbacks(mActionBarMenuSpinnerAdapter, this);
	        switch (viewType) {
/*	            case ViewType.AGENDA:
	                mActionBar.setSelectedNavigationItem(BUTTON_AGENDA_INDEX);
	                break;
	            case ViewType.DAY:
	                mActionBar.setSelectedNavigationItem(BUTTON_DAY_INDEX);
	                break;
	            case ViewType.WEEK:
	                mActionBar.setSelectedNavigationItem(BUTTON_WEEK_INDEX);
	                break;
	            case ViewType.MONTH:
	                mActionBar.setSelectedNavigationItem(BUTTON_MONTH_INDEX);
	                break;
	                */
	            default:
	                mActionBar.setSelectedNavigationItem(BUTTON_DAY_INDEX);
	                break;
	       }
	    }
	 private void configureActionBar(int viewType) {
	        createButtonsSpinner(viewType, mIsTabletConfig);
	        if (mIsMultipane) {
	            mActionBar.setDisplayOptions(
	                    ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
	        } else {
	            mActionBar.setDisplayOptions(0);
	        }
	    }
	 
	  private long parseViewAction(final Intent intent) {
	        long timeMillis = -1;
	        Uri data = intent.getData();
	        if (data != null && data.isHierarchical()) {
	            List<String> path = data.getPathSegments();
	            if (path.size() == 2 && path.get(0).equals("events")) {
	                try {
/*	                    mViewEventId = Long.valueOf(data.getLastPathSegment());
	                    if (mViewEventId != -1) {
	                        mIntentEventStartMillis = intent.getLongExtra(EXTRA_EVENT_BEGIN_TIME, 0);
	                        mIntentEventEndMillis = intent.getLongExtra(EXTRA_EVENT_END_TIME, 0);
	                        mIntentAttendeeResponse = intent.getIntExtra(
	                            ATTENDEE_STATUS, Attendees.ATTENDEE_STATUS_NONE);
	                        mIntentAllDay = intent.getBooleanExtra(EXTRA_EVENT_ALL_DAY, false);
	                        timeMillis = mIntentEventStartMillis;
	                    }
	                    */
	                } catch (NumberFormatException e) {
	                    // Ignore if mViewEventId can't be parsed
	                }
	            }
	        }
	        return timeMillis;
	    }
}
