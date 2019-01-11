package platinum.whatstheplanserver.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.activities.authentications.SignInActivity;
import platinum.whatstheplanserver.models.Event;

public class NewEventThirdActivity extends AppCompatActivity implements
        View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "NewEventThirdActivityTag";
    private FirebaseUser mCurrentUser;
    private Event mEvent;
    EditText mEventDateET;
    EditText mEventTimeET;
    Button mNextBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_3);

        getCurrentUser ();
        initViewsAndVariables ();
        setClickListeners ();
    }

    private void getCurrentUser() {
        mCurrentUser = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser == null) {
            navigateToNewActivityCarryingData(SignInActivity.class, null, null);
        }
    }


    private void setClickListeners() {
        mEventDateET.setOnClickListener(this);
        mEventTimeET.setOnClickListener(this);
        mNextBTN.setOnClickListener(this);

    }


    private void initViewsAndVariables() {
        if (getIntent() != null) {
            mEvent = getIntent().getParcelableExtra("event");
        }
        mEventDateET = findViewById(R.id.event_date_ET);
        mEventTimeET = findViewById(R.id.event_time_ET);
        mNextBTN = findViewById(R.id.next_BTN);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.event_date_ET :
                showDatePickerDialog ();
                break;
            case R.id.event_time_ET :
                showTimePickerDialog ();
                break;
            case R.id.next_BTN :
                mEvent.setEvent_date(mEventDateET.getText().toString());
                mEvent.setEvent_time(mEventTimeET.getText().toString());
                finish();
                navigateToNewActivityCarryingData(NewEventFourthActivity.class, "event", mEvent);
                break;
        }
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(NewEventThirdActivity.this, this, hour, minute, false);

        timePickerDialog.show();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(NewEventThirdActivity.this, this, year, month, day);

        datePickerDialog.show();
    }


    private void navigateToNewActivityCarryingData(Class classname, String key, Object object) {
        Intent intent = new Intent(NewEventThirdActivity.this, classname);
        intent.putExtra(key, (Parcelable) object);
        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        i1 = i1+1;
        mEventDateET.setText(i2 + "/" + i1 + "/" + i);
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        if (i > 12) {
            i = i - 12;
            mEventTimeET.setText(i + ":" + i1 + "pm");
        } else {
            mEventTimeET.setText(i + ":" + i1 + "am");
        }
    }
}
