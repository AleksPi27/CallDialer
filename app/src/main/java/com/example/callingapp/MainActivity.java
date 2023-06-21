package com.example.callingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    EditText edittext;
    Button button;
    ToggleButton toggleButton;
    TextView textSubHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting instance of edittext and button
        button = findViewById(R.id.button);
        edittext = findViewById(R.id.editText);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        textSubHeader = (TextView) findViewById(R.id.textSubHeader);
        // Attach set on click listener to the button for initiating intent
         toggleButton.setOnClickListener(arg -> {
                    try {
                        boolean checked = ((ToggleButton) findViewById(R.id.toggleButton)).isChecked();
                        if (checked) {
                            Intent intent = new Intent(this, RecordingService.class);
                            startService(intent);
                            Toast.makeText(getApplicationContext(), "Call Recording is set ON", Toast.LENGTH_SHORT).show();
                            textSubHeader.setText("Switch on Toggle to record your calls");
                        } else {
                            Intent intent = new Intent(this, RecordingService.class);
                            stopService(intent);
                            Toast.makeText(getApplicationContext(), "Call Recording is set OFF", Toast.LENGTH_SHORT).show();
                            textSubHeader.setText("Switch Off Toggle to stop recording your calls");
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        button.setOnClickListener(arg -> {
            // getting phone number from edit text and changing it to String
            String phone_number = edittext.getText().toString();

            // Getting instance of Intent with action as ACTION_CALL
            Intent phone_intent = new Intent(Intent.ACTION_CALL);

            // Set data of Intent through Uri by parsing phone number
            phone_intent.setData(Uri.parse("tel:" + phone_number));

            // start Intent
            startActivity(phone_intent);
        });

    }
    private final String LOG_TAG = "CallStateReceiver";

    public static String prevState = TelephonyManager.EXTRA_STATE_IDLE;


    @Override
    protected void onResume() {
        super.onResume();

        // Runtime permission
        try {

            boolean permissionGranted_OutgoingCalls = ActivityCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED;
            boolean permissionGranted_phoneState = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
            boolean permissionGranted_recordAudio = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
            boolean permissionGranted_WriteExternal = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            boolean permissionGranted_ReadExternal = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


            if (permissionGranted_OutgoingCalls) {
                if (permissionGranted_phoneState) {
                    if (permissionGranted_recordAudio) {
                        if (permissionGranted_WriteExternal) {
                            if (permissionGranted_ReadExternal) {
                                try {
                                    toggleButton.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
                            }
                        } else {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
                        }
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 400);
                    }
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 500);
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 600);
            }

        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

    }


    //                <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"></uses-permission>
//    <uses-permission android:name="android.permission.READ_PHONE_STATE"></uses-permission>
//    <uses-permission android:name="android.permission.RECORD_AUDIO"></uses-permission>
//    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
//    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"></uses-permission>

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200 || requestCode == 300 || requestCode == 400 || requestCode == 500 || requestCode == 600) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    toggleButton.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}