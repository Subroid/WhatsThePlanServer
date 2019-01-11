package platinum.whatstheplanserver.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import platinum.whatstheplanserver.models.Event;
import platinum.whatstheplanserver.models.Venue;

public class NewEventFourthActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NewEvent4thActvtyTag";

    private Event mEvent;
    private ProgressBar mProgressBar;
    private Button mEventImageUploadBTN;
    private ImageView mEventImageIV;
    private Button mSubmitBTN;
    private FirebaseUser mCurrentUser;
    private Boolean mVenueImageUploadClicked;
    private FirebaseFirestore mFireDb;
    private CollectionReference mEventsDbRef;
    private DocumentReference mEventDbRef;
    private final int REQUEST_SELECT_PICTURE_CODE_1 =  1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_4);

        getCurrentUser ();
        initViewsAndVariables ();
        performActions ();
    }

    private void performActions() {
        setButtonsListener();

    }

    private void setButtonsListener() {
        mEventImageUploadBTN.setOnClickListener(this);
        mSubmitBTN.setOnClickListener(this);
    }

    private void getCurrentUser() {
        mCurrentUser = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser == null) {
            navigateToNewActivity (SignInActivity.class);
        }
    }

    private void initViewsAndVariables() {
        mEvent = getIntent().getParcelableExtra("event");
        mProgressBar = findViewById(R.id.progressBar);
        mFireDb = FirebaseFirestore.getInstance();
        mEventsDbRef = mFireDb.collection("Admins")
                .document(mCurrentUser.getUid())
                .collection("Venues")
                .document(mEvent.getVenue_id())
                .collection("Events");
        mVenueImageUploadClicked = false;
        mEventImageIV = findViewById(R.id.event_image_IV);
        mEventImageUploadBTN = findViewById(R.id.event_image__upload_BTN);
        mSubmitBTN = findViewById(R.id.submit_BTN);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.event_image__upload_BTN :
                Log.d(TAG, "onClick: called = " + "uploadbtn");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_SELECT_PICTURE_CODE_1);
                break;

            case R.id.submit_BTN :
                mProgressBar.setVisibility(View.VISIBLE);
                mSubmitBTN.setText("");
                Bitmap eventBitmap = ((BitmapDrawable) mEventImageIV.getDrawable()).getBitmap();
                ByteArrayOutputStream venueBaos = new ByteArrayOutputStream();
                eventBitmap.compress(Bitmap.CompressFormat.WEBP, 50, venueBaos);
                byte[] venueByteArr = venueBaos.toByteArray();

                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                final StorageReference imageRef = firebaseStorage.getReference()
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("events")
                        .child(mEvent.getEvent_type() + "/" + mEvent.getEvent_name()+".webp");

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
                                    mEvent.setEvent_image(uri.toString());

                                    mEventsDbRef.add(mEvent)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    final String eventId = documentReference.getId();
                                                    mEventsDbRef.document(eventId)
                                                            .update("event_id", eventId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            mEvent.setEvent_id(eventId);

                                                            mEventDbRef = mFireDb.collection("Admins")
                                                                    .document(mCurrentUser.getUid())
                                                                    .collection("Events")
                                                                    .document(mEvent.getEvent_id());
                                                            mEventDbRef.set(mEvent).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                 @Override
                                                                 public void onSuccess(Void aVoid) {
                                                                     mEventDbRef = mFireDb.collection("Events").document(mEvent.getEvent_id());
                                                                     mEventDbRef.set(mEvent).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                         @Override
                                                                         public void onSuccess(Void aVoid) {
                                                                             mEventDbRef = mFireDb.collection(mEvent.getEvent_type()).document(mEvent.getEvent_id());
                                                                             mEventDbRef.set(mEvent).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                 @Override
                                                                                 public void onSuccess(Void aVoid) {
                                                                                     mProgressBar.setVisibility(View.GONE);
                                                                                     navigateToNewActivity(EventsActivity.class);
                                                                                 }
                                                                             });
                                                                         }
                                                                     });
                                                                 }
                                                             });

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
        Log.d(TAG, "onActivityResult: resultCode = " + resultCode);
        switch (requestCode) {
            case REQUEST_SELECT_PICTURE_CODE_1 :
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        mEventImageIV.setVisibility(View.VISIBLE);
                        Glide.with(NewEventFourthActivity.this)
                                .load(selectedImageUri)
                                .into(mEventImageIV);

                    }

                }
        }
    }

    private void navigateToNewActivity(Class classname) {
        Intent intent = new Intent(NewEventFourthActivity.this, classname);
        intent.putExtra("mDifferentViewPagerCurrentTab", true);
        finish();
        startActivity(intent);
    }
}
