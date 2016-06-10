package com.example.avb150.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    private Button bCam;
    private Button bLocalisation;
    private Button bSave;
    private ImageView imageView;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 1200;
    private Uri fileUri;
    Bitmap help1;
    ThumbnailUtils thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bCam = (Button) findViewById(R.id.buttonCamera);
        bLocalisation = (Button) findViewById(R.id.buttonLocalisation);
        bSave = (Button) findViewById(R.id.buttonSauvegarde);
        imageView = (ImageView)this.findViewById(R.id.ivImage);

        bCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE)); // create a file to save the image
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        bLocalisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.popUpLocalisationOn)
                        .setPositiveButton(R.string.btnConfirmation, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO remplacer par carto
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                            }
                        })
                        .setNegativeButton(R.string.cancelLocalisation, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
//            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//                if (resultCode == RESULT_OK) {
//                    Toast.makeText(this, "Image saved to:\n" +
//                            data.getData(), Toast.LENGTH_LONG).show();
//                    imageView.setImageBitmap((Bitmap) data.getExtras().get("data"));
//                } else if (resultCode == RESULT_CANCELED) {
//                    Toast.makeText(this, R.string.cancelPhoto, Toast.LENGTH_LONG).show();
//                } else {
//                    // Image capture failed, advise user
//                }
//            }
            switch (requestCode) {
                case PICK_IMAGE_REQUEST://actionCode
                    if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                        //For Image Gallery
                    }
                    return;

                case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE://actionCode
                    if (resultCode == RESULT_OK) {
                        Toast.makeText(this, "Image saved to:\n" +
                            data.getData(), Toast.LENGTH_LONG).show();
                    imageView.setImageBitmap((Bitmap) data.getExtras().get("data"));
                        return;
                    }
            }
        }catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                try {
                    help1 = MediaStore.Images.Media.getBitmap(getContentResolver(),fileUri);
                    imageView.setImageBitmap( thumbnail.extractThumbnail(help1,imageView.getWidth(), 800));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Spontaneo");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Spontaneo", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
        return mediaFile;
    }
}
