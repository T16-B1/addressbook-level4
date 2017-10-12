package seedu.address.model.person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;

public class AddressTest {

    @Test
    public void isValidAddress() {
        // invalid addresses
        assertFalse(Address.isValidAddress("")); // empty string
        assertFalse(Address.isValidAddress(" ")); // spaces only
        assertFalse(Address.isValidAddress("Blk 456, Den Road, #01-355")); // does not end with 6 digits
        assertFalse(Address.isValidAddress("Blk 456, Den Road, #01-355 345")); // ends with shorter than 6 digits
        assertFalse(Address.isValidAddress("Blk 456, Den Road, #01-355 345123")); // no S prepending 6 digits
        assertFalse(Address.isValidAddress("- S435342")); // one character

        // valid addresses
        assertTrue(Address.isValidAddress("Blk 456, Den Road, #01-355 S345123")); // upper case s
        assertTrue(Address.isValidAddress("12- S435342"));
        assertTrue(Address.isValidAddress("Blk 456, Den Road, #01-355  s345123")); // lower case s
        assertTrue(Address.isValidAddress("Leng Inc; 1234 Market St; USA s123123")); // long address
    }

    @Test
    public void addressBookParsing() throws IllegalValueException {
        String testAddressString = "Blk 456, Den Road, #01-355         S345123";
        String testAddressWithLowerCasePostalCodeString = "Blk 456, Den Road, #01-355         s345123";
        String expectedAddressString = "Blk 456, Den Road, #01-355 S345123";

        Address address = new Address(testAddressString);
        assertEquals(address.toString(), expectedAddressString);
        Address addressWithLowerCasePostalCode = new Address(testAddressWithLowerCasePostalCodeString);
        assertEquals(addressWithLowerCasePostalCode.toString(), expectedAddressString);
        assertEquals(addressWithLowerCasePostalCode.toString(), address.toString());
    }

    @Test
    public void equals() throws IllegalValueException {
        String testAddressString = "Blk 456, Den Road, #01-355         S345123";
        String testAddressWithLowerCasePostalCodeString = "Blk 456, Den Road, #01-355         s345123";
        String expectedAddressString = "Blk 456, Den Road, #01-355 S345123";

        // instantiation
        Address address = new Address(testAddressString);
        Address addressWithLowerCasePostalCode = new Address(testAddressWithLowerCasePostalCodeString);
        Address expectedAddress = new Address(expectedAddressString);

        // equality check
        assertEquals(address, expectedAddress);
        assertEquals(address, addressWithLowerCasePostalCode);
        assertEquals(addressWithLowerCasePostalCode, expectedAddress);

        // hash code equality check
        assertEquals(address.hashCode(), expectedAddress.hashCode());
        assertEquals(address.hashCode(), addressWithLowerCasePostalCode.hashCode());
        assertEquals(addressWithLowerCasePostalCode.hashCode(), expectedAddress.hashCode());
    }
}
