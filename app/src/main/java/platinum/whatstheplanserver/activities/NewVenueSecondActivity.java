package platinum.whatstheplanserver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.activities.authentications.SignInActivity;

public class NewVenueSecondActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mVenueMainEventET;
    private EditText mVenueSubEventsET;
    private Button mNextBTN;
    private FirebaseUser mCurrentUser;
    private List<String> mVenueValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_venue_2);

        getCurrentUser ();
        initViewsAndVariables ();
        performActions ();
    }

    private void performActions() {
        mNextBTNActions ();
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
        mVenueValues = getIntent().getStringArrayListExtra("venue_values");
        mVenueMainEventET = findViewById(R.id.venue_main_event_ET);
        mVenueSubEventsET = findViewById(R.id.venue_sub_events_ET);
        mNextBTN = findViewById(R.id.next_BTN);
        Toast.makeText(this, mVenueValues.get(1).toString(),Toast.LENGTH_LONG).show();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_BTN :
                String venueMainEvent = mVenueMainEventET.getText().toString();
                String venueSubEvents = mVenueSubEventsET.getText().toString();
                String[] arrVenueValues =  new String[] {venueMainEvent, venueSubEvents};
                List<String> listVenueValues = Arrays.asList(arrVenueValues);

                mVenueValues.addAll(listVenueValues);
                navigateToNewActivityCarryingDataList(NewVenueThirdActivity.class, "venue_values", mVenueValues);
        }
    }

    private void navigateToNewActivity(Class classname) {
        Intent intent = new Intent(NewVenueSecondActivity.this, classname);
        startActivity(intent);
    }

    private void navigateToNewActivityCarryingDataList(Class classname, String key, List<String> values) {
        Intent intent = new Intent(NewVenueSecondActivity.this, classname);
        intent.putStringArrayListExtra(key, (ArrayList<String>) values);
        startActivity(intent);
    }
}
