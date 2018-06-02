package edu.gvsu.cis.videre;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import edu.gvsu.cis.videre.dummy.DeviceContent;

public class DeviceActivity extends AppCompatActivity
        implements DeviceFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Title");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected.
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DeviceContent.DeviceItem newItem = DeviceContent.createDevice(input.getText().toString());
                    if(newItem == null) { // Do nothing and alert user the name is in use.
                        Snackbar.make(findViewById(R.id.deviceCoordinatorLayout), R.string.device_name_in_use, Snackbar.LENGTH_LONG)
                                .show();
                    } else {
                        DeviceContent.ITEMS.add(newItem);
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        });
    }

    @Override
    public void onListFragmentInteraction(DeviceContent.DeviceItem item) {
        System.out.println("Interact!");
    }
}
