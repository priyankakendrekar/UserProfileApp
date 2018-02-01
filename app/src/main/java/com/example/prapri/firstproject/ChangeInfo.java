package com.example.prapri.firstproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * Created by prapri on 21-01-2018.
 */

public class ChangeInfo extends AppCompatActivity{
    TextView name, dob, email, heading;
    EditText nameInput, dobInput, emailInput;
    Button imgUploadButton, nextButton, rotateButton;
    ImageView sampleImageView;
    String uid;
    Matrix matrix = new Matrix();
    Bitmap rotatedBitmap;
    float rotationFactor;

    final int REQUEST_CODE_GALLERY = 1,  REQUEST_CAMERA = 2;
    
	Utility util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
	 
		util = new Utility(ChangeInfo.this);

        //init
        heading = findViewById(R.id.ac_change_info_heading);
 //       uid = findViewById(R.id.ac_change_info_uid);
        name = findViewById(R.id.ac_change_info_name);
        dob = findViewById(R.id.ac_change_info_dob);
        email = findViewById(R.id.ac_change_info_email);
  //      uidInput = findViewById(R.id.ac_change_info_uidEdit);
        nameInput = findViewById(R.id.ac_change_info_nameEdit);
        dobInput = findViewById(R.id.ac_change_info_dobEdit);
        emailInput = findViewById(R.id.ac_change_info_emailEdit);
        sampleImageView = findViewById(R.id.ac_change_info_image);
        imgUploadButton = findViewById(R.id.ac_change_info_upload);
        nextButton = findViewById(R.id.ac_change_info_next);
        rotateButton    = findViewById(R.id.ac_change_info_rotate);

        // Set Heading and Button text
        Intent intentChangeinfo = getIntent();
        Bundle extras = getIntent().getExtras();
        uid = intentChangeinfo.getStringExtra("uid");
        String headingText = getString(R.string.uid)+" "+intentChangeinfo.getStringExtra("uid");
        heading.setText(headingText);

        name.setText(R.string.name);
        dob.setText(R.string.dob);
        email.setText(R.string.email);
        imgUploadButton.setText(R.string.uploadPhoto);
        nextButton.setText(R.string.next);
        rotateButton.setText(R.string.rotate);


        // Input field hints
        nameInput.setHint(R.string.nameHint);
        dobInput.setHint(R.string.dobHint);
        emailInput.setHint(R.string.emailHint);

        nameInput.setText(intentChangeinfo.getStringExtra("name"));
        dobInput.setText(intentChangeinfo.getStringExtra("DOB"));
        emailInput.setText(intentChangeinfo.getStringExtra("email"));

        if (intentChangeinfo.hasExtra("byteArray")) {
            Bitmap mBitmap = BitmapFactory.decodeByteArray(
                    intentChangeinfo.getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length
            );
//                getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
            sampleImageView.setImageBitmap(mBitmap);
        }
        
        imgUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				selectImage();
            }
        });

        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotationFactor = (sampleImageView.getRotation() + 90);
                sampleImageView.setRotation(sampleImageView.getRotation() + 90);
            }
        });
    

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rotatedBitmap = Bitmap.createBitmap(sampleImageView.getWidth(),
                        sampleImageView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(rotatedBitmap);
                sampleImageView.draw(canvas);


                matrix.postRotate(rotationFactor);
                rotatedBitmap = Bitmap.createBitmap(rotatedBitmap, 0, 0, rotatedBitmap.getWidth(), rotatedBitmap.getHeight(), matrix, true);

                ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, _bs);

                    Intent intentChangeToViewProfile = new Intent(ChangeInfo.this, ViewProfile.class);

                    intentChangeToViewProfile.putExtra("uid",uid);
                    intentChangeToViewProfile.putExtra("name", nameInput.getText().toString());
                    intentChangeToViewProfile.putExtra("DOB", dobInput.getText().toString());
                    intentChangeToViewProfile.putExtra("email", emailInput.getText().toString());
                    intentChangeToViewProfile.putExtra("byteArray", _bs.toByteArray());
                    intentChangeToViewProfile.putExtra("previousActivity", getString(R.string.changeInfoActivity));

                    startActivity(intentChangeToViewProfile);                    

                }
/*
                catch (Exception e){
                    e.printStackTrace();
                }
                }
*/

        });

    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void selectImage() {

       final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeInfo.this);
        builder.setTitle(R.string.addImageTitle);

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    if (util.cameraPermissionDenied()) {
                        util.requestPermissionCamera();
                    } else if (util.cameraPermissionGranted()){
                        //localToast("Permission Granted");
						util.captureImage();                        
                    } else {
                        //localToast("Permission??");
                    }
                } else if (items[i].equals("Gallery")) {
                    if (util.galleryPermissionDenied()) {
						util.requestPermissionGallery();
				} else if (util.galleryPermissionGranted()) {
					util.pickImage();
                }
               } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
               }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_GALLERY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    util.pickImage();
                } else {
                    boolean showRationale = false;

                    final int sdk = Build.VERSION.SDK_INT;
                    if (sdk >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                    }
                  
                    if (! showRationale) {
						util.accessAlert(R.string.appSettingGalleryPermission, R.string.appSettingGalleryNegativeButton);
     				} else {
						util.requestPermissionGallery();
                        // ActivityCompat.shouldShowRequestPermissionRationale(ChangeInfo.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                       // Toast.makeText(getApplicationContext(), R.string.permission_denied_gallery, Toast.LENGTH_SHORT).show();
                    }
                }
                return;
            }
            case REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                     util.captureImage();
                }
                else  {
                    boolean showRationale = false;

                    final int sdk = Build.VERSION.SDK_INT;
                    if (sdk >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                    }
         //           Toast.makeText(getApplicationContext(), "Please click on ALLOW to give Camera access", Toast.LENGTH_SHORT).show();
                    if (! showRationale) {
					  util.accessAlert(R.string.appSettingCameraPermission, R.string.appSettingCameraNegativeButton);

                    } else {
 					  util.requestPermissionCamera();
                        

                        //ActivityCompat.shouldShowRequestPermissionRationale(ChangeInfo.this, Manifest.permission.CAMERA);
                        //Toast.makeText(getApplicationContext(), R.string.permission_denied_gallery, Toast.LENGTH_SHORT).show();
                    }
                }

        }

        }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                Bundle bundle = data.getExtras();
                final Bitmap mBitmap;
                if (bundle != null) {
                    mBitmap = (Bitmap) bundle.get("data");
                    sampleImageView.setImageBitmap(mBitmap);
                }


            } else if (requestCode == REQUEST_CODE_GALLERY) {
                Uri uri = data.getData();

                try {
                    InputStream inputStream = null;
                    if (uri != null) {
                        inputStream = getContentResolver().openInputStream(uri);
                    }

                    Bitmap mBitmap = BitmapFactory.decodeStream(inputStream);
                    sampleImageView.setImageBitmap(mBitmap);
                    sampleImageView.setRotation(0);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }


    }


    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
