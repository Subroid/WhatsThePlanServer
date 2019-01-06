package platinum.whatstheplanserver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.activities.authentications.SignInActivity;

public class NewEventSecondActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mVenueNameET;
    TextView mVenueAddressTV;
    Button mNextBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_2);

        initViewsAndVariables ();
        performActions ();
    }

    private void performActions() {
        mVenueNameETActions();
        mNextBTNActions ();
    }

    private void mNextBTNActions () {
        mNextBTN.setOnClickListener(this);
    }


    private void mVenueNameETActions() {
        //todo

    }

    private void initViewsAndVariables() {
        mVenueNameET = findViewById(R.id.venue_name_ET);
        mVenueAddressTV = findViewById(R.id.venue_address_TV);
        mNextBTN = findViewById(R.id.next_BTN);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_BTN :
                navigateToNewActivity (NewEventThirdActivity.class);
        }
    }

    private void navigateToNewActivity(Class classname) {
        Intent intent = new Intent(NewEventSecondActivity.this, classname);
        startActivity(intent);
    }
}
