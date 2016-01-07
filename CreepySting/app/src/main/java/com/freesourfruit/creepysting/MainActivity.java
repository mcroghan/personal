package com.freesourfruit.creepysting;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    // LogCat
    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;
    private GoogleApiClient mGApiClient;
    private BroadcastReceiver receiver;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //get the textview
        textView = (TextView) findViewById(R.id.msg);

        //Set the context
        mContext = this;

        //Check Google Play Service Available
        if(isPlayServiceAvailable()) {
            mGApiClient = new GoogleApiClient.Builder(this)
                    .addApi(ActivityRecognition.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            //Connect to Google API
            mGApiClient.connect();
        }else{
            Toast.makeText(mContext, "Google Play Service not Available", Toast.LENGTH_LONG).show();
        }

        //Broadcast receiver
        receiver  = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Add current time
                Calendar rightNow = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss a");
                String strDate = sdf.format(rightNow.getTime());
                String v =  strDate + " " +
                        intent.getStringExtra("activity") + " " +
                        "Confidence : " + intent.getExtras().getInt("confidence") + "\n";

                v = textView.getText() + v;
                textView.setText(v);
            }
        };

        //Filter the Intent and register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("ImActive");
        registerReceiver(receiver, filter);
    }

    //Check for Google play services available on device
    private boolean isPlayServiceAvailable() {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext) == ConnectionResult.SUCCESS;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Intent i = new Intent(this, ActivityRecognitionIntentService.class);
        PendingIntent mActivityRecongPendingIntent = PendingIntent
                .getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.d(TAG, "connected to ActivityRecognition");
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGApiClient, 0, mActivityRecongPendingIntent);

        //Update the TextView
        textView.setText("Connected to Google Play Services \nWaiting for Active Recognition... \n");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Suspended to ActivityRecognition");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Not connected to ActivityRecognition");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Disconnect and detach the receiver
        mGApiClient.disconnect();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
