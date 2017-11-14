package extensibleapps.vandy.mooc.locationtracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import edu.vanderbilt.grader.rubric.Rubric;

//@RunWith(AndroidJUnit4.class)
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)

public class DBTest extends AndroidTestCase {

    /**
     * Instance of the database manager that is being tested.
     */
    private LocLogDBManager mDB;

    /*
        Constants used as data entries for testing
     */
    private static String TIME1 = "1";
    private static String LAT1 = "11";
    private static String LONGI1 = "111";
    private static String DESC1 = "1111";

    private static String TIME2 = "2";
    private static String LAT2 = "22";
    private static String LONGI2 = "222";
    private static String DESC2 = "2222";

    private static String TIME2A = "2";
    private static String LAT2A = "22a";
    private static String LONGI2A = "222a";
    private static String DESC2A = "2222a";

    private static String NOT_IN_DB = "0";

    /**
     * Method called before testing begins. Instantiates the db manager
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        mDB = new LocLogDBManager(RuntimeEnvironment.application);
    }

    ///////////////////////////////////////////////////////////////////
    // Test Helper Methods
    ///////////////////////////////////////////////////////////////////

    /**
     * Enters example data in the db
     * @throws Exception
     */
    private void dataEntryHelper() throws Exception {

        // Just make sure that no exceptions thrown when inserting
        // TODO Could modify storeLocationData to return id of row
        mDB.storeLocationData(TIME1, LAT1, LONGI1, DESC1);
        mDB.storeLocationData(TIME2, LAT2, LONGI2, DESC2);
    }

    /**
     * Resets the db to a clean state
     */
    private void resetDB() {
        mDB.deleteEntries(null, null, null, null);
    }

    ///////////////////////////////////////////////////////////////////
    // Test Entry
    ///////////////////////////////////////////////////////////////////
    @Rubric(
            value = "testDataEntry()",
            goal = "Test if the database can store new data without throwing exception",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDataEntry() throws Exception{
        dataEntryHelper();
        resetDB();
    }

    ///////////////////////////////////////////////////////////////////
    // Test Queries
    ///////////////////////////////////////////////////////////////////
    @Rubric(
            value = "testDataQueryCounts()",
            goal = "Checks if db counts are as expected when storing a querying data",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDataQuery1() throws Exception {

        // Test 1 entry
        mDB.storeLocationData(TIME1, LAT1, LONGI1, DESC1);
        Cursor cur = mDB.queryEntries(null, null, null, null);
        assertEquals(cur.getCount(), 1);
        resetDB();

        // Test 2 entries
        dataEntryHelper();
        cur = mDB.queryEntries(null, null, null, null);
        assertEquals(cur.getCount(), 2);
        resetDB();

    }

    @Rubric(
            value = "testDataQuerySingle()",
            goal = "Tests that the query only returns correct " +
                    "entry when multiple are stored, but" +
                    "one is queried for",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDataQuerySingle() throws Exception {

        dataEntryHelper();

        Cursor cur = mDB.queryEntries(TIME1, LAT1, LONGI1, DESC1);

        assertEquals(cur.getCount(), 1);
        cur.moveToFirst();
        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_TIME)),
                TIME1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_LATITUDE)),
                LAT1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_lONGITUDE)),
                LONGI1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_DESCRIPTION)),
                DESC1);

        resetDB();
    }

    @Rubric(
            value = "testDataQuerySingleTime()",
            goal = "Tests that the query only returns correct entry when just time given",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDataQuerySingleTime() throws Exception {

        dataEntryHelper();

        Cursor cur = mDB.queryEntries(TIME1, null, null, null);

        assertEquals(cur.getCount(), 1);
        cur.moveToFirst();
        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_TIME)),
                TIME1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_LATITUDE)),
                LAT1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_lONGITUDE)),
                LONGI1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_DESCRIPTION)),
                DESC1);

        resetDB();
    }

    @Rubric(
            value = "testDataTimeMulti()",
            goal = "Tests that the query returns both correct " +
                    "entries when they have same " +
                    "time and time is used to query",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDataTimeMulti() throws Exception {

        dataEntryHelper();
        mDB.storeLocationData(TIME2A, LAT2A, LONGI2A, DESC2A);

        Cursor cur = mDB.queryEntries(TIME2, null, null, null);
        assertEquals(cur.getCount(), 2);

        resetDB();
    }

    @Rubric(
            value = "testDataQueryNullTime()",
            goal = "Test that null works for time value",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDataQueryNullTime() throws Exception {

        dataEntryHelper();
        Cursor cur = mDB.queryEntries(null, LAT2, LONGI2, DESC2);


        assertEquals(cur.getCount(), 1);
        cur.moveToFirst();
        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_TIME)),
                TIME2);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_LATITUDE)),
                LAT2);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_lONGITUDE)),
                LONGI2);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_DESCRIPTION)),
                DESC2);

        resetDB();

    }

    @Rubric(
            value = "testDataNullLat()",
            goal = "Test that null works for lat value",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDataNullLat() throws Exception {

        dataEntryHelper();

        Cursor cur = mDB.queryEntries(TIME2, null, LONGI2, DESC2);

        assertEquals(cur.getCount(), 1);
        cur.moveToFirst();
        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_TIME)),
                TIME2);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_LATITUDE)),
                LAT2);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_lONGITUDE)),
                LONGI2);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_DESCRIPTION)),
                DESC2);

        resetDB();

    }

    @Rubric(
            value = "testDataNullLong()",
            goal = "Test that null works for long value",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDataNullLong() throws Exception {

        dataEntryHelper();

        Cursor cur = mDB.queryEntries(TIME2, LAT2, null, DESC2);

        assertEquals(cur.getCount(), 1);
        cur.moveToFirst();
        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_TIME)),
                TIME2);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_LATITUDE)),
                LAT2);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_lONGITUDE)),
                LONGI2);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_DESCRIPTION)),
                DESC2);

        resetDB();

    }

    @Rubric(
            value = "testDataQueryNullDesc()",
            goal = "Test that null works for desc valu",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDataQueryNullDesc() throws Exception {

        dataEntryHelper();

        Cursor cur = mDB.queryEntries(TIME2, LAT2, LONGI2, null);


        assertEquals(cur.getCount(), 1);
        cur.moveToFirst();
        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_TIME)),
                TIME2);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_LATITUDE)),
                LAT2);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_lONGITUDE)),
                LONGI2);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_DESCRIPTION)),
                DESC2);

        resetDB();

    }

    @Rubric(
            value = "testDataQueryOnlyTime()",
            goal = "Test that time can be used by itself",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDataQueryOnlyTime() throws Exception {

        dataEntryHelper();

        Cursor cur = mDB.queryEntries(TIME1, null, null, null);


        assertEquals(cur.getCount(), 1);
        cur.moveToFirst();
        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_TIME)),
                TIME1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_LATITUDE)),
                LAT1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_lONGITUDE)),
                LONGI1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_DESCRIPTION)),
                DESC1);

        resetDB();

    }

    @Rubric(
            value = "testDataQueryOnlyLat()",
            goal = "Test that lat can be used by itself",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDataQueryOnlyLat() throws Exception {

        dataEntryHelper();

        Cursor cur = mDB.queryEntries(null, LAT1, null, null);


        assertEquals(cur.getCount(), 1);
        cur.moveToFirst();
        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_TIME)),
                TIME1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_LATITUDE)),
                LAT1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_lONGITUDE)),
                LONGI1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_DESCRIPTION)),
                DESC1);

        resetDB();

    }

    @Rubric(
            value = "testDataQueryOnlyLong()",
            goal = "Test that long can be used by itself",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDataQueryOnlyLong() throws Exception {

        dataEntryHelper();

        Cursor cur = mDB.queryEntries(null, null, LONGI1, null);


        assertEquals(cur.getCount(), 1);
        cur.moveToFirst();
        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_TIME)),
                TIME1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_LATITUDE)),
                LAT1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_lONGITUDE)),
                LONGI1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_DESCRIPTION)),
                DESC1);

        resetDB();

    }

    @Rubric(
            value = "testDataQueryOnlyDesc()",
            goal = "Test that desc can be used by itself",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDataQueryOnlyDesc() throws Exception {

        dataEntryHelper();

        Cursor cur = mDB.queryEntries(null, null, null, DESC1);


        assertEquals(cur.getCount(), 1);
        cur.moveToFirst();
        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_TIME)),
                TIME1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_LATITUDE)),
                LAT1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_lONGITUDE)),
                LONGI1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_DESCRIPTION)),
                DESC1);

        resetDB();

    }

    @Rubric(
            value = "testDataQueryNotInDB()",
            goal = "Test that nothing is returned if something is queried that" +
                    "doesn't exist",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDataQueryNotInDB() throws Exception {

        dataEntryHelper();

        Cursor cur = mDB.queryEntries(NOT_IN_DB, null, null, null);

        assertEquals(cur.getCount(), 0);

        resetDB();

    }

    ///////////////////////////////////////////////////////////////////
    // Test Delete
    ///////////////////////////////////////////////////////////////////

    @Rubric(
            value = "testDeleteSimple()",
            goal = "Test that delete works in general",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDeleteSimple() throws Exception {

        dataEntryHelper();

        assertEquals(mDB.deleteEntries(TIME1, LAT1, LONGI1, DESC1), 1);

        Cursor cur = mDB.queryEntries(TIME1, LAT1, LONGI1, DESC1);
        assertEquals(cur.getCount(), 0);

        resetDB();

    }

    @Rubric(
            value = "testDeleteNotInDB()",
            goal = "Test that delete behaves correctly if called on data not in db",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDeleteNotInDB() throws Exception {

        dataEntryHelper();

        assertEquals(mDB.deleteEntries(NOT_IN_DB, null, null, null), 0);

        resetDB();
    }

    @Rubric(
            value = "testDeleteTimeMulti()",
            goal = "Tests that both correct entries are deleted when they have same time" +
                    "and time is used to delete. Also that only correct entries are deleted",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDeleteTimeMulti() throws Exception {

        dataEntryHelper();

        mDB.storeLocationData(TIME2A, LAT2A, LONGI2A, DESC2A);

        assertEquals(mDB.deleteEntries(TIME2, null, null, null), 2);

        Cursor cur = mDB.queryEntries(TIME2, null, null, null);
        assertEquals(cur.getCount(), 0);

        cur = mDB.queryEntries(null, null, null, null);
        assertEquals(cur.getCount(), 1);

        resetDB();

    }

    @Rubric(
            value = "testDeleteNullTime()",
            goal = "Test that null works for time value",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDeleteNullTime() throws Exception {

        mDB.storeLocationData(TIME1, LAT1, LONGI1, DESC1);

        assertEquals(mDB.deleteEntries(null, LAT1, LONGI1, DESC1), 1);

        Cursor cur = mDB.queryEntries(TIME1, LAT1, LONGI1, DESC1);
        assertEquals(cur.getCount(), 0);

        resetDB();

    }

    @Rubric(
            value = "testDeleteNullLat()",
            goal = "Test that null works for lat value",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDeleteNullLat() throws Exception {

        mDB.storeLocationData(TIME1, LAT1, LONGI1, DESC1);

        assertEquals(mDB.deleteEntries(TIME1, null, LONGI1, DESC1), 1);

        Cursor cur = mDB.queryEntries(TIME1, LAT1, LONGI1, DESC1);
        assertEquals(cur.getCount(), 0);

        resetDB();

    }

    @Rubric(
            value = "testDeleteNullLong()",
            goal = "Test that null works for long value",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDeleteNullLong() throws Exception {

        mDB.storeLocationData(TIME1, LAT1, LONGI1, DESC1);

        assertEquals(mDB.deleteEntries(TIME1, LAT1, null, DESC1), 1);

        Cursor cur = mDB.queryEntries(TIME1, LAT1, LONGI1, DESC1);
        assertEquals(cur.getCount(), 0);

        resetDB();

    }

    @Rubric(
            value = "testDeleteNullDesc()",
            goal = "Test that null works for desc value",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDeleteNullDesc() throws Exception {

        mDB.storeLocationData(TIME1, LAT1, LONGI1, DESC1);

        assertEquals(mDB.deleteEntries(TIME1, LAT1, LONGI1, null), 1);

        Cursor cur = mDB.queryEntries(TIME1, LAT1, LONGI1, DESC1);
        assertEquals(cur.getCount(), 0);

        resetDB();

    }

    @Rubric(
            value = "testDeleteOnlyTime()",
            goal = "Test that time can be used by itself",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDeleteOnlyTime() throws Exception {

        mDB.storeLocationData(TIME1, LAT1, LONGI1, DESC1);

        assertEquals(mDB.deleteEntries(TIME1, null, null, null), 1);

        Cursor cur = mDB.queryEntries(TIME1, LAT1, LONGI1, DESC1);
        assertEquals(cur.getCount(), 0);

        resetDB();

    }

    @Rubric(
            value = "testDeleteOnlyLat()",
            goal = "Test that lat can be used by itself",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDeleteOnlyLat() throws Exception {

        mDB.storeLocationData(TIME1, LAT1, LONGI1, DESC1);

        assertEquals(mDB.deleteEntries(null, LAT1, null, null), 1);

        Cursor cur = mDB.queryEntries(TIME1, LAT1, LONGI1, DESC1);
        assertEquals(cur.getCount(), 0);

        resetDB();

    }

    @Rubric(
            value = "testDeleteOnlyLong()",
            goal = "Test that long can be used by itself",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDeleteOnlyLong() throws Exception {

        mDB.storeLocationData(TIME1, LAT1, LONGI1, DESC1);

        assertEquals(mDB.deleteEntries(null, null, LONGI1, null), 1);

        Cursor cur = mDB.queryEntries(TIME1, LAT1, LONGI1, DESC1);
        assertEquals(cur.getCount(), 0);

        resetDB();

    }

    @Rubric(
            value = "testDeleteOnlyDesc()",
            goal = "Test that desc can be used by itself",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDeleteOnlyDesc() throws Exception {

        mDB.storeLocationData(TIME1, LAT1, LONGI1, DESC1);

        assertEquals(mDB.deleteEntries(null, null, null, DESC1), 1);

        Cursor cur = mDB.queryEntries(TIME1, LAT1, LONGI1, DESC1);
        assertEquals(cur.getCount(), 0);

        resetDB();
    }

    @Rubric(
            value = "testDeleteAll()",
            goal = "Test that all entries can be deleted correctly",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testDeleteAll() throws Exception {

        mDB.storeLocationData(TIME1, LAT1, LONGI1, DESC1);
        mDB.storeLocationData(TIME2, LAT2, LONGI2, DESC2);

        Cursor cur = mDB.queryEntries(null, null, null, null);
        assertEquals(cur.getCount(), 2);

        mDB.deleteEntries(null, null, null, null);

        cur = mDB.queryEntries(null, null, null, null);
        assertEquals(cur.getCount(), 0);

        resetDB();

    }

//    @Rubric(
//            value = "testForInjection()",
//            goal = "To determine if database impl is injection safe",
//            points = 0.0,
//            reference = {
//            }
//    )
//    @Test
//    // todo get rid of this test for the assignment
//    public void testSQLInjection() throws Exception {
//
//        dataEntryHelper();
//
//        // TODO test in the service? Or DBManager (Assuming it is provided)
//        // TODO What kind of behavior should we expect given injection?
//
//        // Test to see if a delete injection is possible on insertion
//
////        mDB.storeLocationData(TIME1, LAT1, LONGI1, DESC1);
//
//
////        mDB.storeLocationData(TIME2, LAT2, LONGI2, DESC2 +
////                 "); DELETE FROM " + LocDBContract.FeedEntry.TABLE_NAME +
////                " WHERE " +LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_TIME +
////                "=" + TIME1 + ";");
//
//        Cursor cur = mDB.queryEntries(null, null, null, "'" + DESC1 + "' or desc!='hi'");
//        assertEquals(cur.getCount(), 1);
//    }

    @Rubric(
            value = "testUsingSQLiteDatabase()",
            goal = "Check that the manager is acutally using SQLite",
            points = 50.0,
            reference = {
            }
    )
    @Test
    public void testUsingSQLiteDatabase()throws Exception{
        SQLiteDatabase sqlDB = mDB.getSQLiteDB();

        dataEntryHelper();

        Cursor cur = sqlDB.query(
                true,
                LocDBContract.FeedEntry.TABLE_NAME,
                null,
                LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_DESCRIPTION + " = ?",
                new String[] {DESC1},
                null, null, null, null);

        assertEquals(cur.getCount(), 1);

        cur.moveToFirst();
        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_TIME)),
                TIME1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_LATITUDE)),
                LAT1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_lONGITUDE)),
                LONGI1);

        assertEquals(
                cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_DESCRIPTION)),
                DESC1);


        resetDB();

    }
}
