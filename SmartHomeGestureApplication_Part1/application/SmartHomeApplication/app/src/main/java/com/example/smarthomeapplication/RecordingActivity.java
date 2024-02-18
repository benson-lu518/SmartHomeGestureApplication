package com.example.smarthomeapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecordingActivity extends Activity {

    private Uri videoPath;
    Bundle savedInstanceState;
    int uploadCount = 0;
    String selection;
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_screen);
        Bundle b = getIntent().getExtras();
        selection = b.getString("selection");

        // Create a new intent to control the camera
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File videoFile = filePath();
        Uri videoUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", videoFile);
        //System.out.println(selection); // debug
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        intent.putExtra(Intent.EXTRA_DURATION_MILLIS, 500);

        //Ensure camera permissions
        getCameraPermission();

        // Launch intent
        startActivityForResult(intent, 101);
    }

    /**
     * Generates the file and initializes the filename.
     * @return A new file, with the name being in the format [GESTURE NAME]_PRACTICE_[NUMBER].mp4.
     */
    private File filePath(){
        filename = selection + "_PRACTICE_" + uploadCount + ".mp4";
        String s = this.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + filename;
        System.err.println("PATH:" + s);
        return new File(s);
    }

    /**
     * Checks if camera permissions are granted, asks for permission if they are not.
     */
    private void getCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.CAMERA}, 101);
        }
    }

    /**
     * Onclick function for the redo button; reopens the camera intent and does not upload.
     * @param v  required by Android to match the method signature for onclick in the activity.xml
     */
    public void redoOnClick(View v){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File videoFile = filePath();
        Uri videoUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", videoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        intent.putExtra(Intent.EXTRA_DURATION_MILLIS, 5000);
        getCameraPermission();
        startActivityForResult(intent, 101);
    }

    /**
     * Uploads the currently recorded video.
     * @param v required by Android to match the method signature for onclick in the activity.xml
     */
    public void uploadOnClick(View v){
        //System.out.println("UC: " + uploadCount); //debug
        if (uploadCount == 3) Toast.makeText(this, "Maximum 3 Uploads!", Toast.LENGTH_SHORT).show();
        else{
            connectServer();
            uploadCount++;

            Intent previousPage = new Intent(RecordingActivity.this, MainActivity.class);
            startActivity(previousPage);

        }
    }

    /**
     * Override for activity result; required to update the video playback of the recently recorded
     * video by the user.
     * @param requestCode requestCode associated with the request; expected to be 101 (video record)
     * @param resultCode -1 if succesful, 1 if failure. (RESULT_OK == -1 ?)
     * @param intent The intent from which the activity originally launched.
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            videoPath = intent.getData();
            System.out.println("VIDEO URI: " + videoPath);
            VideoView vv = (VideoView) findViewById(R.id.preview);
            vv.setVideoURI(videoPath);
            vv.start();
        }
    }

    /**
     * connects to the server and uploads the video.
     * change "address" and "port" variables in order to change server.
     * Android requires that network operations run on a separate thread which is why it is used.
     * Uses OkHttpClient to connect to a flask server and POST a multipart form.
     */
    public void connectServer(){
         new Thread(new Runnable(){
             @Override
             public void run(){
                 try {
                     String address = "10.0.2.2";
                     String port = "8080";
                     String postUrl = "http://" + address  + ":" + port + "/";
                     File f = new File("/storage/emulated/0/Android/data/com.example.smarthomeapplication/files/DCIM/" + filename);
                     OkHttpClient client = new OkHttpClient();
                     RequestBody formBody = new MultipartBody.Builder()
                             .setType(MultipartBody.FORM)
                             .addFormDataPart("file", f.getName(),
                                     RequestBody.create(MediaType.parse("video/mp4"), f))
                             .build();
                     Request request = new Request.Builder().url(postUrl).post(formBody).build();

//                     Response response = client.newCall(request).execute();

                     try (Response response = client.newCall(request).execute()) {
                         if (response.isSuccessful()) {
                             // Handle successful response
                             System.out.println( "responce body:"+response.body().string());
                         } else {
                             // Handle unsuccessful response
                             System.out.println("responce code:"+ response.code());                         }
                     }
                 }catch(Exception e){
                     e.printStackTrace();
                 }
             }
         }).start();
      }
}
