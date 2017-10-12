package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}
 */
public class Address {

    public static final String MESSAGE_ADDRESS_CONSTRAINTS =
            "Person addresses can take any values, cannot be blank and must end with 'S' or 's' appended "
                    + "with 6 digits seperated from the rest of the address by one or multiple space(s)";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String ADDRESS_VALIDATION_REGEX = "[\\w].+\\s+([Ss]{1}\\d{6})$";
    public static final int POSTAL_CODE_STRING_LENGTH = 7;

    public final String value;
    public final PostalCode postalCode;

    /**
     * Validates given address.
     *
     * @throws IllegalValueException if given address string is invalid.
     */
    public Address(String address) throws IllegalValueException {
        requireNonNull(address);
        if (!isValidAddress(address)) {
            throw new IllegalValueException(MESSAGE_ADDRESS_CONSTRAINTS);
        }

        this.value = address.substring(0, address.length() - POSTAL_CODE_STRING_LENGTH).trim();
        this.postalCode = new PostalCode(address.substring(address.length() - POSTAL_CODE_STRING_LENGTH,
                address.length()));
    }

    /**
     * Returns true if a given string is a valid person address.
     */
    public static boolean isValidAddress(String test) {
        return test.matches(ADDRESS_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.value, this.postalCode);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Address // instanceof handles nulls
                && this.toString().equals(((Address) other).toString())); // state check
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

}
