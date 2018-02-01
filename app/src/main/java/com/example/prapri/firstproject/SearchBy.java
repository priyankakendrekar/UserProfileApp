package com.example.prapri.firstproject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class SearchBy extends AppCompatActivity {

    private Button searchButton;
    EditText searchField;
    TextView searchHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by);

        // init
        searchHeading = findViewById(R.id.ac_search_Heading);
        searchField = findViewById(R.id.ac_search_searchField);
        searchButton = findViewById(R.id.ac_search_searchButton);

        // Set Heading and Button text
        searchHeading.setText(R.string.searchbyHeading);
        searchButton.setText(R.string.search);
        searchField.setHint(R.string.searchHint);

        final SQLiteHelper db = new SQLiteHelper(this, "UserDB.sqlite", null, 1);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSearchToView = new Intent(SearchBy.this, ViewProfile.class);
                String typedText = searchField.getText().toString();

                if (typedText.compareTo("") == 0) {

                    Toast.makeText(SearchBy.this, R.string.emptyField, Toast.LENGTH_SHORT).show();

                } else {
                    intentSearchToView.putExtra("searchField", typedText);
                    intentSearchToView.putExtra("previousActivity", getString(R.string.searchActivity));
                    startActivity(intentSearchToView);
                }
            }
        });
    }
}
