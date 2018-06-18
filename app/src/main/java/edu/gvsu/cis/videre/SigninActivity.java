package edu.gvsu.cis.videre;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.widget.Toast.LENGTH_LONG;

public class SigninActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @BindView(R.id.emailLog) EditText email;
    @BindView(R.id.passLog) EditText password;

    String emailStr;
    String passStr;

    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "ˆ[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    @OnClick(R.id.NewUse) void NewUse() {
        Intent addActivity =  new Intent(SigninActivity.this, NewUserActivity.class);
        startActivity(addActivity);
    }

    @OnClick(R.id.logBtn) void logBtn() {
        emailStr = email.getText().toString();
        passStr = password.getText().toString();

        if(emailStr.length() == 0)
        {
            Snackbar.make(email, "Email field empty!", Snackbar.LENGTH_LONG)
                    .show();
        } else if (passStr.length() == 0) {
            Snackbar.make(email, "Password field empty!", Snackbar.LENGTH_LONG)
                    .show();
        } else {
            mAuth.signInWithEmailAndPassword(emailStr, passStr)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Intent toMain = new Intent(this, DeviceActivity.class);
                            // Get the new user's data and set the session data to it.
                            CurrentSession.getInstance().setUser(mAuth.getCurrentUser());
                            CurrentSession.getInstance().setDatabaseRef(FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()));
                            startActivity(toMain);
                            finish();
                        } else {
                            Snackbar.make(email, R.string.incorrect_password, Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    });
        }
    }

    public void init() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        init();
    }
}
