package platinum.whatstheplanserver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import platinum.whatstheplanserver.R;

public class NewEventThirdActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mEventDateET;
    EditText mEventTimeET;
    Button mNextBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_3);

        initViewsAndVariables ();
        performActions ();
    }

    private void performActions() {
        mEventDateETActions();
        mEventTimeETActions ();

    }

    private void mEventTimeETActions() {
        mNextBTN.setOnClickListener(this);
    }


    private void mEventDateETActions() {
        //todo

    }

    private void initViewsAndVariables() {
        mEventDateET = findViewById(R.id.event_date_ET);
        mEventTimeET = findViewById(R.id.event_time_ET);
        mNextBTN = findViewById(R.id.next_BTN);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_BTN :
                navigateToNewActivity (NewEventThirdActivity.class);
        }
    }

    private void navigateToNewActivity(Class classname) {
        Intent intent = new Intent(NewEventThirdActivity.this, classname);
        startActivity(intent);
    }
}
