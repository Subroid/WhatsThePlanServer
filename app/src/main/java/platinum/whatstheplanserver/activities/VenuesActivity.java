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
import platinum.whatstheplanserver.adapters.EventActionsPagerAdapter;
import platinum.whatstheplanserver.fragments.HotEventsFragment;
import platinum.whatstheplanserver.fragments.HotVenuesFragment;
import platinum.whatstheplanserver.fragments.NewEventFragment;
import platinum.whatstheplanserver.fragments.NewVenueFragment;
import platinum.whatstheplanserver.fragments.PastEventsFragment;
import platinum.whatstheplanserver.fragments.PastVenuesFragment;

public class VenuesActivity extends AppCompatActivity {

    private static final String TAG = "VenuesActivityTag";

    private TabLayout tabLayoutTL;
    private ViewPager viewPagerVP;
    private EventActionsPagerAdapter mEventActionsPagerAdapter;
    private List<Fragment> mFragmentList;
    private FirebaseUser mCurrentUser;
    private Boolean mDifferentViewPagerCurrentTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venues);

        getCurrentUser ();
        initViewsAndVariables ();
        performActions ();
    }

    private void performActions() {
        tabLayoutTLActions ();
    }

    private void tabLayoutTLActions() {
        viewPagerVP.setAdapter(mEventActionsPagerAdapter);
        tabLayoutTL.setupWithViewPager(viewPagerVP);
        tabLayoutTL.getTabAt(0).setCustomView(R.layout.tabview_newvenue);
        tabLayoutTL.getTabAt(1).setCustomView(R.layout.tabview_hotvenues);
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
        mFragmentList.add(NewVenueFragment.newInstance());
        mFragmentList.add(HotVenuesFragment.newInstance());
        //todo mVenue..PagerAdapter
        mEventActionsPagerAdapter = new EventActionsPagerAdapter(getSupportFragmentManager(), mFragmentList);

    }

    private void getCurrentUser() {
        mCurrentUser = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser == null) {
            navigateToNewActivity (SignInActivity.class);
        }
    }

    private void navigateToNewActivity(Class classname) {
        Intent intent = new Intent(VenuesActivity.this, classname);
        startActivity(intent);
    }
}
