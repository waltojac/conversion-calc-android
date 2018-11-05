package waltojac.conversioncalc;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.joda.time.DateTime;

import waltojac.conversioncalc.dummy.HistoryContent;


public class MainActivity extends AppCompatActivity {
    public static final int UNIT_SELECTION = 1;
    public static final int HISTORY_RESULT = 1;

    boolean distanceMode = true;

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == UNIT_SELECTION){
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
                startActivityForResult(intent, HISTORY_RESULT );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final EditText fromTextField = findViewById(R.id.fromTextField);
        final EditText toTextField = findViewById(R.id.toTextField);
        final TextView fromUnitLabel = (TextView) findViewById(R.id.fromUnitLabel);
        final TextView toUnitLabel = (TextView) findViewById(R.id.toUnitLabel);
        final TextView convLabel = (TextView) findViewById(R.id.convType);

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

                    // remember the calculation.
                    HistoryContent.HistoryItem item = new HistoryContent.HistoryItem(Double.parseDouble(fromTextField.getText().toString()), Double.parseDouble(toTextField.getText().toString()), "Length",
                            toUnits.toString(), fromUnits.toString(), DateTime.now());
                    HistoryContent.addItem(item);
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
                }



                hideKeyboard(view);
            }
        });
    }


}
