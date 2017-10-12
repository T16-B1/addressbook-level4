package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Address' postal code in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPostalCode(String)}
 */
public class PostalCode {

    public static final String MESSAGE_POSTAL_CODE_CONSTRAINTS =
            "Person's postal code should start with 'S' or 's' appended with 6 digits";

    /*
     * Postal Code should contain 's' or 'S' appended with  6 digits, and it should not be blank
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String POSTAL_CODE_VALIDATION_REGEX = "^[sS]{1}[0-9]{6}$";

    public final String value;

    /**
     * Validates given postal code.
     *
     * @throws IllegalValueException if given postalCode string is invalid.
     */
    public PostalCode(String postalCode) throws IllegalValueException {
        requireNonNull(postalCode);
        String trimmedPostalCode = postalCode.trim();
        if (!isValidPostalCode(trimmedPostalCode)) {
            throw new IllegalValueException(MESSAGE_POSTAL_CODE_CONSTRAINTS);
        }
        this.value = trimmedPostalCode.toUpperCase();
    }

    /**
     * Returns true if a given string is a valid person name.
     */
    public static boolean isValidPostalCode(String test) {
        return test.matches(POSTAL_CODE_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PostalCode // instanceof handles nulls
                && this.value.equals(((PostalCode) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
