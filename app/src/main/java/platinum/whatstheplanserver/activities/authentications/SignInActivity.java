package platinum.whatstheplanserver.activities.authentications;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.sony.fancytoastlib.FancyToast;

import platinum.whatstheplanserver.R;
import platinum.whatstheplanserver.activities.EventsActivity;
import platinum.whatstheplanserver.activities.HomeActivity;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignInActivityTag";
    TextInputEditText emailTIET;
    TextInputEditText passwordTIET;
    Button signUpBTN;
    Button signInBTN;
    String mEmailString;
    String mPasswordString;
    private TextView mResetPwTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailTIET = findViewById(R.id.sia_emailET);
        passwordTIET = findViewById(R.id.sia_passwordET);
        signUpBTN = findViewById(R.id.sia_signupBTN);
        signInBTN = findViewById(R.id.sia_signinBTN);
        mResetPwTV = findViewById(R.id.reset_pw_TV);

        signInBTN.setOnClickListener(this);
        signUpBTN.setOnClickListener(this);
        mResetPwTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sia_signinBTN :
                signInUsingEmailPassword();
                break;
            case R.id.sia_signupBTN :
                    navigateToAnotherActivity(SignUpActivity.class);
                break;
            case R.id.reset_pw_TV :
                showPasswordResetDialog ();
        }
    }

    private void navigateToAnotherActivity(Class className) {
        Intent intent = new Intent(SignInActivity.this, className);
        startActivity(intent);
    }

    private void showPasswordResetDialog() {
        new AlertDialog.Builder(SignInActivity.this)
                .setTitle("Reset Password")
                .setMessage("Do you want to reset the Password?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        navigateToAnotherActivity(PasswordResetActivity.class);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void signInUsingEmailPassword() {
                    mEmailString = emailTIET.getText().toString();
                    mPasswordString = passwordTIET.getText().toString();
                    if (mEmailString.isEmpty() || mPasswordString.isEmpty() || mPasswordString.length() < 6) {
                        FancyToast.makeText(getApplicationContext(),
                                "Please type valid Email or Password",
                                FancyToast.LENGTH_LONG, FancyToast.ERROR,false).show();
                    } else {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmailString, mPasswordString)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // todo validation for user login restriction
                                        if (task.isSuccessful()) {
                                                Intent homeIntent = new Intent(SignInActivity.this, HomeActivity.class);
                                            startActivity(homeIntent);
                                            finish();
                                        } else {
                                            FancyToast.makeText(getApplicationContext(),
                                                    "There is some error while signing in to your account",
                                                    FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                                        }
                                    }
                                });
                        }
                }
}
