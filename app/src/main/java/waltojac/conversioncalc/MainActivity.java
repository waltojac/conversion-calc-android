package waltojac.conversioncalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    public static final int UNIT_SELECTION = 1;
    boolean distanceMode = true;

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
