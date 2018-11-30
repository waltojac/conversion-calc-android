package waltojac.conversioncalc;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import waltojac.conversioncalc.dummy.HistoryContent;
import waltojac.conversioncalc.webservice.WeatherService;

import static waltojac.conversioncalc.webservice.WeatherService.BROADCAST_WEATHER;


public class MainActivity extends AppCompatActivity {
    public static final int UNIT_SELECTION = 1;
    public static final int HISTORY_RESULT = 1;
    private static final String TAG = "Main Activity";
    DatabaseReference topRef;
    public static List<HistoryContent.HistoryItem> allHistory = new ArrayList<HistoryContent.HistoryItem>();
    ImageView weatherIcon = null;
    TextView current = null;
    TextView temperature = null;
    private double longitude;
    private double latitude;

    LocationManager mLocationManager;
    Location location;


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    private BroadcastReceiver weatherReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: " + intent);
            Bundle bundle = intent.getExtras();
            double temp = bundle.getDouble("TEMPERATURE");
            String summary = bundle.getString("SUMMARY");
            String icon = bundle.getString("ICON").replaceAll("-", "_");
            String key = bundle.getString("KEY");
            Log.d(TAG, "onReceive: " + key + " " + summary);
            int resID = getResources().getIdentifier(icon, "drawable", getPackageName());
            //setWeatherViews(View.VISIBLE);
            if (key.equals("p1")) {
                current.setText(summary);
                temperature.setText(Double.toString(temp));
                weatherIcon.setImageResource(resID);
            }
        }
    };


    boolean distanceMode = true;

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onResume() {
        super.onResume();
        allHistory.clear();
        topRef = FirebaseDatabase.getInstance().getReference("history");
        topRef.addChildEventListener(chEvListener);

        IntentFilter weatherFilter = new IntentFilter(BROADCAST_WEATHER);
        LocalBroadcastManager.getInstance(this).registerReceiver(weatherReceiver, weatherFilter);

    }

    @Override
    public void onPause() {
        super.onPause();
        topRef.removeEventListener(chEvListener);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(weatherReceiver);
    }


    private ChildEventListener chEvListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HistoryContent.HistoryItem entry =
                    (HistoryContent.HistoryItem) dataSnapshot.getValue(HistoryContent.HistoryItem.class);
            entry._key = dataSnapshot.getKey();
            allHistory.add(entry);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            HistoryContent.HistoryItem entry =
                    (HistoryContent.HistoryItem) dataSnapshot.getValue(HistoryContent.HistoryItem.class);
            List<HistoryContent.HistoryItem> newHistory = new ArrayList<HistoryContent.HistoryItem>();
            for (HistoryContent.HistoryItem t : allHistory) {
                if (!t._key.equals(dataSnapshot.getKey())) {
                    newHistory.add(t);
                }
            }
            allHistory = newHistory;
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == UNIT_SELECTION) {
            final TextView fromUnitLabel = (TextView) findViewById(R.id.fromUnitLabel);
            final TextView toUnitLabel = (TextView) findViewById(R.id.toUnitLabel);

            fromUnitLabel.setText(data.getStringExtra("fromSelect"));
            toUnitLabel.setText(data.getStringExtra("toSelect"));
        } else if (resultCode == HISTORY_RESULT) {
            String[] vals = data.getStringArrayExtra("item");

            final TextView fromField = findViewById(R.id.fromTextField);
            final TextView fromUnits = (TextView) findViewById(R.id.fromUnitLabel);
            final TextView toField = findViewById(R.id.toTextField);
            final TextView toUnits = (TextView) findViewById(R.id.toUnitLabel);
            final TextView mode = findViewById(R.id.convType);
            final TextView title = (TextView) findViewById(R.id.showTitle);


            fromField.setText(vals[0]);
            toField.setText(vals[1]);
            mode.setText(vals[2]);
            fromUnits.setText(vals[3]);
            toUnits.setText(vals[4]);
            title.setText(vals[2] + " Converter");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        allHistory = new ArrayList<HistoryContent.HistoryItem>();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_item:
                Intent settingsIntent = new Intent(MainActivity.this, ModeSelectionActivity.class);
                final TextView fromUnitLabel = (TextView) findViewById(R.id.fromUnitLabel);
                final TextView toUnitLabel = (TextView) findViewById(R.id.toUnitLabel);
                settingsIntent.putExtra("toUnit", toUnitLabel.getText());
                settingsIntent.putExtra("fromUnit", fromUnitLabel.getText());
                settingsIntent.putExtra("dMode", distanceMode);
                startActivityForResult(settingsIntent, UNIT_SELECTION);
                return true;

            case R.id.action_history:
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivityForResult(intent, HISTORY_RESULT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,
                10, mLocationListener);


        final EditText fromTextField = findViewById(R.id.fromTextField);
        final EditText toTextField = findViewById(R.id.toTextField);
        final TextView fromUnitLabel = (TextView) findViewById(R.id.fromUnitLabel);
        final TextView toUnitLabel = (TextView) findViewById(R.id.toUnitLabel);
        final TextView convLabel = (TextView) findViewById(R.id.convType);

        weatherIcon = findViewById(R.id.weatherIcon);
        current = findViewById(R.id.current);
        temperature = findViewById(R.id.temperature);


        fromTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else {
                    toTextField.setText("");
                }
            }
        });

        toTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else{
                    fromTextField.setText("");
                }
            }
        });


        Button clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromTextField.setText("");
                toTextField.setText("");
                hideKeyboard(view);
            }
        });


        Button modeButton = (Button) findViewById(R.id.modeButton);
        modeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                distanceMode = !distanceMode;
                if(distanceMode) {
                    fromUnitLabel.setText(R.string.yards);
                    toUnitLabel.setText(R.string.meters);
                    convLabel.setText(R.string.length_conversion);
                } else {
                    fromUnitLabel.setText(R.string.gallons);
                    toUnitLabel.setText(R.string.liters);
                    convLabel.setText(R.string.volume_conversion);
                }
                hideKeyboard(view);
            }
        });


        Button convertButton = (Button) findViewById(R.id.calculateButton);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lat = Double.toString(latitude);
                String lon = Double.toString(longitude);


                WeatherService.startGetWeather(MainActivity.this, lat, lon, "p1");

                //check mode
                if(distanceMode) {  // distance mode
                    UnitsConverter.LengthUnits fromUnits;
                    UnitsConverter.LengthUnits toUnits;
                    fromUnits = UnitsConverter.LengthUnits.valueOf(fromUnitLabel.getText().toString());
                    toUnits = UnitsConverter.LengthUnits.valueOf(toUnitLabel.getText().toString());

                    if (!fromTextField.getText().toString().matches("")) {   //fromTextField nopt empty
                        toTextField.setText(Double.toString(UnitsConverter.convert(Double.parseDouble(fromTextField.getText().toString()), fromUnits, toUnits))); //from -> to
                    } else if (!toTextField.getText().toString().matches("")) {
                        fromTextField.setText(Double.toString(UnitsConverter.convert(Double.parseDouble(toTextField.getText().toString()), toUnits, fromUnits))); //to -> from
                    }

                    HistoryContent.HistoryItem item = new HistoryContent.HistoryItem(Double.parseDouble(fromTextField.getText().toString()), Double.parseDouble(toTextField.getText().toString()), "Length",
                            toUnits.toString(), fromUnits.toString(), DateTime.now());


                    HistoryContent.addItem(item);
                    topRef.push().setValue(item);

                } else {    // volume mode
                    UnitsConverter.VolumeUnits fromUnits;
                    UnitsConverter.VolumeUnits toUnits;
                    fromUnits = UnitsConverter.VolumeUnits.valueOf(fromUnitLabel.getText().toString());
                    toUnits = UnitsConverter.VolumeUnits.valueOf(toUnitLabel.getText().toString());

                    if (!fromTextField.getText().toString().matches("")) {   //fromTextField nopt empty
                        toTextField.setText(Double.toString(UnitsConverter.convert(Double.parseDouble(fromTextField.getText().toString()), fromUnits, toUnits))); //from -> to
                    } else if (!toTextField.getText().toString().matches("")) {
                        fromTextField.setText(Double.toString(UnitsConverter.convert(Double.parseDouble(toTextField.getText().toString()), toUnits, fromUnits))); //to -> from
                    }

                    // remember the calculation.
                    HistoryContent.HistoryItem item = new HistoryContent.HistoryItem(Double.parseDouble(fromTextField.getText().toString()), Double.parseDouble(toTextField.getText().toString()), "Volume",
                            toUnits.toString(), fromUnits.toString(), DateTime.now());
                    HistoryContent.addItem(item);
                    topRef.push().setValue(item);

                }



                hideKeyboard(view);
            }
        });
    }


}
