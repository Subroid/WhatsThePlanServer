package platinum.whatstheplanserver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.activities.authentications.SignInActivity;

public class NewVenueThirdActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "NewVenueThirdActvtyTag";

    private Button mVenueImageUploadBTN;
    private Button mSubmitBTN;
    private FirebaseUser mCurrentUser;
    private List<String> mVenueValues;
    private Boolean mVenueImageUploadClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_venue_3);

        getCurrentUser ();
        initViewsAndVariables ();
        performActions ();
    }

    private void performActions() {
        setButtonsListener();

    }

    private void setButtonsListener() {
        mVenueImageUploadBTN.setOnClickListener(this);
        mSubmitBTN.setOnClickListener(this);
    }

    private void getCurrentUser() {
        mCurrentUser = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser == null) {
            navigateToNewActivity (SignInActivity.class);
        }
    }

    private void initViewsAndVariables() {
        mVenueImageUploadClicked = false;
        mVenueValues = getIntent().getStringArrayListExtra("venue_values");
        Log.d(TAG, "initViewsAndVariables: mVenueValues = " + mVenueValues);
        mVenueImageUploadBTN = findViewById(R.id.venue_image__upload_BTN);
        mSubmitBTN = findViewById(R.id.submit_BTN);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.venue_image__upload_BTN :
                // todo
                break;

            case R.id.submit_BTN :

//                navigateToNewActivity (NewVenueThirdActivity.class);
                break;
        }
    }



    private void navigateToNewActivity(Class classname) {
        Intent intent = new Intent(NewVenueThirdActivity.this, classname);
        startActivity(intent);
    }
}
