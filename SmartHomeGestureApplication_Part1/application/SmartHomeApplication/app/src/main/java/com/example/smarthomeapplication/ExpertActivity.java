package com.example.smarthomeapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class ExpertActivity extends Activity {
    int watchCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expert_gesture);

        TextView tv = (TextView) findViewById(R.id.expertTitle);
        Bundle b = getIntent().getExtras();
        String selection = b.getString("selection");
        tv.setText(b.getString(selection));

        int id = parseSelection(selection);
        if (id != 0) {
            // Preview the correct video based upon previous selection on prior screen.
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + id);
            VideoView vv = (VideoView) findViewById(R.id.videoView);
            vv.setVideoURI(video);
            vv.start();
        }
    }

    /**
     *
     * @param selection the String representation of the selection
     * @return id , the integer constant for the corresponding resource file selected.
     */

    private int parseSelection(String selection){
        // Switch the file name depending on the selection from previous screen, selection
        // is passed from Bundle.
        switch (selection){
            case "Turn on lights":
                return R.raw.hlighton;
            case "Turn off lights":
                return R.raw.hlightoff;
            case "Turn on fan":
                return R.raw.hfanon;
            case "Turn off fan":
                return R.raw.hfanoff;
            case "Increase fan speed":
                return R.raw.hincreasefanspeed;
            case "Decrease fan speed":
                return R.raw.hdecreasefanspeed;
            case "Set Thermostat to specified temperature":
                return R.raw.hsettherme;
            case "0":
                return R.raw.h0;
            case "1":
                return R.raw.h1;
            case "2":
                return R.raw.h2;
            case "3":
                return R.raw.h3;
            case "4":
                return R.raw.h4;
            case "5":
                return R.raw.h5;
            case "6":
                return R.raw.h6;
            case "7":
                return R.raw.h7;
            case "8":
                return R.raw.h8;
            case "9":
                return R.raw.h9;
        }
        return 0;
    }

    /**
     * Onclick handler for rewatch button; simply replays the video. The reason why the video file
     * is reloaded is to fix a bug whereby the user may break the expected flow of the program and
     * land on a selection screen at an unexpected time by using the back button.
     * @param v Required by Android for matching the method signature of onclick.
     */
    public void rewatchOnClick(View v){
//        if (watchCount < 3) {
        Bundle b = getIntent().getExtras();
        String selection = b.getString("selection");
        VideoView vv = (VideoView) findViewById(R.id.videoView);
        if (vv.isPlaying()){
            vv.stopPlayback();
        }
        vv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + parseSelection(selection)));
        vv.start();
//            ++watchCount;
//        }
//        else{
//            Toast.makeText(this, "Maximum of 3 re-watches.", Toast.LENGTH_SHORT).show();
//        }
    }

    /**
     * On click handldr for the practice button, launches the RecordingActivity.
     * @param v Required by Android for matching the method signature of onclick.
     */
    public void practiceOnClick(View v){
        Intent intent = new Intent(v.getContext(), RecordingActivity.class);
        intent.putExtras(getIntent().getExtras());
        startActivity(intent);
    }
}
