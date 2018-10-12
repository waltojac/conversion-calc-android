package waltojac.conversioncalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ModeSelectionActicity extends AppCompatActivity {
    private String fromSelection = "Miles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_selection_acticity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();*/
                Intent intent = new Intent();
                intent.putExtra("fromDistance", fromSelection);
                setResult(MainActivity.UNIT_SELECTION);
                finish();
            }
        });
    }

    Spinner fromSpinner = (Spinner) findViewById(R.id.fromChoice);
    Spinner toSpinner = (Spinner) findViewById(R.id.toChoice);

    //check mode
    /*if(true) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.distances, android.R.layout.simple_spinner_item);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.distances, android.R.layout.simple_spinner_item);

        /*adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSeledtedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromSelection = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }
        });
    }*/
}
