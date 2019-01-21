package platinum.whatstheplanserver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseUser;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.activities.authentications.SignInActivity;
import platinum.whatstheplanserver.models.Venue;

public class NewVenueSecondActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner mVenueMainEventSPNR;
    private EditText mVenueSubEventsET;
    private Button mNextBTN;
    private String [] mMainEvents;
    private FirebaseUser mCurrentUser;
    private Venue mVenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_venue_2);

        getCurrentUser ();
        initViewsAndVariables ();
        performActions ();
    }

    private void performActions() {
        actionsVenueMainEventSPNR ();
        mNextBTNActions ();
    }

    private void actionsVenueMainEventSPNR() {
        ArrayAdapter<String> mainEventsAdapter = new ArrayAdapter<>(NewVenueSecondActivity.this,
                R.layout.layout_spinner_item,
                mMainEvents);
        mVenueMainEventSPNR.setAdapter(mainEventsAdapter);
    }

    private void mNextBTNActions () {
        mNextBTN.setOnClickListener(this);
    }

    private void getCurrentUser() {
        mCurrentUser = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser == null) {
            navigateToNewActivity (SignInActivity.class);
        }
    }

    private void initViewsAndVariables() {
        mVenue = getIntent().getParcelableExtra("venue");
        mMainEvents = new String[]{"Main Event", "Foods Drinks", "Open Events", "Parties", "Sports"};
        mVenueMainEventSPNR = findViewById(R.id.venue_main_event_SPNR);
        mVenueSubEventsET = findViewById(R.id.venue_sub_events_ET);
        mNextBTN = findViewById(R.id.next_BTN);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_BTN :
                String venueMainEvent = mVenueMainEventSPNR.getSelectedItem().toString();
                String venueSubEvents = mVenueSubEventsET.getText().toString();
                mVenue.setVenue_main_event(venueMainEvent);
                mVenue.setVenue_sub_events(venueSubEvents);
                finish();
                navigateToNewActivityCarryingData(NewVenueThirdActivity.class, "venue", mVenue);
        }
    }

    private void navigateToNewActivity(Class classname) {
        Intent intent = new Intent(NewVenueSecondActivity.this, classname);
        startActivity(intent);
    }

    private void navigateToNewActivityCarryingData(Class classname, String key, Parcelable venue) {
        Intent intent = new Intent(NewVenueSecondActivity.this, classname);
        intent.putExtra(key, venue);
        startActivity(intent);
    }
}
