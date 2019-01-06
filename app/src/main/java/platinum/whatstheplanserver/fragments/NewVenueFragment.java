package platinum.whatstheplanserver.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.activities.NewEventFirstActivity;
import platinum.whatstheplanserver.activities.NewVenueFirstActivity;
import platinum.whatstheplanserver.activities.NewVenueThirdActivity;

import static android.app.Activity.RESULT_OK;

public class NewVenueFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "NewVenueFrgmntTag";
    private ImageView addIV;
    private LatLng mLatLng;
    private final int REQUEST_PLACE_PICKER_CODE_1 =  1;

    public NewVenueFragment() {
        // Required empty public constructor
    }

    public static NewVenueFragment newInstance() {
        NewVenueFragment fragment = new NewVenueFragment();
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
        return inflater.inflate(R.layout.fragment_new_venue, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewsAndVariables (view);
        performActions ();
    }

    private void initViewsAndVariables(View view) {
        addIV = view.findViewById(R.id.add_IV);
    }

    private void performActions() {
        addIVActions ();
    }

    private void addIVActions() {
        addIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_IV :
//                navigateToNewActivity(NewVenueFirstActivity.class);
                //todo validations
                try {
                    startActivityForResult(new PlacePicker.IntentBuilder().build(getActivity()), REQUEST_PLACE_PICKER_CODE_1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PLACE_PICKER_CODE_1 :
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(getActivity(), data);
                    mLatLng = place.getLatLng();
                    Log.d(TAG, "onActivityResult: place-name = " + place.getName());
                }
        }
    }

    private void navigateToNewActivity(Class className) {
        Intent intent = new Intent(getActivity(), className);
        startActivity(intent);
    }
}
