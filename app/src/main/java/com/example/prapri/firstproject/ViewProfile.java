package com.example.prapri.firstproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.prapri.firstproject.CreateProfile.imageViewToByte;

public class ViewProfile extends AppCompatActivity {
    private Button modifyButton, finishButton, deleteButton, pdfButton;
    TextView name, dob, email, uid, heading, nameInput, dobInput, emailInput;
    ImageView previewImageView;
    public static SQLiteHelper db;
    String uidNumber;
    Bitmap mBitmap;
    final int REQUEST_CODE_PDF = 3;
    Utility util;


    //Minimize app when Back is pressed on ViewProfile
    //Needed because we need to disallow user make another entry in db
    //Using different UID by going back
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        util = new Utility(ViewProfile.this);

        String previousActivity = getIntent().getExtras().getString("previousActivity");

        setContentView(R.layout.activity_view_profile);

        //init
        heading          = findViewById(R.id.ac_profile_heading);
        uid              = findViewById(R.id.ac_profile_uid);
        name             = findViewById(R.id.ac_profile_name);
        dob              = findViewById(R.id.ac_profile_dob);
        email            = findViewById(R.id.ac_profile_email);
        //uidInput         = findViewById(R.id.ac_profile_uidInput);
        nameInput        = findViewById(R.id.ac_profile_nameInput);
        dobInput         = findViewById(R.id.ac_profile_dobInput);
        emailInput       = findViewById(R.id.ac_profile_emailInput);
        previewImageView = findViewById(R.id.ac_profile_image);
        modifyButton     = findViewById(R.id.ac_profile_modifyButton);
        finishButton     = findViewById(R.id.ac_profile_finishButton);
        deleteButton     = findViewById(R.id.ac_profile_deleteButton);
        pdfButton        = findViewById(R.id.ac_profile_pdfButton);

        // Set Heading and Button text
        heading.setText(R.string.profileHeading);

        uid.setText(R.string.uid);
        name.setText(R.string.name);
        dob.setText(R.string.dob);
        email.setText(R.string.email);
        modifyButton.setText(R.string.modify);
        finishButton.setText(R.string.finish);
        deleteButton.setText(R.string.delete);
        pdfButton.setText(R.string.exportPdf);

        Intent intentViewProfile = getIntent();
        Bundle extras = getIntent().getExtras();

        db = new SQLiteHelper(this, "UserDB.sqlite", null, 1);

        if(previousActivity.compareTo( getString(R.string.searchActivity) ) == 0) {

            String searchField = intentViewProfile.getStringExtra("searchField");

            // Retrieve Existing Entry from Database
            User searchOutputUser = db.searchByUID(Integer.parseInt(searchField));

            //User searchOutputUser = new User();
            //searchOutputUser.setUID(-1);

            if (searchOutputUser.getUID() >= 0) {

                uidNumber = Integer.toString(searchOutputUser.getUID());
                uid.setText(getString(R.string.uid)+" "+uidNumber);
                byte[] image = searchOutputUser.getImage();
                mBitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

                ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, _bs);

                nameInput.setText(searchOutputUser.getName());

                dobInput.setText(searchOutputUser.getDOB());
                emailInput.setText(searchOutputUser.getEmail());

                previewImageView.setImageBitmap(mBitmap);
            } else {
//                uidInput.setText(searchField+" NOT FOUND!");
//                uidNumber = Integer.toString(searchOutputUser.getUID());
                uid.setText(getString(R.string.uid)+" "+searchField+" not found.");
                nameInput.setText("Not Found!");
                dobInput.setText("-");
                emailInput.setText("-");
                modifyButton.setText("cannot modify!");



                ViewGroup layout = (ViewGroup) modifyButton.getParent();
                if(null!=layout) //for safety only  as you are doing onClick
                    layout.removeView(modifyButton);

                layout = (ViewGroup) deleteButton.getParent();
                if(null!=layout) //for safety only  as you are doing onClick
                    layout.removeView(deleteButton);

                layout = (ViewGroup) pdfButton.getParent();
                if(null!= layout)
                    layout.removeView(pdfButton);

            }
        } else if(previousActivity.compareTo(getString(R.string.changeInfoActivity)) == 0) {

           // Show on Screen
//            uidInput.setText(intentViewProfile.getStringExtra("uid"));
            uidNumber = intentViewProfile.getStringExtra("uid");
            uid.setText(getString(R.string.uid)+" "+uidNumber);
            nameInput.setText(intentViewProfile.getStringExtra("name"));

            dobInput.setText(intentViewProfile.getStringExtra("DOB"));
            emailInput.setText(intentViewProfile.getStringExtra("email"));

            if(intentViewProfile.hasExtra("byteArray")) {
                mBitmap = BitmapFactory.decodeByteArray(
                        intentViewProfile.getByteArrayExtra("byteArray"),0, getIntent().getByteArrayExtra("byteArray").length
                );

                previewImageView.setImageBitmap(mBitmap);
            }

            // Update Existing Entry from Database
            db.updateData(
                    nameInput.getText().toString().trim(),
                    dobInput.getText().toString().trim(),
                    emailInput.getText().toString().trim(),
                    imageViewToByte(previewImageView),
                    Integer.parseInt(uidNumber.trim())
            );
            //Toast.makeText(ViewProfile.this, getString(R.string.afterUpdation), Toast.LENGTH_SHORT).show();

        } else if (previousActivity.compareTo(getString(R.string.createProfileActivity)) == 0) {
            //uidInput.setText(intentViewProfile.getStringExtra("uid"));
            uidNumber = intentViewProfile.getStringExtra("uid");
//            uid.setText(getString(R.string.uid)+" "+uidNumber);

            nameInput.setText(intentViewProfile.getStringExtra("name"));

            dobInput.setText(intentViewProfile.getStringExtra("DOB"));
            emailInput.setText(intentViewProfile.getStringExtra("email"));

            if(intentViewProfile.hasExtra("byteArray")) {
                 mBitmap = BitmapFactory.decodeByteArray(
                        intentViewProfile.getByteArrayExtra("byteArray"),0, getIntent().getByteArrayExtra("byteArray").length
                );

                previewImageView.setImageBitmap(mBitmap);
            }

            // Create New Entry in Database

            try {
                // First Time Initialization of Database
                db.queryData("CREATE TABLE IF NOT EXISTS UserTable(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, DOB VARCHAR, Email VARCHAR, image BLOB)");

                // Add new entry
                int rowid = db.insertData(
                        nameInput.getText().toString().trim(),
                        dobInput.getText().toString().trim(),
                        emailInput.getText().toString().trim(),
                        imageViewToByte(previewImageView)
                );
//                uidInput.setText(Integer.toString(rowid));
                uidNumber = Integer.toString(rowid);
                uid.setText(getString(R.string.uid)+" "+uidNumber);
         //       Toast.makeText(getApplicationContext(), getString(R.string.afterAddition), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (util.pdfPermissionDenied()) {
                    util.requestPermissionPdf();
                } else if (util.pdfPermissionGranted()){
                    //localToast("Permission Granted");
                    createPdf(ViewProfile.this);
                } else {
                    //localToast("Permission??");
                }
            }
        });


        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfileToChangeinfo = new Intent(ViewProfile.this, ChangeInfo.class);
                Bitmap mBitmap = Bitmap.createBitmap(previewImageView.getWidth(),
                        previewImageView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mBitmap);
                previewImageView.draw(canvas);
                previewImageView.setImageBitmap(mBitmap);

                ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, _bs);

                intentProfileToChangeinfo.putExtra("uid", uidNumber);
                intentProfileToChangeinfo.putExtra("name", nameInput.getText().toString());
                intentProfileToChangeinfo.putExtra("DOB", dobInput.getText().toString());
                intentProfileToChangeinfo.putExtra("email", emailInput.getText().toString());
                intentProfileToChangeinfo.putExtra("byteArray", _bs.toByteArray());

                startActivity(intentProfileToChangeinfo);
                //finish();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent finishintent2 = new Intent(ViewProfile.this,MainActivity.class);
                startActivity(finishintent2);

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete Existing Entry from Database
                db.deleteData(
                        Integer.parseInt(uidNumber.trim())
                );
              Toast.makeText(ViewProfile.this, getString(R.string.afterDeletion), Toast.LENGTH_SHORT).show();
                Intent finishintent2 = new Intent(ViewProfile.this, MainActivity.class);
                startActivity(finishintent2);
            }
                                        }

        );
    }

    private void createPdf(final Activity activity){
        // create a new document
       // int densityDpi = getResources().getDisplayMetrics().densityDpi;
       // DisplayMetrics metrics = getResources().getDisplayMetrics();
        //Toast.makeText(ViewProfile.this,,Toast.LENGTH_SHORT).show();
        PdfDocument document = new PdfDocument();
        float dpi = 100;
        // A4 Page Size 8.27 × 11.69 inches
        float a4_width = 8.27f;
        float a4_height = 11.69f;

        int widthInPixels = Math.round(a4_width*dpi);
        int heightInPixels = Math.round(a4_height*dpi);

        // crate a page description
        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(widthInPixels, heightInPixels, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw something on the page
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pdf_layout, null, false);

        Canvas pageCanvas = page.getCanvas();
        int pageWidth = pageCanvas.getWidth();
        int pageHeight = pageCanvas.getHeight();

        int measuredWidth = View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(pageHeight, View.MeasureSpec.EXACTLY);

        //localToast("height = "+Integer.toString(measuredHeight)+" "+Integer.toString(pageHeight));

        content.measure(measuredWidth, measuredHeight);
        content.layout(0, 0, pageWidth, pageHeight);

        TextView pdf_heading = content.findViewById(R.id.pdf_layout_heading);
        pdf_heading.setText(R.string.pdfHeading);

        TextView pdf_uid = content.findViewById(R.id.pdf_layout_uid);
        pdf_uid.setText(getString(R.string.uid)+" "+uidNumber);

        TextView pdf_name = content.findViewById(R.id.pdf_layout_name);
        pdf_name.setText(name.getText().toString());
        TextView pdf_nameInput = content.findViewById(R.id.pdf_layout_nameInput);
        pdf_nameInput.setText(nameInput.getText().toString());

        TextView pdf_dob = content.findViewById(R.id.pdf_layout_dob);
        pdf_dob.setText(dob.getText().toString());
        TextView pdf_dobInput = content.findViewById(R.id.pdf_layout_dobInput);
        pdf_dobInput.setText(dobInput.getText().toString());

        TextView pdf_email = content.findViewById(R.id.pdf_layout_email);
        pdf_email.setText(email.getText().toString());
        TextView pdf_emailInput = content.findViewById(R.id.pdf_layout_emailInput);
        pdf_emailInput.setText(emailInput.getText().toString());

        ImageView pdf_image = content.findViewById(R.id.pdf_layout_image);
        pdf_image.setImageBitmap(mBitmap);

        content.draw(pageCanvas);

        // finish the page
        document.finishPage(page);

        // write the document content
        final String targetPdf = Environment.getExternalStorageDirectory().getPath()
                +"/uid"+uidNumber+"_profile.pdf";
        final File filePath = new File(targetPdf);
       // Uri uri= FileProvider.getUriForFile(ViewProfile.this, getPackageName() + ".provider", filePath);

        try {
            document.writeTo(new FileOutputStream(filePath));
            //util.localToastLong("Done. Please check "+targetPdf);

            final AlertDialog.Builder builder = new AlertDialog.Builder(ViewProfile.this);
            builder.setTitle("PDF Location");
            builder.setMessage(targetPdf);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Open PDF
                    Intent intentOpenPDF = new Intent(Intent.ACTION_VIEW);
                    Uri uri = FileProvider.getUriForFile(ViewProfile.this, BuildConfig.APPLICATION_ID + ".provider", filePath);
                    intentOpenPDF.setDataAndType(uri, "application/pdf");
                    intentOpenPDF.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //intentOpenPDF.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intentOpenPDF);
                }
            });
            builder.show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(),
                    Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }

}

