package platinum.whatstheplanserver.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.common.collect.ObjectArrays;
import com.google.firebase.auth.FirebaseUser;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.activities.authentications.SignInActivity;
import platinum.whatstheplanserver.models.Event;

public class NewEventSecondActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NewEventSecActivityTag";

    private FirebaseUser mCurrentUser;
    private Event mEvent;
    private EditText mEventName;
    private Spinner mEventTypeSPNR;
    private Spinner mEventSubtypeSPNR;
    private String [] mEventTypes;
    private String [] mEventSubtypes;
    private Button mNextBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_2);

        getCurrentUser ();
        initViewsAndVariables ();
        performActions ();
    }

    private void getCurrentUser() {
        mCurrentUser = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser == null) {
            navigateToNewActivityCarryingData(SignInActivity.class, null, null);
        }
    }

    private void performActions() {
        mEventTypeSPNRActions ();
        mEventSubtypeSPNRActions ();
        mNextBTNActions ();
    }

    private void mNextBTNActions () {
        mNextBTN.setOnClickListener(this);
    }

    private void mEventSubtypeSPNRActions () {
        ArrayAdapter<String> eventTypesAdapter =
                new ArrayAdapter<>(
                        NewEventSecondActivity.this,
                        R.layout.layout_spinner_item,
                       mEventSubtypes);
        mEventSubtypeSPNR.setAdapter(eventTypesAdapter);

    }

    private void mEventTypeSPNRActions () {

        ArrayAdapter<String> eventTypesAdapter = new ArrayAdapter<>(NewEventSecondActivity.this,
                                                                    R.layout.layout_spinner_item,
                                                                    mEventTypes);
        mEventTypeSPNR.setAdapter(eventTypesAdapter);

    }

    private void initViewsAndVariables() {
        if (getIntent() != null) {
            mEvent = getIntent().getParcelableExtra("event");
        }
        mEventName = findViewById(R.id.event_name_ET);
        mEventTypes = new String[]{"Event Type", mEvent.getEvent_type()};
        Log.d(TAG, "initViewsAndVariables: mEvent.getEvent_geopoint = " + mEvent.getEvent_geopoint());
        String[] eventSubtypesCustom = mEvent.getEvent_subtype().split(",");
        String[] eventSubtypeDefault = new String[] {"Event Subtype"};
        mEventSubtypes = ObjectArrays.concat(eventSubtypeDefault,  eventSubtypesCustom, String.class);
        mEventTypeSPNR = findViewById(R.id.event_type_SPNR);
        mEventSubtypeSPNR = findViewById(R.id.event_subtype_SPNR);
        mNextBTN = findViewById(R.id.next_BTN);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_BTN :
                mEvent.setEvent_name(mEventName.getText().toString());
                mEvent.setEvent_type(mEventTypeSPNR.getSelectedItem().toString());
                mEvent.setEvent_subtype(mEventSubtypeSPNR.getSelectedItem().toString());
                finish();
                navigateToNewActivityCarryingData (NewEventThirdActivity.class, "event", mEvent);
        }
    }

    private void navigateToNewActivityCarryingData(Class classname, String key, Object object) {
        Intent intent = new Intent(NewEventSecondActivity.this, classname);
        intent.putExtra(key, (Parcelable) object);
        startActivity(intent);
    }
}
