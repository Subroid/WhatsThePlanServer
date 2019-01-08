package platinum.whatstheplanserver.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.activities.authentications.SignInActivity;
import platinum.whatstheplanserver.models.Venue;

public class NewVenueThirdActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "NewVenueThirdActvtyTag";

    private Venue mVenue;
    private ProgressBar mProgressBar;
    private Button mVenueImageUploadBTN;
    private ImageView mVenueImageIV;
    private Button mSubmitBTN;
    private FirebaseUser mCurrentUser;
    private Boolean mVenueImageUploadClicked;
    private FirebaseFirestore mFireDb;
    private CollectionReference mVenuesDbRef;
    private final int REQUEST_SELECT_PICTURE_CODE_1 =  1;

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
        mVenue = getIntent().getParcelableExtra("venue");
        mProgressBar = findViewById(R.id.progressBar);
        mFireDb = FirebaseFirestore.getInstance();
        mVenuesDbRef = mFireDb.collection("Admins")
                .document(mCurrentUser.getUid())
                .collection("Venues");
        mVenueImageUploadClicked = false;
        mVenueImageIV = findViewById(R.id.venue_image_IV);
        mVenueImageUploadBTN = findViewById(R.id.venue_image__upload_BTN);
        mSubmitBTN = findViewById(R.id.submit_BTN);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.venue_image__upload_BTN :
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_SELECT_PICTURE_CODE_1);
                break;

            case R.id.submit_BTN :
                mProgressBar.setVisibility(View.VISIBLE);
                Bitmap venueBitmap = ((BitmapDrawable) mVenueImageIV.getDrawable()).getBitmap();
                ByteArrayOutputStream venueBaos = new ByteArrayOutputStream();
                venueBitmap.compress(Bitmap.CompressFormat.WEBP, 50, venueBaos);
                byte[] venueByteArr = venueBaos.toByteArray();

                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                final StorageReference imageRef = firebaseStorage.getReference()
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("venues")
                        .child(mVenue.getVenue_type() + "/" + mVenue.getVenue_name()+".webp");

                UploadTask uploadTask = imageRef.putBytes(venueByteArr);

                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Log.d(TAG, "onComplete: task " + task.isSuccessful());
                        if (task.isSuccessful())  {
                            Log.d(TAG, "onComplete: task " + task.isSuccessful());
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    mVenue.setVenue_image(uri.toString());

                                    mVenuesDbRef.add(mVenue)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            String venueId = documentReference.getId();
                                            mVenuesDbRef.document(venueId)
                                                    .update("venue_id", venueId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    mProgressBar.setVisibility(View.GONE);
                                                    navigateToNewActivity(VenuesActivity.class);
                                                }
                                            });
                                        }
                                    });
                                }
                            });

                        }
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: requestCode = " + requestCode);
        Log.d(TAG, "onActivityResult: resultcode = " + resultCode);
        switch (requestCode) {
            case REQUEST_SELECT_PICTURE_CODE_1 :
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Log.d(TAG, "onActivityResult: selectedImageUri = " + selectedImageUri);
                        mVenueImageIV.setVisibility(View.VISIBLE);
                        Glide.with(NewVenueThirdActivity.this)
                                .load(selectedImageUri)
                                .into(mVenueImageIV);

                    }

                }
        }
    }

    private void navigateToNewActivity(Class classname) {
        Intent intent = new Intent(NewVenueThirdActivity.this, classname);
        intent.putExtra("mDifferentViewPagerCurrentTab", true);
        startActivity(intent);
    }
}
