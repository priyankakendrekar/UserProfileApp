package com.example.prapri.firstproject;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by prapri on 28-01-2018.
 */


public class Utility extends ContextWrapper {
    final int REQUEST_CODE_GALLERY = 1,  REQUEST_CAMERA = 2, REQUEST_CODE_PDF = 3;
    Activity activity;

    public Utility(Context base) {
        super(base);
        Activity activity = (Activity) base;
        this.activity = activity;
    }

    public void localToast(String toPrint) {
        Toast.makeText(this, toPrint, Toast.LENGTH_SHORT).show();
    }

    public void localToastLong(String toPrint) {
        Toast.makeText(this, toPrint, Toast.LENGTH_LONG).show();
    }

    public void requestPermissionCamera(){
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.CAMERA},
                REQUEST_CAMERA
        );
    }

    public void requestPermissionGallery(){
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_GALLERY
        );
    }
    public void requestPermissionPdf(){
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_PDF
        );
    }

    public void captureImage(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, REQUEST_CAMERA);
        }
    }

    public void pickImage(){
        Intent selectImageIntent = new Intent(Intent.ACTION_PICK);
        if (selectImageIntent.resolveActivity(getPackageManager()) != null) {
            selectImageIntent.setType("image/*");
            activity.startActivityForResult(selectImageIntent, REQUEST_CODE_GALLERY);
        }
    }

    public void accessAlert(int accessMessage, int accessNegativeButton){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(R.string.grantAccessTitle)
                .setMessage(accessMessage)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));

                    }
                })
                .setNegativeButton(accessNegativeButton, new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public boolean cameraPermissionDenied(){
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission
                .CAMERA)
                == PackageManager.PERMISSION_DENIED);
    }

    public boolean cameraPermissionGranted(){
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission
                .CAMERA)
                == PackageManager.PERMISSION_GRANTED);
    }

    public boolean galleryPermissionDenied(){
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission
                .READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED);
    }

    public boolean galleryPermissionGranted(){
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission
                .READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }

    public boolean pdfPermissionDenied(){
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission
                .WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED);
    }

    public boolean pdfPermissionGranted(){
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission
                .WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }

}
