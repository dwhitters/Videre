package edu.gvsu.cis.videre;

        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.design.widget.CoordinatorLayout;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;
        import android.widget.EditText;

        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;

        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;


        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;

        import butterknife.BindView;
        import butterknife.ButterKnife;
        import butterknife.OnClick;

public class NewUserActivity extends AppCompatActivity {

    /**
     * Hide the soft keypad.
     */
    private void hideKeypad() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    private FirebaseAuth mAuth;

    public static final int NEW_DEVICE = 1;
    public static final int NEW_USER = 2;

    @BindView(R.id.newLog) EditText email;
    @BindView(R.id.newPass) EditText passwd;

    String emailStr;
    String passStr;

    @OnClick(R.id.createUser) void createUser() {

        emailStr = email.getText().toString();
        passStr = passwd.getText().toString();

        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()) {
                        CurrentSession.getInstance().setUser(mAuth.getCurrentUser());
                        Intent toMain = new Intent(NewUserActivity.this, DeviceActivity.class);
                        // Create new node in the database for the user.
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                        HashMap<String, String> newUserEmail = new HashMap<>();
                        newUserEmail.put("email", emailStr);
                        usersRef.child(mAuth.getCurrentUser().getUid()).setValue(newUserEmail);

                        CurrentSession.getInstance().setDatabaseRef(
                                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()));
                        startActivity(toMain);
                        finish();
                    } else {
                        Snackbar.make(email,R.string.fail_toCreate,
                                Snackbar.LENGTH_LONG)
                                .show();
                    }
                });
    }

    public void init() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        //coordinatorLayout = findViewById(R.id.coordinatorLayout);
        mAuth = FirebaseAuth.getInstance();

        init();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}