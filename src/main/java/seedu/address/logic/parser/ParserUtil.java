package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.parcel.Address;
import seedu.address.model.parcel.DeliveryDate;
import seedu.address.model.parcel.Email;
import seedu.address.model.parcel.Name;
import seedu.address.model.parcel.Phone;
import seedu.address.model.parcel.TrackingNumber;
import seedu.address.model.tag.Tag;
import seedu.address.storage.XmlAddressBookStorage;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 * {@code ParserUtil} contains methods that take in {@code Optional} as parameters. However, it goes against Java's
 * convention (see https://stackoverflow.com/a/39005452) as {@code Optional} should only be used a return type.
 * Justification: The methods in concern receive {@code Optional} return values from other methods as parameters and
 * return {@code Optional} values based on whether the parameters were present. Therefore, it is redundant to unwrap the
 * initial {@code Optional} before passing to {@code ParserUtil} as a parameter and then re-wrap it into an
 * {@code Optional} return value inside {@code ParserUtil} methods.
 */
public class ParserUtil {

    public static final String MESSAGE_FILE_NOT_FOUND = "File to import not found.";
    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INVALID_DATA = "Xml file to import at %1$s is not in the correct format.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws IllegalValueException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws IllegalValueException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new IllegalValueException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses {@code filePath} and retrieve an {@code ReadOnlyAddressBook} from the filepath and returns it.
     * Leading and trailing whitespaces will be trimmed.
     * @throws IllegalValueException if the specified file cannot be formed or it is not the proper addressbook xml
     * format.
     */
    public static ReadOnlyAddressBook parseImportFilePath(String filePath) throws IllegalValueException {
        requireNonNull(filePath);
        String trimmedFilePath = filePath.trim();

        try {
            Optional<ReadOnlyAddressBook> addressBookOptional = new XmlAddressBookStorage(trimmedFilePath)
                    .readAddressBook();

            if (addressBookOptional.isPresent()) {
                return addressBookOptional.get();
            } else {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            throw new IllegalValueException(MESSAGE_FILE_NOT_FOUND);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(MESSAGE_INVALID_DATA, filePath));
        }
    }

    /**
     * Parses a {@code Optional<String> trackingNumber} into an {@code Optional<TrackingNumber>} if
     * {@code trackingNumber} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<TrackingNumber> parseTrackingNumber(Optional<String> trackingNumber)
            throws IllegalValueException {
        requireNonNull(trackingNumber);
        return trackingNumber.isPresent() ? Optional.of(new TrackingNumber(trackingNumber.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Name>} if {@code name} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Name> parseName(Optional<String> name) throws IllegalValueException {
        requireNonNull(name);
        return name.isPresent() ? Optional.of(new Name(name.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> phone} into an {@code Optional<Phone>} if {@code phone} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Phone> parsePhone(Optional<String> phone) throws IllegalValueException {
        requireNonNull(phone);
        return phone.isPresent() ? Optional.of(new Phone(phone.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> address} into an {@code Optional<Address>} if {@code address} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Address> parseAddress(Optional<String> address) throws IllegalValueException {
        requireNonNull(address);
        return address.isPresent() ? Optional.of(new Address(address.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> email} into an {@code Optional<Email>} if {@code email} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Email> parseEmail(Optional<String> email) throws IllegalValueException {
        requireNonNull(email);
        return email.isPresent() ? Optional.of(new Email(email.get())) : Optional.empty();
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws IllegalValueException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses {@code deliveryDate} into an {@code deliveryDate} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws IllegalValueException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Optional<DeliveryDate> parseDeliveryDate(Optional<String> deliveryDate) throws IllegalValueException {
        requireNonNull(deliveryDate);
        return deliveryDate.isPresent() ? Optional.of(new DeliveryDate(deliveryDate.get())) : Optional.empty();
    }

}
