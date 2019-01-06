package platinum.whatstheplanserver.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import platinum.whatstheplanserver.R;

public class NewEventFirstActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner mEventTypeSPNR;
    Spinner mEventSubtypeSPNR;
    String [] mEventTypes;
    String [] mEventSubtypes;
    Button mNextBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_1);

        initViewsAndVariables ();
        performActions ();
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
        ArrayAdapter<String> eventTypesAdapter = new ArrayAdapter<>(NewEventFirstActivity.this,
                R.layout.layout_spinner_item,
                mEventSubtypes);
        mEventSubtypeSPNR.setAdapter(eventTypesAdapter);
        mEventSubtypeSPNR.setEnabled(false);

    }

    private void mEventTypeSPNRActions () {

        ArrayAdapter<String> eventTypesAdapter = new ArrayAdapter<>(NewEventFirstActivity.this,
                                                                    R.layout.layout_spinner_item,
                                                                    mEventTypes);
        mEventTypeSPNR.setAdapter(eventTypesAdapter);

    }

    private void initViewsAndVariables() {
        mEventTypes = new String[]{"Select Event Type", "Restaurant", "Open Event", "Party", "Sport"};
        mEventSubtypes = new String[] {"Select Event Subtype"};
        mEventTypeSPNR = findViewById(R.id.event_type_SPNR);
        mEventSubtypeSPNR = findViewById(R.id.event_subtype_SPNR);
        mNextBTN = findViewById(R.id.next_BTN);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_BTN :
                navigateToNewActivity (NewEventSecondActivity.class);
        }
    }

    private void navigateToNewActivity(Class classname) {
        Intent intent = new Intent(NewEventFirstActivity.this, classname);
        startActivity(intent);
    }
}
