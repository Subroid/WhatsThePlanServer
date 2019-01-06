package platinum.whatstheplanserver.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import platinum.whatstheplanserver.R;

public class HotVenuesFragment extends Fragment {


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

        initViewsAndVariables ();
        performActions ();
    }

    private void initViewsAndVariables() {

    }

    private void performActions() {

    }

}
