package platinum.whatstheplanserver.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.activities.authentications.SignInActivity;
import platinum.whatstheplanserver.models.Event;
import platinum.whatstheplanserver.models.Venue;

public class NewEventFirstActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NewEventFirstActvityTag";
    
    private MaterialSearchBar mVenueNameSearchBar;
    private TextView mVenueAddressTV;
    private Button mNextBTN;
    private List<Venue> mVenuesFullList;
    private List<Venue> mVenuesQueryList;
    private List<String> mVenuesNameFullList;
    private List<Venue> mVenuesSuggestionList;
    private List<String> mVenuesNameSuggestionList;
    private FirebaseFirestore mFireDb;
    private FirebaseUser mCurrentUser;
    private CollectionReference mVenuesDbRef;
    private Venue mVenue;
    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_1);

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

    private void navigateToNewActivity(Class classname) {
        Intent intent = new Intent(NewEventFirstActivity.this, classname);
        startActivity(intent);
    }


    private void performActions() {
        mVenuesFullList = getVenuesFullList ();
        setClickListeners ();
        mVenueNameSearchBarActions ();
    }

    private void mVenueNameSearchBarActions() {

        mVenueNameSearchBar.addTextChangeListener(getTextWatcherInstance ());
        mVenueNameSearchBar.setOnSearchActionListener(getOnSearchActionListenerInstance ());
        mVenueNameSearchBar.setSuggstionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                if (mVenuesSuggestionList.size() > 0) {
                    String venueName =  mVenuesNameSuggestionList.get(position);
                    startSearchInVenuesDbRef (venueName);
                    hideSoftKeyboard(NewEventFirstActivity.this, mVenueNameSearchBar);
                    mVenueNameSearchBar.setText(venueName);
                    mVenueNameSearchBar.hideSuggestionsList();
                    mVenuesSuggestionList.clear();
                    mVenueNameSearchBar.clearFocus();
                } else {
                    String venueName =  mVenuesNameFullList.get(position);
                    startSearchInVenuesDbRef (venueName);
                    hideSoftKeyboard(NewEventFirstActivity.this, mVenueNameSearchBar);
                    mVenueNameSearchBar.setText(venueName);
                    mVenueNameSearchBar.hideSuggestionsList();
                    mVenuesFullList.clear();
                    mVenueNameSearchBar.clearFocus();
                }
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });
    }

    private List<Venue> getVenuesFullList() {

        mVenuesDbRef.addSnapshotListener(getEventListenerInstance ());

        return mVenuesFullList;

    }

    private EventListener<QuerySnapshot> getEventListenerInstance() {

        EventListener<QuerySnapshot> eventListener = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e == null) {
                    for (DocumentSnapshot documentSnapshot :
                         queryDocumentSnapshots) {

                    Venue venue = documentSnapshot.toObject(Venue.class);
                    mVenuesFullList.add(venue);
                    mVenuesNameFullList.add(venue.getVenue_name());

                    }
                }
            }
        };

        return eventListener;
    }


    private void setClickListeners() {
        mNextBTN.setOnClickListener(this);
    }


    private void initViewsAndVariables() {
        mVenuesFullList = new ArrayList<>();
        mVenuesNameFullList = new ArrayList<>();
        mVenuesSuggestionList = new ArrayList<>();
        mVenuesNameSuggestionList = new ArrayList<>();
        mVenuesQueryList = new ArrayList<>();
        mEvent = new Event(); //^precautionary instantiation
        mFireDb = FirebaseFirestore.getInstance();
        mVenuesDbRef = mFireDb.collection("Admins")
                .document(mCurrentUser.getUid())
                .collection("Venues");
        mVenueNameSearchBar = findViewById(R.id.venue_name_SearchBar);
        mVenueAddressTV = findViewById(R.id
                .venue_address_TV);
        mNextBTN = findViewById(R.id.next_BTN);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_BTN :
                finish();
                navigateToNewActivityCarryingData (NewEventSecondActivity.class, mEvent);

            }
    }

    private TextWatcher getTextWatcherInstance() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String searchText = charSequence.toString();
                if (searchText.length() > 0) {
                    searchSuggestions (searchText);
                    mVenueNameSearchBar.setLastSuggestions(mVenuesNameSuggestionList);
                    mVenueNameSearchBar.showSuggestionsList();
                } else {
                    mVenueNameSearchBar.hideSuggestionsList();
                }

            }

            private void searchSuggestions(String searchText) {
                mVenuesSuggestionList.clear();
                mVenueNameSearchBar.clearSuggestions();
                for (Venue venue : mVenuesFullList) {
                    if (venue.getVenue_name().toLowerCase().contains(searchText.toLowerCase())) {
                        mVenuesSuggestionList.add(venue);
                        mVenuesNameSuggestionList.add(venue.getVenue_name());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    hideSoftKeyboard(NewEventFirstActivity.this, mVenueNameSearchBar);

                }
            }
        };

        return textWatcher;
    }

    private MaterialSearchBar.OnSearchActionListener getOnSearchActionListenerInstance() {
        MaterialSearchBar.OnSearchActionListener onSearchActionListener =
                new MaterialSearchBar.OnSearchActionListener() {
                    @Override
                    public void onSearchStateChanged(boolean enabled) {
                        Log.d(TAG, "onSearchStateChanged: enabled = " + enabled);
                        if (enabled) {
                            mVenueNameSearchBar.setLastSuggestions(mVenuesNameFullList);
                            mVenueNameSearchBar.showSuggestionsList();
                        } else {
                            mVenueNameSearchBar.hideSuggestionsList();
                        }
                    }

                    @Override
                    public void onSearchConfirmed(CharSequence text) {
                        hideSoftKeyboard(NewEventFirstActivity.this, mVenueNameSearchBar);
                        startSearchInVenuesDbRef (text.toString());

                    }

                    @Override
                    public void onButtonClicked(int buttonCode) {
                        Log.d(TAG, "onButtonClicked: buttonCode = " + buttonCode);

                    }

                };
        return onSearchActionListener;
    }

    private void startSearchInVenuesDbRef(String text) {

        Log.d(TAG, "startSearchInVenuesDbRef: mVenuesDbRef = " + mVenuesDbRef.getPath());
        Query query = mVenuesDbRef.whereEqualTo("venue_name", text);
        query.addSnapshotListener(getEventListenerInstanceForQuery ());

    }

    private EventListener<QuerySnapshot> getEventListenerInstanceForQuery() {
        EventListener<QuerySnapshot> eventListener = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e == null) {
                    for (DocumentSnapshot documentSnapshot :
                            queryDocumentSnapshots) {
                        Venue venue = documentSnapshot.toObject(Venue.class);
                        Log.d(TAG, "onEvent: venue name " + venue.getVenue_name());
                        mVenuesQueryList.add(venue);
                    }
                    if (mVenuesQueryList.size() == 1) {
                        mVenue = mVenuesQueryList.get(0);
                        Log.d(TAG, "onEvent: mVenue.getVenue_geopoint = " + mVenue.getVenue_geopoint());
                        mEvent = new Event(null, mVenue.getVenue_main_event(), mVenue.getVenue_sub_events(), mVenue.getVenue_name(),
                                mVenue.getVenue_id(), mVenue.getVenue_address(), mVenue.getVenue_image(),
                                null, null, null, null, FirebaseAuth.getInstance().getUid(), mVenue.getVenue_geopoint());
                        mVenueAddressTV.setText(mVenue.getVenue_address());
                    } else {
                        new AlertDialog.Builder(NewEventFirstActivity.this)
                                .setMessage("There are more than 1 result so Please type the " +
                                        "Venue Name and Address manually")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //todo treat search bar like edit text
                                    }
                                });
                    }
                } else {
                    FancyToast.makeText(NewEventFirstActivity.this, e.toString(), FancyToast.LENGTH_LONG)
                            .show();
                }
            }
        };
        return eventListener;
    }

    private void navigateToNewActivityCarryingData(Class classname, Event event) {
        Intent intent = new Intent(NewEventFirstActivity.this, classname);
        intent.putExtra("event", event);
        startActivity(intent);
    }

    public void hideSoftKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
}
