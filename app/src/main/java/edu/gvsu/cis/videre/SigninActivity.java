package edu.gvsu.cis.videre;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

public class SigninActivity extends AppCompatActivity {

    /**
     * Hide the soft keypad.
     */
    private void hideKeypad() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button loginBtn = (Button) findViewById(R.id.logBtn);
        loginBtn.setOnClickListener(v -> {
            hideKeypad(); // Hide the keypad.

            Intent intent = new Intent(SigninActivity.this, DeviceActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signin, menu);
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
}


