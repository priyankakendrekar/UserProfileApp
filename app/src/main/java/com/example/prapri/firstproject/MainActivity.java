package com.example.prapri.firstproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity  {

    private Button CreateProfileButton;
    private Button ViewProfileButton;
    public static SQLiteHelper db;

    //Minimize app when Back is pressed on MainActivity
    //Needed because we need to disallow user to see past entries in history
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new SQLiteHelper(this, "UserDB.sqlite", null, 1);
        db.queryData("CREATE TABLE IF NOT EXISTS UserTable(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, DOB VARCHAR, Email VARCHAR, image BLOB)");
        db.close();
        CreateProfileButton = findViewById(R.id.CreateProfile);
        CreateProfileButton.setText(R.string.createProfile);
        CreateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMainToCreate = new Intent(MainActivity.this,CreateProfile.class);
                startActivity(intentMainToCreate);
                //finish();
            }
        });
        ViewProfileButton = findViewById(R.id.ViewProfile);
        ViewProfileButton.setText(R.string.viewProfile);
        ViewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMainToSearch = new Intent(MainActivity.this,SearchBy.class);
                startActivity(intentMainToSearch);
            }
        });
    }
}
