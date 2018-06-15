package edu.gvsu.cis.videre;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.google.firebase.auth.FirebaseAuth;
//
//import org.parceler.transfuse.annotations.Resource;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class SigninActivity extends AppCompatActivity {
//
//    /**
//     * Hide the soft keypad.
//     */
//
//    private FirebaseAuth mAuth;
//    EditText email = (EditText) findViewById (R.id.emailLog);
//
//    private void hideKeypad() {
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
//    }
//
//    @OnClick(R.id.NewUse) void NewUse() {
//        Intent addActivity =  new Intent(SigninActivity.this, NewUserActivity.class);
//        startActivity(addActivity);
//    }
//
//    public void init() {
//        ButterKnife.bind(this);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signin);
//
//        mAuth = FirebaseAuth.getInstance();
//
////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
////
////        Button loginBtn = (Button) findViewById(R.id.logBtn);
////        loginBtn.setOnClickListener(v -> {
////            hideKeypad(); // Hide the keypad.
////
////            Intent intent = new Intent(SigninActivity.this, DeviceActivity.class);
////            startActivity(intent);
////        });
//
//
//
//
//        init();
//
//    }
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate (savedInstanceState);
//        setContentView (R.layout.activity_signin);
//        EditText email = (EditText) findViewById(R.id.emailLog);
//        EditText password = (EditText) findViewById(R.id.passLog);
//        Button loginBtn = (Button) findViewById(R.id.logBtn);
//        String emailStr = email.getText().toString();
//        String passStr = password.getText().toString();
//
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String emailStr = email.getText().toString();
//                if (emailStr.length() == 0) {
//                    Snackbar.make(email, "Email is required", Snackbar.LENGTH_LONG)
//                            .show();
//                } }
//        }); }
//
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_signin, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
////        int id = item.getItemId();
////
////        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_new_user) {
////            return true;
////        }
////
////        return super.onOptionsItemSelected(item);
//
//
////        boolean handled = super.onOptionsItemSelected(item);
////        if(!handled) {
////            int id = item.getItemId();
////            if(id == R.id.action_new_user) {
////                Intent intent = new Intent{SigninActivity.this, NewUserActivity.class);
////                startActivityForResult(intent, NEW_USER);
////                handled = true;
////                }
////            }
////        }
////        return handled;
//        return true;
//    }
//}


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

        mAuth.signInWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()) {
                        Intent toMain = new Intent(this, DeviceActivity.class);
                        toMain.putExtra("email", emailStr);
                        //FirebaseUser trackUser = mAuth.getCurrentUser();
                        startActivity(toMain);
                        finish();
                    } else {
                        Snackbar.make(email,R.string.incorrect_password,
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

        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        init();


    }
}