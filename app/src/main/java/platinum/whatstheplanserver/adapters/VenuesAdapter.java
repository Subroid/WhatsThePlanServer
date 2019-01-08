package platinum.whatstheplanserver.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.models.Venue;

public class VenuesAdapter extends FirestoreRecyclerAdapter<Venue, VenuesAdapter.VenueHolder> {

    private Context mContext;

    public VenuesAdapter(@NonNull FirestoreRecyclerOptions<Venue> options, Context context) {
        super(options);
        mContext = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull VenueHolder holder, int position, @NonNull Venue venue) {

        holder.venue_name_TV.setText(venue.getVenue_name());
        holder.venue_type_TV.setText("Type : \n" + venue.getVenue_type());
        holder.venue_main_event_TV.setText("Main Event : \n" + venue.getVenue_main_event());
        holder.venue_sub_events_TV.setText("Sub Events : " + venue.getVenue_sub_events());
        holder.venue_address_TV.setText("Address : " + venue.getVenue_address());
        Glide.with(mContext)
                .load(Uri.parse(venue.getVenue_image()))
                .apply(new RequestOptions().fitCenter())
                .into(holder.venue_image_IV);
        Glide.with(mContext)
                .load(Uri.parse(venue.getVenue_image()))
                .apply(new RequestOptions().fitCenter())
                .into(holder.venue_layout_bg_IV);

    }

    @NonNull
    @Override
    public VenueHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_venue, viewGroup, false);
        VenueHolder viewHolder = new VenueHolder(itemView);
        return viewHolder;
    }

    class VenueHolder extends RecyclerView.ViewHolder {

        private TextView venue_name_TV;
        private TextView venue_type_TV;
        private TextView venue_main_event_TV;
        private TextView venue_sub_events_TV;
        private TextView venue_address_TV;
        private ImageView venue_image_IV;
        private ImageView venue_layout_bg_IV;

        public VenueHolder(@NonNull View itemView) {
            super(itemView);

            venue_name_TV = itemView.findViewById(R.id.venue_name_TV);
            venue_type_TV = itemView.findViewById(R.id.venue_type_TV);
            venue_main_event_TV = itemView.findViewById(R.id.venue_main_event_TV);
            venue_sub_events_TV = itemView.findViewById(R.id.venue_sub_events_TV);
            venue_address_TV = itemView.findViewById(R.id.venue_address_TV);
            venue_image_IV = itemView.findViewById(R.id.venue_image_IV);
            venue_layout_bg_IV = itemView.findViewById(R.id.venue_layout_bg_IV);

        }
    }

}
