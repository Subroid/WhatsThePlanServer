package platinum.whatstheplanserver.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.activities.NewEventFirstActivity;

public class NewEventFragment extends Fragment implements View.OnClickListener {

    ImageView addIV;

    public NewEventFragment() {
        // Required empty public constructor
    }

    public static NewEventFragment newInstance() {
        NewEventFragment fragment = new NewEventFragment();
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
        return inflater.inflate(R.layout.fragment_new_event, container, false);
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
                navigateToNewActivity(NewEventFirstActivity.class);
        }
    }

    private void navigateToNewActivity(Class className) {
        Intent intent = new Intent(getActivity(), className);
        startActivity(intent);
    }
}
