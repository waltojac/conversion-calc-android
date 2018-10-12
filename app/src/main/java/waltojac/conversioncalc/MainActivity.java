package waltojac.conversioncalc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    public static final int UNIT_SELECTION = 1;
    boolean distanceMode = true;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == UNIT_SELECTION){
            final TextView fromUnitLabel = (TextView) findViewById(R.id.fromUnitLabel);
            final TextView toUnitLabel = (TextView) findViewById(R.id.toUnitLabel);

            fromUnitLabel.setText(data.getStringExtra("fromSelect"));
            toUnitLabel.setText(data.getStringExtra("toSelect"));
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView fromTextField = (TextView) findViewById(R.id.fromTextField);
        final TextView toTextField = (TextView) findViewById(R.id.toTextField);
        final TextView fromUnitLabel = (TextView) findViewById(R.id.fromUnitLabel);
        final TextView toUnitLabel = (TextView) findViewById(R.id.toUnitLabel);

        Button clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromTextField.setText("");
                toTextField.setText("");
            }
        });


        Button modeButton = (Button) findViewById(R.id.modeButton);
        modeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                distanceMode = !distanceMode;
                if(distanceMode) {
                    fromUnitLabel.setText("Yards");
                    toUnitLabel.setText("Miles");
                } else {
                    fromUnitLabel.setText("Gallons");
                    toUnitLabel.setText("Liters");
                }
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
                }
            }
        });
    }


}
