package extensibleapps.vandy.mooc.locationtracker;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import junit.framework.Assert;

import org.junit.runner.RunWith;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.content.Context.LOCATION_SERVICE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import edu.vanderbilt.grader.rubric.Rubric;

@RunWith(AndroidJUnit4.class)

public class LocLogServiceTest {

    private static final String LOCATION_UNKNOWN = "No known last location";

    private String TEST_DESC_1 = "test1";
    private String TEST_DESC_2 = "test2";
    private String TEST_DESC_3 = "test3";

    @Rule
    public final ServiceTestRule mServiceRule = ServiceTestRule.withTimeout(60L, TimeUnit.SECONDS);

    private LocLogDBManager mDB;

    private LocationManager mLocationManager;

    @Before
    public void setUp() throws Exception {

        mDB =  new LocLogDBManager(InstrumentationRegistry.getTargetContext());

        resetDB();
    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)InstrumentationRegistry.getTargetContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {

            Location loc;

            try{
                loc = mLocationManager.getLastKnownLocation(provider);
            } catch (SecurityException e) {
                loc = null;
            }

            if (loc == null) {
                continue;
            }
            if (bestLocation == null || loc.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = loc;
            }
        }
        return bestLocation;
    }

    private void resetDB() {
        int del = mDB.deleteEntries(null, null, null, null);
    }

    private void sendToService(String desc) throws TimeoutException{
        Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(),LocationLogService.class);
        serviceIntent.putExtra(LocationLogService.DESCRIPTION_KEY, desc);

        mServiceRule.startService(serviceIntent);

        // wait to make sure service has time to store info
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }



    @Rubric(
            value = "testServiceStarts()",
            goal = "Determine if Service Starts as expected.",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testServiceStarts() throws Exception {

        Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(),
                LocationLogService.class);

        serviceIntent.putExtra(LocationLogService.DESCRIPTION_KEY, "");

        assertNotNull(InstrumentationRegistry.getTargetContext().startService(serviceIntent));

        resetDB();
    }


    @Rubric(
            value = "testCountSingle()",
            goal = "Test if Service stores a single location object in the database when requested.",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testCountSingle() throws TimeoutException {

        sendToService(TEST_DESC_1);

        Cursor cur = mDB.queryEntries(null, null, null, TEST_DESC_1);

        Assert.assertEquals(cur.getCount(), 1);

        resetDB();

    }

    @Rubric(
            value = "testMultipleSameDesc()",
            goal = "Test if Service stores multiple location objects with the same data correctly.",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testMultipleSameDesc() throws TimeoutException {

        sendToService(TEST_DESC_1);
        sendToService(TEST_DESC_1);
        sendToService(TEST_DESC_1);

        // Test that they are all stored
        Cursor cur = mDB.queryEntries(null, null, null, null);
        Assert.assertEquals(cur.getCount(), 3);

        // Test that they all have the same description
        cur = mDB.queryEntries(null, null, null, TEST_DESC_1);
        Assert.assertEquals(cur.getCount(), 3);

        while(cur.moveToNext()){
            Assert.assertEquals(cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_DESCRIPTION)),
                    TEST_DESC_1);
        }

        resetDB();
    }

    @Rubric(
            value = "testMultipleDifferentDesc()",
            goal = "Test if Service stores multiple location objects with different data correctly.",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testMultipleDifferentDesc() throws TimeoutException {

        List<String> descriptions = new ArrayList<>();
        descriptions.add(TEST_DESC_1);
        descriptions.add(TEST_DESC_2);
        descriptions.add(TEST_DESC_3);

        for (String desc : descriptions){
            sendToService(desc);
        }

        // Test that they are all stored
        Cursor cur = mDB.queryEntries(null, null, null, null);
        Assert.assertEquals(cur.getCount(), 3);

        // Check that they are all individually stored correctly
        for (String desc : descriptions){

            cur.moveToNext();
            Assert.assertEquals(cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_DESCRIPTION)),
                    desc);
        }

        resetDB();
    }

    @Rubric(
            value = "testCorrectData()",
            goal = "Test if Service stores the correct description for an entry.",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testCorrectDesc() throws TimeoutException{

        sendToService(TEST_DESC_1);

        Cursor cur = mDB.queryEntries(null, null, null, TEST_DESC_1);
        Assert.assertEquals(cur.getCount(), 1);
        cur.moveToFirst();

        Assert.assertEquals(cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_DESCRIPTION)),
                TEST_DESC_1);

        resetDB();
    }

    @Rubric(
            value = "testCorrectTime()",
            goal = "Test if Service stores the correct time for an entry.",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testCorrectTime()throws TimeoutException{

        double currTime = System.currentTimeMillis();

        sendToService(TEST_DESC_1);

        Cursor cur = mDB.queryEntries(null, null, null, TEST_DESC_1);
        cur.moveToFirst();

        double storedTime = Long.valueOf(cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_TIME)));
        double timeDiff = Math.abs(currTime - storedTime);

        Assert.assertTrue(timeDiff <= 10000); // 10 seconds of lee-way

        resetDB();
    }

    @Rubric(
            value = "testCorrectLoc()",
            goal = "Test if Service stores the correct location for an entry.",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testCorrectLoc() throws TimeoutException{

        sendToService(TEST_DESC_1);

        Cursor cur = mDB.queryEntries(null, null, null, TEST_DESC_1);
        cur.moveToFirst();

        LocationManager locationManager =
                (LocationManager) InstrumentationRegistry.getTargetContext()
                        .getSystemService(LOCATION_SERVICE);

        String lat = null;
        String lon = null;

        Location location = getLastKnownLocation();
        if (location != null) {
            lat = String.valueOf(location.getLatitude());
            lon = String.valueOf(location.getLongitude());
        } else {
            lat = LOCATION_UNKNOWN;
            lon = LOCATION_UNKNOWN;
        }

        assertEquals(lat, cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_LATITUDE)));
        assertEquals(lon, cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_lONGITUDE)));

        resetDB();
    }

    @Rubric(
            value = "testTimeDelay()",
            goal = "Test that service stores different times for sequential entries.",
            points = 10.0,
            reference = {
            }
    )
    @Test
    public void testTimeDelay() throws TimeoutException {

        sendToService(TEST_DESC_1);
        try
        {
            Thread.sleep(5000);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
        sendToService(TEST_DESC_1);

        Cursor cur = mDB.queryEntries(null, null, null, TEST_DESC_1);

        double pastTime = 0;

        while(cur.moveToNext()){

            if (pastTime != 0){
                pastTime = Long.valueOf(cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_TIME)));
            } else {

                double curTime = Long.valueOf(cur.getString(cur.getColumnIndex(LocDBContract.FeedEntry.COLUMN_NAME_ENTRY_TIME)));

                assertTrue(Math.abs(pastTime - curTime) >= 4999); // greater than or equal to the sleep time
            }

        }

        resetDB();
    }


}