package platinum.whatstheplanserver.activities.authentications;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.activities.EventsActivity;
import platinum.whatstheplanserver.activities.HomeActivity;

public class NameSubmitActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "NameSubmitActivityTag";
    TextInputEditText firstnameTIET;
    TextInputEditText lastnameTIET;
    Button submitBTN;
    String mFirstNameString;
    String mLastNameString;
    String mDocumentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_submit);

        firstnameTIET = findViewById(R.id.fistnameTIET);
        lastnameTIET = findViewById(R.id.lastnameTIET);
        submitBTN = findViewById(R.id.submitBTN);
        submitBTN.setOnClickListener(this);

    }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.submitBTN :
                        mFirstNameString = firstnameTIET.getText().toString();
                        mLastNameString = lastnameTIET.getText().toString();
                        final FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    Log.d(TAG, "onClick: current_user.getUid() = " + current_user.getUid());

                    FirebaseFirestore.getInstance().collection("Users")
                            .document(current_user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            /*if (task.isSuccessful() && task.getResult() != null) {
                                UserInformation userInformationResult = task.getResult().toObject(UserInformation.class);
                                UserProfile userProfile = new UserProfile();
                                userProfile.setName(userInformationResult.getUserProfile().getName());
                                userProfile.setEmail(userInformationResult.getUserProfile().getEmail());
                                userProfile.setAdmin(userInformationResult.getUserProfile().isAdmin());
                                userProfile.setPassword(userInformationResult.getUserProfile().getPassword());
                                userProfile.setUid(userInformationResult.getUserProfile().getUid());
                                UserLocation userLocation = new UserLocation();
                                userLocation.setGeoPoint(userInformationResult.getUserLocation().getGeoPoint());
                                userLocation.setTimeStamp(userInformationResult.getUserLocation().getTimeStamp());*/

//                                UserInformation userInformation = new UserInformation(userProfile, userLocation);
                                FirebaseFirestore.getInstance().collection("Admins")
                                        .document(current_user.getUid()).update(
                                                "userProfile.name",
                                                mFirstNameString + " " + mLastNameString);
//                            }
                        }
                    });
                        Intent homeIntent = new Intent(NameSubmitActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    finish();
                    break;
            }
        }
}
