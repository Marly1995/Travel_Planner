package gilliam.marlon.com.travel_planner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import static gilliam.marlon.com.travel_planner.Home.PRAGMA_KEY;
import static gilliam.marlon.com.travel_planner.Home.pass;

/**
 * Created by Computing on 17/12/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Travel_Database.db";

    public static final String LOCATIONS_TABLE_NAME = "Locations_Table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Location";
    public static final String COL_3 = "Description";
    public static final String COL_4 = "Latitude";
    public static final String COL_5 = "Longitude";

    public static final String PICTURES_TABLE_NAME = "Pictures_Table";
    public static final String P_COL_1 = "ID";
    public static final String P_COL_2 = "Location_Id";
    public static final String P_COL_3 = "Picture";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase.loadLibs(context);
        SQLiteDatabase db = this.getWritableDatabase("");
        db.rawExecSQL(PRAGMA_KEY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(("create table " + LOCATIONS_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, LOCATION TEXT, DESCRIPTION TEXT, LATITUDE FLOAT, LONGITUDE FLOAT)"));
        db.execSQL(("create table " + PICTURES_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, LOCATION_ID INTEGER, PICTURE BLOB)"));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LOCATIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PICTURES_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String location, String description, String latitude, String longitude)
    {
        SQLiteDatabase db = this.getWritableDatabase(pass);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, location);
        contentValues.put(COL_3, description);
        contentValues.put(COL_4, latitude);
        contentValues.put(COL_5, longitude);
        long result = db.insert(LOCATIONS_TABLE_NAME, null, contentValues);
        if (result == -1)
        {
            return false;
        }
        else { return true;}
    }

    public boolean insertPicture(String location_id, byte[] image)
    {
        SQLiteDatabase db = this.getWritableDatabase(pass);
        ContentValues contentValues = new ContentValues();
        contentValues.put(P_COL_2, location_id);
        contentValues.put(P_COL_3, image);
        long result = db.insert(PICTURES_TABLE_NAME, null, contentValues);
        if (result == -1)
        {
            return false;
        }
        else { return true; }
    }

    public Cursor getPicture(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase(pass);
        String[] projection = {P_COL_1, P_COL_2, P_COL_3};
        String where = P_COL_2 + " = ?";
        String[] whereArgs = {id};
        Cursor res = db.query(PICTURES_TABLE_NAME, projection, where, whereArgs, null, null, null);
        return res;
    }
    public Cursor getData()
    {
        SQLiteDatabase db = this.getWritableDatabase(pass);
        Cursor res = db.rawQuery("select * from " + LOCATIONS_TABLE_NAME, null);
        return res;
    }

    public Cursor getPlace(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase(pass);
        String[] projection = {COL_1, COL_2, COL_3, COL_4, COL_5};
        String where = COL_1 + " = ?";
        String[] whereArgs = {id};
        Cursor res = db.query(LOCATIONS_TABLE_NAME, projection, where, whereArgs, null, null, null);
        return res;
    }

    public boolean updateData(String id, String location, String description, String latitude, String longitude)
    {
        SQLiteDatabase db = this.getWritableDatabase(pass);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, location);
        contentValues.put(COL_3, description);
        contentValues.put(COL_4, latitude);
        contentValues.put(COL_5, longitude);

        db.update(LOCATIONS_TABLE_NAME, contentValues, "ID = ?", new String[] {id});
        return true;
    }

    public Integer deleteData (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase(pass);
        return db.delete(LOCATIONS_TABLE_NAME, "ID = ?", new String[] {id});
    }

    public Integer deletePicture (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase(pass);
        return db.delete(PICTURES_TABLE_NAME, "ID = ?", new String[] {id});
    }
}
