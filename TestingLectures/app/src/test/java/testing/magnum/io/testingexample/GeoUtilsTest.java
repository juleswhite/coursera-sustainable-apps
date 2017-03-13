package testing.magnum.io.testingexample;


import android.location.Address;
import android.location.Geocoder;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class GeoUtilsTest {

    @Mock
    private Geocoder geocoder;

    @Mock
    private Address addressForNashville;

    private GeoUtils geoUtils;

    @Before
    public void setUp(){
        geoUtils = new GeoUtils(geocoder);
    }

    @Test
    public void coordinatesWithNoZipCodeReturnNull() throws Exception {
        // This latitude, longitude is in the middle of the ocean and
        // won't have an address
        String zipCode = geoUtils.getCurrentZipCode(0, 0);
        assertNull(zipCode);
    }

    @Test
    public void nashvilleLatLngReturns37212() throws Exception {

        // Whenever we ask this address for the postal code, it is going to return 37212
        when(addressForNashville.getPostalCode()).thenReturn("37212");

        // Whenever we ask this geocoder for the addresses associated with the Nashville
        // latitute and longitude, we want to return the Nashville mock address
        when(geocoder.getFromLocation(
                    anyDouble(),
                    3.09,
                    anyInt()
                )
            ).thenReturn(Arrays.asList( addressForNashville ));

        String zipCode = geoUtils.getCurrentZipCode(36.139017, -86.796924);
        String zipCode2 = geoUtils.getCurrentZipCode(37.139017, -87.796924);
        assertEquals("37212", zipCode);
        assertEquals("37212", zipCode2);
    }
}
