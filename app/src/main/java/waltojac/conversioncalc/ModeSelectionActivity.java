package waltojac.conversioncalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Spinner;

import static java.lang.Boolean.parseBoolean;

public class ModeSelectionActivity extends AppCompatActivity {
    private String fromSelection = "";
    private String toSelection = "";
    private boolean distanceMode;
    private int toIndex;
    private int fromIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_selection_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        toSelection = intent.getStringExtra("toUnit");
        fromSelection = intent.getStringExtra("fromUnit");
        distanceMode = intent.getBooleanExtra("dMode", true);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("fromSelect", fromSelection);
                intent.putExtra("toSelect", toSelection);
                setResult(MainActivity.UNIT_SELECTION, intent);
                finish();
            }
        });


        Spinner fromSpinner = (Spinner) findViewById(R.id.fromChoice);
        Spinner toSpinner = (Spinner) findViewById(R.id.toChoice);

        ArrayAdapter<CharSequence> adapter;
        //check mode
        if (distanceMode) {
             adapter = ArrayAdapter.createFromResource(this, R.array.distances, android.R.layout.simple_spinner_item);
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

             for (int i = 0; i < 3; i++){
                 if (fromSelection.equals(getResources().getStringArray(R.array.distances)[i])){
                    fromIndex = i;
                 }
             }
            for (int i = 0; i < 3; i++){
                if (toSelection.equals(getResources().getStringArray(R.array.distances)[i])){
                    toIndex = i;
                }
            }

        } else {
            adapter = ArrayAdapter.createFromResource(this, R.array.volumes, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            for (int i = 0; i < 3; i++){
                if (fromSelection.equals(getResources().getStringArray(R.array.volumes)[i])){
                    fromIndex = i;
                }
            }
            for (int i = 0; i < 3; i++){
                if (toSelection.equals(getResources().getStringArray(R.array.volumes)[i])){
                    toIndex = i;
                }
            }
        }


        fromSpinner.setAdapter(adapter);
        fromSpinner.setSelection(fromIndex);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromSelection = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        toSpinner.setAdapter(adapter);
        toSpinner.setSelection(toIndex);

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toSelection = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
