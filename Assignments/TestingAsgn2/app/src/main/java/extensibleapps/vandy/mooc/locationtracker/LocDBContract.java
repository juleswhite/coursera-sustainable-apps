package extensibleapps.vandy.mooc.locationtracker;

import android.provider.BaseColumns;


/**
 * Contract containing labels that are used to access the table and
 * its entries of the location storage database.
 */
public final class LocDBContract {
    public LocDBContract(){}

    // Define table contents
    public static abstract class FeedEntry implements BaseColumns {

        /**
         * Table where location entries are stored
         */
        public static final String TABLE_NAME = "entry";

        /**
         * Time that location was logged
         */
        public static final String COLUMN_NAME_ENTRY_TIME = "time";

        /**
         * Latitude that was logged
         */
        public static final String COLUMN_NAME_ENTRY_LATITUDE = "lat";

        /**
         * Longitude that was logged
         */
        public static final String COLUMN_NAME_ENTRY_lONGITUDE = "long";

        /**
         * Description of the Location
         */
        public static final String COLUMN_NAME_ENTRY_DESCRIPTION = "desc";

    }
}
