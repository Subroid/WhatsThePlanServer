package platinum.whatstheplanserver.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.adapters.VenuesAdapter;
import platinum.whatstheplanserver.models.Venue;

public class HotVenuesFragment extends Fragment {

    private TextView mNoVenueTV;
    private RecyclerView mVenuesRV;
    private FirebaseFirestore mDbFirestore;


    public HotVenuesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HotVenuesFragment newInstance() {
        HotVenuesFragment fragment = new HotVenuesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hot_venues, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewsAndVariables (view);
        performActions ();
    }

    private void initViewsAndVariables(View view) {
        mNoVenueTV = view.findViewById(R.id.no_venue_TV);
        mVenuesRV = view.findViewById(R.id.venues_RV);
        mDbFirestore = FirebaseFirestore.getInstance();
    }

    private void performActions() {
        displayVenues ();

    }

    private void displayVenues() {
        CollectionReference venuesDbRef = mDbFirestore.collection("Admins")
                .document(FirebaseAuth.getInstance().getUid()).collection("Venues");

        FirestoreRecyclerOptions<Venue> frOptions =
                new FirestoreRecyclerOptions.Builder<Venue>()
                        .setQuery(venuesDbRef, Venue.class)
                        .build();

        VenuesAdapter adapter = new VenuesAdapter(frOptions, getActivity());
        mVenuesRV.setHasFixedSize(true);
        mVenuesRV.setAdapter(adapter);
        adapter.startListening();
        mVenuesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}
