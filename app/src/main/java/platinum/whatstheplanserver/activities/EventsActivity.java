package platinum.whatstheplanserver.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.adapters.EventActionsPagerAdapter;
import platinum.whatstheplanserver.fragments.HotEventsFragment;
import platinum.whatstheplanserver.fragments.NewEventFragment;
import platinum.whatstheplanserver.fragments.PastEventsFragment;

public class EventsActivity extends AppCompatActivity {

    private TabLayout tabLayoutTL;
    private ViewPager viewPagerVP;
    private EventActionsPagerAdapter mEventActionsPagerAdapter;
    private List<Fragment> mFragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        initViewsAndVariables ();
        performActions ();
    }

    private void performActions() {
        tabLayoutTLActions ();
    }

    private void tabLayoutTLActions() {
        viewPagerVP.setAdapter(mEventActionsPagerAdapter);
        tabLayoutTL.setupWithViewPager(viewPagerVP);
        tabLayoutTL.getTabAt(0).setCustomView(R.layout.tabview_newevent);
        tabLayoutTL.getTabAt(1).setCustomView(R.layout.tabview_hotevents);
        tabLayoutTL.getTabAt(2).setCustomView(R.layout.tabview_pastevents);

    }


    private void initViewsAndVariables() {

        tabLayoutTL = findViewById(R.id.tabLayout_TL);
        viewPagerVP = findViewById(R.id.viewpager_VP);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(NewEventFragment.newInstance());
        mFragmentList.add(HotEventsFragment.newInstance());
        mFragmentList.add(PastEventsFragment.newInstance());
        mEventActionsPagerAdapter = new EventActionsPagerAdapter(getSupportFragmentManager(), mFragmentList);

    }
}
