package extensibleapps.vandy.mooc.locationtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper that is used to access the Location Logging Database
 */
public class LocDBSQLHelper extends SQLiteOpenHelper{

    /**
     *  Constant defining the string to store "TEXT" types
     *  in SQL
     */
    private static final String TEXT_TYPE = " TEXT";

    /**
     * Comma separation Constant
     */
    private static final String COMMA_SEP = ",";

    /**
     * Creates the table storing location logs
     */
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LocDBContract.FeedEntry.TABLE_NAME + " (" +
                    LocDBContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_TIME + TEXT_TYPE + COMMA_SEP +
                    LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_LATITUDE + TEXT_TYPE + COMMA_SEP +
                    LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_lONGITUDE + TEXT_TYPE + COMMA_SEP +
                    LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_DESCRIPTION + TEXT_TYPE +
            " )";

    /**
     * Deletes the table storing location logs
     */
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LocDBContract.FeedEntry.TABLE_NAME;

    /**
     * Current version of the database. Needs to be updated any time a change
     * to the database's structure is released
     */
    public static final int DB_Version = 1;

    /**
     * Name of the location logging database
     */
    public static final String DB_Name = "LocLogDB.db";

    /**
     * Constructor for the helper
     * @param context
     */
    public LocDBSQLHelper(Context context) {
        super(context, DB_Name, null, DB_Version);
    }

    /**
     * This method is called the first time the database is created, and
     * creates the database.
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     * This method is called when the database version has been changed.
     * It migrates the outdated implementation to the new structure.
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // We simply delete the old information and create a new table.
        // Notice that all information stored in the old table is lost.
        // A more robust solution would migrate the data before deleting.
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
