package seedu.address.bot.parcel;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELIVERY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRACKING_NUMBER;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.parcel.Address;
import seedu.address.model.parcel.DeliveryDate;
import seedu.address.model.parcel.Email;
import seedu.address.model.parcel.Name;
import seedu.address.model.parcel.Parcel;
import seedu.address.model.parcel.Phone;
import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.model.parcel.Status;
import seedu.address.model.parcel.TrackingNumber;
import seedu.address.model.tag.Tag;

/**
 * Parser to parse details of a parcel.
 */
public class ParcelParser {

    public static final String PARCEL_PARSER_ERROR = "Invalid parcel format!";

    /**
     * Parse ia a method to parse a String containing the details of a parcel into a ReadOnlyParcel
     * @param args represent the details of the parcels with the prefixes.
     * @return a ReadOnlyParcel that has the details corresponding to the arguments
     * @throws ParseException if we are unable to understand the parcel information
     */
    public ReadOnlyParcel parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(" " + args, PREFIX_TRACKING_NUMBER, PREFIX_NAME, PREFIX_PHONE,
                        PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_DELIVERY_DATE, PREFIX_STATUS, PREFIX_TAG);
        if (!arePrefixesPresent(argMultimap, PREFIX_TRACKING_NUMBER, PREFIX_NAME, PREFIX_ADDRESS,
                PREFIX_DELIVERY_DATE)) {
            throw new ParseException(PARCEL_PARSER_ERROR);
        }

        try {
            TrackingNumber trackingNumber = ParserUtil.parseTrackingNumber(argMultimap
                    .getValue(PREFIX_TRACKING_NUMBER)).get();
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).get();
            Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).get();
            DeliveryDate deliveryDate = ParserUtil.parseDeliveryDate(argMultimap.getValue(PREFIX_DELIVERY_DATE)).get();
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            Optional<Email> emailOptional = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL));
            Optional<Status> statusOptional = ParserUtil.parseStatus(argMultimap.getValue(PREFIX_STATUS));
            Optional<Phone> phoneOptional = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE));

            Email email;
            Status status;
            Phone phone;

            if (emailOptional.isPresent()) {
                email = emailOptional.get();
            } else {
                email = new Email();
            }

            if (statusOptional.isPresent()) {
                status = statusOptional.get();
            } else {
                status = Status.getInstance("Pending");
            }

            if (phoneOptional.isPresent()) {
                phone = phoneOptional.get();
            } else {
                phone = new Phone();
            }

            ReadOnlyParcel parcel = new Parcel(trackingNumber, name, phone, email, address, deliveryDate, status,
                    tagList);

            return parcel;
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
