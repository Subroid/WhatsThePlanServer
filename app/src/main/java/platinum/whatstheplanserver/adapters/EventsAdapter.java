package platinum.whatstheplanserver.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.activities.BookingsActivity;
import platinum.whatstheplanserver.models.Event;
import platinum.whatstheplanserver.models.Guest;
import platinum.whatstheplanserver.models.Venue;

public class EventsAdapter extends FirestoreRecyclerAdapter<Event, EventsAdapter.EventsHolder> {

    private static final String TAG = "EventsAdapterTag";

    private Context mContext;

    public EventsAdapter(@NonNull FirestoreRecyclerOptions<Event> options, Context context) {
        super(options);
        mContext = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull EventsHolder holder, int position, @NonNull Event event) {

            holder.event_name_TV.setText(event.getEvent_name());
            Log.d(TAG, "onBindViewHolder: eventname = " +  event.getEvent_name());
            try {
                holder.event_tickets.setText(String.valueOf(event.getevent_tickets()));
            } catch (NullPointerException e) {
                Log.d(TAG, "onBindViewHolder: e = " + e.getMessage());
            }
            holder.event_date_TV.setText(event.getEvent_date());
            holder.event_time_TV.setText(event.getEvent_time());
            holder.venue_name_TV.setText(event.getVenue_name());
            holder.venue_address_TV.setText("Address : " + event.getVenue_address());
            holder.event_bookings_BTN.setTag(R.id.TAG_FOR_EVENT, event);
            holder.delete_event_BTN.setTag(R.id.TAG_FOR_EVENT, event);

            Glide.with(mContext)
                    .load(Uri.parse(event.getEvent_image()))
                    .apply(new RequestOptions().fitCenter())
                    .into(holder.event_image_IV);
            Glide.with(mContext)
                    .load(Uri.parse(event.getEvent_image()))
                    .apply(new RequestOptions().fitCenter())
                    .into(holder.event_layout_bg_IV);


    }

    @NonNull
    @Override
    public EventsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_event, viewGroup, false);
        EventsHolder viewHolder = new EventsHolder(itemView);
        return viewHolder;
    }

    class EventsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView event_name_TV;
        private TextView event_tickets;
        private TextView event_date_TV;
        private TextView event_time_TV;
        private TextView venue_name_TV;
        private TextView venue_address_TV;
        private ImageView event_image_IV;
        private ImageView event_layout_bg_IV;
        private Button event_bookings_BTN;
        private Button delete_event_BTN;
        private ProgressBar progressBar;

        public EventsHolder(@NonNull View itemView) {
            super(itemView);
            event_name_TV = itemView.findViewById(R.id.event_name_TV);
            event_tickets = itemView.findViewById(R.id.event_tickets_ET);
            event_date_TV = itemView.findViewById(R.id.event_date_TV);
            event_time_TV = itemView.findViewById(R.id.event_time_TV);
            venue_name_TV = itemView.findViewById(R.id.venue_name_TV);
            venue_address_TV = itemView.findViewById(R.id.venue_address_TV);
            event_image_IV   = itemView.findViewById(R.id.event_image_IV);
            event_layout_bg_IV = itemView.findViewById(R.id.event_layout_bg_IV);
            event_bookings_BTN = itemView.findViewById(R.id.event_bookings_BTN);
            delete_event_BTN = itemView.findViewById(R.id.delete_event_BTN);
            progressBar = itemView.findViewById(R.id.progressBar);

            event_bookings_BTN.setOnClickListener(this);
            delete_event_BTN.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Event event;
            switch (view.getId()) {
                case R.id.delete_event_BTN :
                    progressBar.setVisibility(View.VISIBLE);
                    event = (Event) delete_event_BTN.getTag(R.id.TAG_FOR_EVENT);
                    deleteEvent(event);
                    break;
                case R.id.event_bookings_BTN :
                    progressBar.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onClick: bookings button clicked");
                    event = (Event) event_bookings_BTN.getTag(R.id.TAG_FOR_EVENT);
                    navigateToNewActivityCarryingData (BookingsActivity.class, event);
            }

        }

        private void deleteEvent(final Event event) {
           final FirebaseFirestore dbFirestore =  FirebaseFirestore.getInstance();
           String adminId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Log.d(TAG, "deleteEvent: adminid " + adminId);
           final DocumentReference dbAdminRef = dbFirestore
                   .collection("Admins")
                   .document(adminId);
            Log.d(TAG, "deleteEvent: event name " + event.getEvent_name());
           DocumentReference dbEventRef = dbAdminRef
                   .collection("Events")
                   .document(event.getEvent_id());
           dbEventRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {
                  DocumentReference dbEventRef = dbAdminRef
                          .collection("Venues")
                          .document(event.getVenue_id())
                          .collection("Events")
                          .document(event.getEvent_id());
                  dbEventRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {
                          DocumentReference dbEventRef = dbAdminRef
                                  .collection("Bookings")
                                  .document(event.getEvent_id());
                          CollectionReference dbGuestsRef = dbEventRef
                                  .collection("Guests");
                          final List<Guest> guestList = new ArrayList<>();
                          dbGuestsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                              @Override
                              public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                  for (DocumentSnapshot documentSnapshot :
                                          queryDocumentSnapshots) {
                                     Guest guest = documentSnapshot.toObject(Guest.class);
                                     guestList.add(guest);
                                  }
                                  for (int i = 0; i < guestList.size(); i++) {
                                      Guest guest = guestList.get(i);
                                      DocumentReference dbUserRef = dbFirestore
                                              .collection("Users")
                                              .document(guest.getGuest_id());
                                      DocumentReference dbEventRef = dbUserRef
                                              .collection("Bookings")
                                              .document(event.getEvent_id());
                                      final int finalI = i;
                                      dbEventRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                          @Override
                                          public void onSuccess(Void aVoid) {
                                              Log.d(TAG, "onSuccess: deleted");
                                              if (finalI == guestList.size()) {
                                                  progressBar.setVisibility(View.INVISIBLE);
                                                  FancyToast.makeText(mContext, "Event deleted successully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                              }
                                          }
                                      });
                                  }
                              }
                          });

                      }
                  });
               }
           });
        }

        private void navigateToNewActivityCarryingData(Class classname, Event event) {
            Intent intent = new Intent(mContext, classname);
            intent.putExtra("event", event);
            progressBar.setVisibility(View.INVISIBLE);
            mContext.startActivity(intent);
        }
    }

}
