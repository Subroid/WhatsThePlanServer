package platinum.whatstheplanserver.activities;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.adapters.BookingsAdapter;
import platinum.whatstheplanserver.models.Booking;
import platinum.whatstheplanserver.models.Event;

public class BookingsActivity extends AppCompatActivity {

    private static final String TAG = "BookingsActivityTag";
    private Event mEvent;
    private ProgressBar mProgressBar;
    private RecyclerView mBookingsRV;
    private TextView mToolbarTitleTV;
    private TextView mNoBookingTV;
    private FirebaseFirestore mDbFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        
        initViewsAndVariables ();
        performActions ();
        
    }

    private void performActions() {
        mToolbarTitleTV.setText(mEvent.getEvent_name() + " " + "bookings");
        mBookingsRV.addItemDecoration(getItemDecorator ());
        mBookingsRV.setHasFixedSize(true);
        mBookingsRV.setHasFixedSize(true);
        BookingsAdapter bookingsAdapter = new BookingsAdapter(getFirestoreRecyclerOptions (), BookingsActivity.this, mNoBookingTV);
        mBookingsRV.setAdapter(bookingsAdapter);
        bookingsAdapter.startListening();
        mBookingsRV.setLayoutManager(new LinearLayoutManager(BookingsActivity.this));
        mProgressBar.setVisibility(View.INVISIBLE);

    }

    private RecyclerView.ItemDecoration getItemDecorator() {
        DividerItemDecoration itemDecorator = new DividerItemDecoration
                (BookingsActivity.this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(BookingsActivity.this, R.drawable.divider));
        return itemDecorator;
    }

    private FirestoreRecyclerOptions<Booking> getFirestoreRecyclerOptions() {
        FirestoreRecyclerOptions<Booking> fsOptions = new FirestoreRecyclerOptions.Builder<Booking>()
                .setQuery(getFirestoreQueryReference(),Booking.class)
                .build();
        return fsOptions;
    }

    private Query getFirestoreQueryReference() {
        Log.d(TAG, "getFirestoreQueryReference: uid = " + FirebaseAuth.getInstance().getCurrentUser().getUid());
        Query queryGuests = mDbFirestore.collection("Admins")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Bookings")
                .document(mEvent.getEvent_id())
                .collection("Guests");
        return queryGuests;

    }

    private void initViewsAndVariables() {
        mEvent = getIntent().getParcelableExtra("event");
        mNoBookingTV = findViewById(R.id.no_booking_TV);
        mDbFirestore = FirebaseFirestore.getInstance();
        mProgressBar = findViewById(R.id.progressBar);
        mBookingsRV = findViewById(R.id.bookings_RV);
        mToolbarTitleTV = findViewById(R.id.toolbar_title_TV);
    }
}
