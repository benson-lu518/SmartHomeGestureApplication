package com.example.smarthomeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    Button selectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateSpinner();
        selectButton = (Button) findViewById(R.id.confirm_selection_button);
    }

    /**
     * Populates the Spinner object on the Main Activity with the Spinner items.
     */
    protected void populateSpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.video_spinner);
        ArrayAdapter<CharSequence> a = ArrayAdapter.createFromResource(this, R.array.videos_array, android.R.layout.simple_spinner_item);
    }

    /**
     * on click handler for the select button. selects the current choice from the spinner menu and
     * changes the activity to the expert activity, passing along the information of the choice
     * selected.
     */
    public void selectButtonOnClick(View v){
       Spinner spinner = (Spinner) findViewById(R.id.video_spinner);
       Object selection = spinner.getSelectedItem();
       String select;
        if (selection != null) {
            select = selection.toString();
        }
        else {
            select = "Turn on lights";
        }
       Intent intent = new Intent(v.getContext(), ExpertActivity.class);
       Bundle b = new Bundle();
       b.putString("selection", select);
       intent.putExtras(b);
       startActivity(intent);
    }
}