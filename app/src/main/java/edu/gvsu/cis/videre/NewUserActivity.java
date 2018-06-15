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

        import butterknife.BindView;
        import butterknife.ButterKnife;
        import butterknife.OnClick;

public class NewUserActivity extends AppCompatActivity {

    /**
     * Hide the soft keypad.
     *
     *
     */

    private FirebaseAuth mAuth;
    DatabaseReference topRef;
    trackUser mAddUser;
    ArrayList<trackUser> allUser;

    public static final int NEW_DEVICE = 1;
    public static final int NEW_USER = 2;


    @BindView(R.id.newLog) EditText email;
    @BindView(R.id.newPass) EditText passwd;

    String emailStr;
    String passStr;

    //private CoordinatorLayout coordinatorLayout;

    private void hideKeypad() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        //do we want this or nah
        allUser.clear();
        topRef = FirebaseDatabase.getInstance().getReference("trackUser");
        topRef.addChildEventListener(chEvListener);
    }

//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        System.out.println("Hello World!!!");
//    }

    private class FinalValues {

        private String emailStr = "";
        private String passStr = "";

        public void setEmailStr(String emailStr){ this.emailStr = emailStr;}
        public void setPassStr(String passStr){ this.passStr = passStr;}
        public String getEmailStr() { return emailStr;}
        public String getPassStr() { return passStr;}
    }

    private FinalValues finalVals;


    @OnClick(R.id.createUser) void createUser() {

        emailStr = email.getText().toString();
        passStr = passwd.getText().toString();

        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()) {
                        mAddUser.emailStr = emailStr;
                        mAddUser.passStr = passStr;
                        topRef.push().setValue(mAddUser);
                        Intent toMain = new Intent(NewUserActivity.this, DeviceActivity.class);
                        //do i need
                        //toMain.putExtra("email", emailStr);
                        startActivity(toMain);
                        finish();
                    } else {
//                        Snackbar.make(email,R.string.fail_toCreate,
//                                Snackbar.LENGTH_LONG)
//                                .show();
                    }
                });


//        topRef.child("trackUser").child(emailStr).child()

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
        mAddUser = new trackUser();
        allUser = new ArrayList<trackUser>();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        //hideKeypad();


//        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab);
//        fab2.setOnClickListener(v -> {
//            hideKeypad(); // Hide the keypad.
//
//            Intent intent = new Intent(NewUserActivity.this, SetupActivity.class);
//            startActivity(intent);
//        });

        init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_signin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_new_user) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);


//        boolean handled = super.onOptionsItemSelected(item);
//        if(!handled) {
//            int id = item.getItemId();
//            if(id == R.id.action_new_user) {
//                Intent intent = new Intent{SigninActivity.this, NewUserActivity.class);
//                startActivityForResult(intent, NEW_USER);
//                handled = true;
//                }
//            }
//        }
//        return handled;
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private ChildEventListener chEvListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            trackUser entry = (trackUser)
                    dataSnapshot.getValue(trackUser.class);
            entry._key = dataSnapshot.getKey();
            allUser.add(entry);
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            trackUser entry = (trackUser)
                    dataSnapshot.getValue(trackUser.class);
            ArrayList<trackUser> newHistory = new ArrayList<trackUser>();
            for(trackUser t : allUser) {
                if (!t._key.equals(dataSnapshot.getKey())) {
                    newHistory.add(t);
                }
            }
            allUser = newHistory;
        }

        @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override public void onCancelled(DatabaseError databaseError) {}
    };

    @Override
    public void onPause() {
        super.onPause();
        topRef.removeEventListener(chEvListener);
    }
}