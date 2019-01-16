package platinum.whatstheplanserver.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.models.Booking;

public class BookingsAdapter extends FirestoreRecyclerAdapter<Booking, BookingsAdapter.BookingHolder> {

    private static final String TAG = "BookingsAdapterTag";
    private Context mContext;
    private TextView mNoBookingTV;

    public BookingsAdapter(@NonNull FirestoreRecyclerOptions<Booking> options, Context context, TextView noBookingTV) {
        super(options);
        mContext = context;
        mNoBookingTV = noBookingTV;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookingHolder holder, int position, @NonNull Booking booking) {
        Log.d(TAG, "onBindViewHolder: booking.getGuest_name = " + booking.getGuest_name());
        holder.guest_name_TV.setText("Guest Name : " + booking.getGuest_name());
        holder.guest_email_TV.setText("Guest Email : " + booking.getGuest_email());

        mNoBookingTV.setVisibility(View.INVISIBLE);

    }

    @NonNull
    @Override
    public BookingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_booking, viewGroup, false);
        BookingHolder bookingHolder = new BookingHolder(view);
        return bookingHolder;
    }

    class BookingHolder extends RecyclerView.ViewHolder {

        private TextView guest_name_TV;
        private TextView guest_email_TV;


        public BookingHolder(@NonNull View itemView) {
            super(itemView);
            guest_name_TV = itemView.findViewById(R.id.guest_name);
            guest_email_TV = itemView.findViewById(R.id.guest_email);
        }
    }

}
