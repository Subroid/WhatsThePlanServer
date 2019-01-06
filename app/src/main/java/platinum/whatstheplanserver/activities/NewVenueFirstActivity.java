package platinum.whatstheplanserver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.activities.authentications.SignInActivity;

public class NewVenueFirstActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mVenueNameET;
    private Spinner mVenueTypeSPNR;
    private EditText mVenueAddressET;
    private String [] mVenueTypes;
    private Button mNextBTN;
    private FirebaseUser mCurrentUser;
    private List<String> mVenueValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_venue_1);

        getCurrentUser ();
        initViewsAndVariables ();
        performActions ();
    }

    private void performActions() {
        mVenueTypeSPNRActions ();
        mNextBTNActions ();
    }

    private void mNextBTNActions () {
        mNextBTN.setOnClickListener(this);
    }


    private void mVenueTypeSPNRActions() {

        ArrayAdapter<String> venuesTypesAdapter = new ArrayAdapter<>(NewVenueFirstActivity.this,
                                                                    R.layout.layout_spinner_item,
                mVenueTypes);
        mVenueTypeSPNR.setAdapter(venuesTypesAdapter);

    }

    private void initViewsAndVariables() {
        mVenueNameET = findViewById(R.id.venue_name_ET);
        mVenueTypes = new String[]{"Select Venue Type", "Restaurant", "Auditorium", "Club", "Stadium", "Other"};
        mVenueTypeSPNR = findViewById(R.id.venue_type_SPNR);
        mVenueAddressET = findViewById(R.id.venue_address_ET);
        mNextBTN = findViewById(R.id.next_BTN);
        mVenueValues = new ArrayList<>();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_BTN :
                String venueName = mVenueNameET.getText().toString();
                String venueType = mVenueTypeSPNR.getSelectedItem().toString();
                String venueAddress = mVenueAddressET.getText().toString();
                String[] arrVenueValues =  new String[] {venueName, venueType, venueAddress};
                List<String> listVenueValues = Arrays.asList(arrVenueValues);
                mVenueValues.addAll(listVenueValues);
                navigateToNewActivityCarryingDataList(NewVenueSecondActivity.class, "venue_values", mVenueValues);
        }
    }

    private void getCurrentUser() {
        mCurrentUser = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser == null) {
            navigateToNewActivity (SignInActivity.class);
        }
    }

    private void navigateToNewActivity(Class classname) {
        Intent intent = new Intent(NewVenueFirstActivity.this, classname);
        startActivity(intent);
    }

    private void navigateToNewActivityCarryingDataList(Class classname, String key, List<String> values) {
        Intent intent = new Intent(NewVenueFirstActivity.this, classname);
        intent.putStringArrayListExtra(key, (ArrayList<String>) values);
        startActivity(intent);
    }
}
