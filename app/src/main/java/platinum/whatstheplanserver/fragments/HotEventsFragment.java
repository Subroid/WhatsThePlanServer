package platinum.whatstheplanserver.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.adapters.EventsAdapter;
import platinum.whatstheplanserver.models.Event;

public class HotEventsFragment extends Fragment {

    private static final String TAG = "HotEventsFragmentTag";
    private TextView mNoEventTV;
    private RecyclerView mEventsRV;
    private FirebaseFirestore mDbFirestore;


    public HotEventsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HotEventsFragment newInstance() {
        HotEventsFragment fragment = new HotEventsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hot_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: called");
        initViewsAndVariables (view);
        performActions ();
    }

    private void initViewsAndVariables(View view) {
        Log.d(TAG, "initViewsAndVariables: called");
        mNoEventTV = view.findViewById(R.id.no_event_TV);
        mEventsRV = view.findViewById(R.id.events_RV);
        mDbFirestore = FirebaseFirestore.getInstance();
    }

    private void performActions() {
        displayEvents ();

    }

    private void displayEvents() {
        Log.d(TAG, "displayEvents: called");
        CollectionReference EventsDbRef = mDbFirestore.collection("Admins")
                .document(FirebaseAuth.getInstance().getUid()).collection("Events");

        FirestoreRecyclerOptions<Event> frOptions =
                new FirestoreRecyclerOptions.Builder<Event>()
                        .setQuery(EventsDbRef, Event.class)
                        .build();

        DividerItemDecoration itemDecorator = new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        mEventsRV.addItemDecoration(itemDecorator);
        mEventsRV.setHasFixedSize(true);

        EventsAdapter adapter = new EventsAdapter(frOptions, getActivity());
        mEventsRV.setAdapter(adapter);
        adapter.startListening();
        mEventsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
