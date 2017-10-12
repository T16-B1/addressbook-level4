package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PostalCodeTest {

    @Test
    public void isValidPostalCode() {

        // invalid postal code
        assertFalse(PostalCode.isValidPostalCode("")); // empty string
        assertFalse(PostalCode.isValidPostalCode(" ")); // spaces only
        assertFalse(PostalCode.isValidPostalCode("000000")); // no 's' present
        assertFalse(PostalCode.isValidPostalCode("ss000000")); // prepended by two 's'
        assertFalse(PostalCode.isValidPostalCode("s00000")); // shorter than 6 digits
        assertFalse(PostalCode.isValidPostalCode("s0000000")); // longer than 6 digits

        // valid postal code
        assertTrue(PostalCode.isValidPostalCode("s000000"));
        assertTrue(PostalCode.isValidPostalCode("S123456"));
        assertTrue(PostalCode.isValidPostalCode("s000845"));
    }
}
