package platinum.whatstheplanserver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LandActivity extends AppCompatActivity {

    // ^this activity would be used as splash screen whenever the EventsActivity would take time to start

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        jumpToHomeActivity ();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
            jumpToHomeActivity ();
    }

        private void jumpToHomeActivity() {
                Intent homeIntent = new Intent(LandActivity.this, HomeActivity.class);
            startActivity(homeIntent);
            finish();

        }
}
