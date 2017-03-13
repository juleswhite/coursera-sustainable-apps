package testing.magnum.io.testingexample;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * An incomplete set of tests for the LoginUtils class. What would you add
 * to build a more comprehensive set of tests?
 */
public class LoginUtilsTest {

    private LoginUtils utils;

    @Before
    public void setUp(){
        utils = new LoginUtils();
    }

    @Test
    public void aValidEmailAddressPasses() throws Exception {
        assertTrue(utils.isValidEmailAddress("foo@bar.com"));
    }

    @Test
    public void anInvalidEmailAddressFails() throws Exception {
        assertFalse(utils.isValidEmailAddress("invalid"));
    }

    @Test
    public void localPartLengthForValidEmailAddress() throws Exception {
        assertEquals(1, utils.getLocalPartLength("a@b.com"));
    }
}