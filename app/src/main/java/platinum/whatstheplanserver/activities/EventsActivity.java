package platinum.whatstheplanserver.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.activities.authentications.SignInActivity;
import platinum.whatstheplanserver.adapters.EventsOrVenuesPagerAdapter;
import platinum.whatstheplanserver.fragments.HotEventsFragment;
import platinum.whatstheplanserver.fragments.NewEventFragment;

public class EventsActivity extends AppCompatActivity {

    private static final String TAG = "EventsActivityTag";

    private TabLayout tabLayoutTL;
    private ViewPager viewPagerVP;
    private EventsOrVenuesPagerAdapter mEventsPagerAdapter;
    private List<Fragment> mFragmentList;
    private FirebaseUser mCurrentUser;
    private Boolean mDifferentViewPagerCurrentTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        getCurrentUser ();
        initViewsAndVariables ();
        performActions ();
    }

    private void getCurrentUser() {
        mCurrentUser = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser == null) {
            navigateToNewActivity (SignInActivity.class);
        }
    }

    private void navigateToNewActivity(Class classname) {
        Intent intent = new Intent(EventsActivity.this, classname);
        startActivity(intent);
    }

    private void performActions() {
        tabLayoutTLActions ();
    }

    private void tabLayoutTLActions() {
        viewPagerVP.setAdapter(mEventsPagerAdapter);
        tabLayoutTL.setupWithViewPager(viewPagerVP);
        tabLayoutTL.getTabAt(0).setCustomView(R.layout.tabview_newevent);
        tabLayoutTL.getTabAt(1).setCustomView(R.layout.tabview_hotevents);
        if (mDifferentViewPagerCurrentTab) {
            viewPagerVP.setCurrentItem(tabLayoutTL.getTabAt(1).getPosition());
        }

    }


    private void initViewsAndVariables() {

        mDifferentViewPagerCurrentTab  = false;
        if (getIntent().getBooleanExtra("mDifferentViewPagerCurrentTab", false)) {
            mDifferentViewPagerCurrentTab = true;
        }
        Log.d(TAG, "initViewsAndVariables: mDifferentViewPagerCurrentTab = " + mDifferentViewPagerCurrentTab);
        tabLayoutTL = findViewById(R.id.tabLayout_TL);
        viewPagerVP = findViewById(R.id.viewpager_VP);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(NewEventFragment.newInstance());
        mFragmentList.add(HotEventsFragment.newInstance());
        mEventsPagerAdapter = new EventsOrVenuesPagerAdapter(getSupportFragmentManager(), mFragmentList);

    }
}
