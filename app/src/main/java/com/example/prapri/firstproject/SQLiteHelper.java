package com.example.prapri.firstproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.ContactsContract;
import android.util.Log;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.widget.Toast;

/**
 * Created by prapri on 19-01-2018.
 */


public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLiteHelper";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DOB = "DOB";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_IMAGE = "IMAGE";

    private static final String[] COLUMNS = {KEY_ID, KEY_NAME, KEY_DOB, KEY_EMAIL, KEY_IMAGE};

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public int insertData(String name, String DOB, String Email, byte[] image){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO UserTable VALUES (NULL, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, DOB);
        statement.bindString(3,Email);

        statement.bindBlob(4, image);

        int rowid = (int)statement.executeInsert();
        database.close();
        return rowid;
        //Log.d(TAG, "addData: Adding " + name + " to " +UserTable);
    }

    public void updateData(String name, String DOB, String Email, byte[] image, int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE UserTable SET name = ?, DOB = ?, Email = ?, image = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindString(2, DOB);
        statement.bindString(3, Email);
        statement.bindBlob(4, image);
        statement.bindDouble(5, (double)id);

        statement.execute();
        database.close();
    }

    public  void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM UserTable WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public User searchByUID(int id){
        // 1. get reference to readable DB
        SQLiteDatabase database = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                database.query("UserTable", // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        User user = new User();

          if(cursor.moveToFirst()) {
            // 4. build user object
            user.setUID(Integer.parseInt(cursor.getString(0)));
            user.setName(cursor.getString(1));
            user.setDOB(cursor.getString(2));
            user.setEmail(cursor.getString(3));
            byte[] image = cursor.getBlob(4);

            user.setImage(image);
            database.close();
            // 5. return user
            return user;
        } else {
            // No results found!
              user.setUID(-1);
        }

        database.close();
          cursor.close();

        return user;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
