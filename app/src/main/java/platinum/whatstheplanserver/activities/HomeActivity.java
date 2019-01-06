package platinum.whatstheplanserver.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.activities.authentications.SignInActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private com.google.firebase.auth.FirebaseUser mCurrentUser;
    private ImageView eventsIV;
    private ImageView venuesIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

    private void performActions() {
        Glide.with(HomeActivity.this)
                .load(R.drawable.events)
                .apply(RequestOptions.circleCropTransform())
                .into(eventsIV);
        eventsIV.setOnClickListener(this);

        Glide.with(HomeActivity.this)
                .load(R.drawable.venues)
                .apply(RequestOptions.circleCropTransform())
                .into(venuesIV);
        venuesIV.setOnClickListener(this);
    }

    private void initViewsAndVariables() {
        eventsIV = findViewById(R.id.events_IV);
        venuesIV = findViewById(R.id.venues_IV);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.events_IV :
                navigateToNewActivity (EventsActivity.class);
                break;
            case R.id.venues_IV :
                navigateToNewActivity(VenuesActivity.class);
        }
    }

    private void navigateToNewActivity(Class classname) {
        Intent intent = new Intent(HomeActivity.this, classname);
        startActivity(intent);
    }
}
