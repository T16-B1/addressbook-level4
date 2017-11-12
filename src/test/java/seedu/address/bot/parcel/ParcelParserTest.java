package seedu.address.bot.parcel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.DELIVERY_DATE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DELIVERY_DATE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DELIVERY_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_STATUS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TRACKING_NUMBER_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.STATUS_DESC_COMPLETED;
import static seedu.address.logic.commands.CommandTestUtil.STATUS_DESC_DELIVERING;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FLAMMABLE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FROZEN;
import static seedu.address.logic.commands.CommandTestUtil.TRACKING_NUMBER_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TRACKING_NUMBER_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DELIVERY_DATE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DELIVERY_DATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STATUS_COMPLETED;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FLAMMABLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FROZEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TRACKING_NUMBER_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TRACKING_NUMBER_BOB;

import org.junit.Test;

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
import seedu.address.testutil.ParcelBuilder;

public class ParcelParserTest {
    private ParcelParser parser = new ParcelParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Parcel expectedParcel = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_BOB)
                .withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withDeliveryDate(VALID_DELIVERY_DATE_BOB)
                .withStatus(VALID_STATUS_COMPLETED).withTags(VALID_TAG_FLAMMABLE).build();

        // multiple tracking number - last tracking number accepted
        assertParseSuccess(parser, " " + TRACKING_NUMBER_DESC_AMY
                + TRACKING_NUMBER_DESC_BOB + NAME_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_BOB + STATUS_DESC_COMPLETED + TAG_DESC_FLAMMABLE,
                expectedParcel);

        // multiple names - last name accepted
        assertParseSuccess(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_AMY
                + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_BOB
                + STATUS_DESC_COMPLETED + TAG_DESC_FLAMMABLE,
                expectedParcel);

        // multiple phones - last phone accepted
        assertParseSuccess(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_BOB
                + PHONE_DESC_AMY + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + STATUS_DESC_COMPLETED
                + DELIVERY_DATE_DESC_BOB + TAG_DESC_FLAMMABLE,
                expectedParcel);

        // multiple emails - last email accepted
        assertParseSuccess(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_AMY + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_BOB
                + STATUS_DESC_COMPLETED + TAG_DESC_FLAMMABLE,
                expectedParcel);

        // multiple addresses - last address accepted
        assertParseSuccess(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_AMY + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_BOB
                + STATUS_DESC_COMPLETED + TAG_DESC_FLAMMABLE,
                expectedParcel);

        // multiple delivery dates - last delivery date accepted
        assertParseSuccess(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_BOB
               + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_AMY
               + DELIVERY_DATE_DESC_BOB + STATUS_DESC_COMPLETED + TAG_DESC_FLAMMABLE,
               expectedParcel);

        // multiple status - last status accepted
        assertParseSuccess(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_BOB
               + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_BOB
               + STATUS_DESC_DELIVERING + STATUS_DESC_COMPLETED + TAG_DESC_FLAMMABLE,
               expectedParcel);

        // multiple tags - all accepted
        Parcel expectedParcelMultipleTags = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_BOB)
                .withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withDeliveryDate(VALID_DELIVERY_DATE_BOB)
                .withTags(VALID_TAG_FLAMMABLE, VALID_TAG_FROZEN).build();
        assertParseSuccess(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_BOB + TAG_DESC_FROZEN
                + TAG_DESC_FLAMMABLE,
                expectedParcelMultipleTags);
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags and no status
        Parcel expectedParcel = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_AMY)
                .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withDeliveryDate(VALID_DELIVERY_DATE_AMY).withStatus("PENDING")
                .withTags().build();
        assertParseSuccess(parser, " " + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY,
                expectedParcel);

        // no phone number
        Parcel expectedParcelDefaultPhone = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_BOB)
                .withName(VALID_NAME_BOB).withPhone(Phone.PHONE_DEFAULT_VALUE).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withDeliveryDate(VALID_DELIVERY_DATE_BOB)
                .withStatus(VALID_STATUS_COMPLETED).withTags(VALID_TAG_FLAMMABLE).build();
        assertParseSuccess(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_BOB
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_BOB
                + STATUS_DESC_COMPLETED + TAG_DESC_FLAMMABLE,
                expectedParcelDefaultPhone);

        // no email
        Parcel expectedParcelDefaultEmail = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_BOB)
                .withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).withEmail(Email.EMAIL_DEFAULT_VALUE)
                .withAddress(VALID_ADDRESS_BOB).withDeliveryDate(VALID_DELIVERY_DATE_BOB)
                .withStatus(VALID_STATUS_COMPLETED).withTags(VALID_TAG_FLAMMABLE).build();
        assertParseSuccess(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_BOB
                + PHONE_DESC_BOB + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_BOB
                + STATUS_DESC_COMPLETED + TAG_DESC_FLAMMABLE,
                expectedParcelDefaultEmail);


    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = ParcelParser.PARCEL_PARSER_ERROR;

        // missing tracking number prefix
        assertParseFailure(parser, " " + VALID_TRACKING_NUMBER_BOB + NAME_DESC_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_BOB, expectedMessage);

        // missing name prefix
        assertParseFailure(parser, " " + TRACKING_NUMBER_DESC_BOB + VALID_NAME_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_BOB, expectedMessage);

        // missing address prefix
        assertParseFailure(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB + DELIVERY_DATE_DESC_BOB, expectedMessage);

        // missing delivery date prefix
        assertParseFailure(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + VALID_DELIVERY_DATE_BOB, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, " " + TRACKING_NUMBER_DESC_BOB + VALID_NAME_BOB
                + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB + VALID_DELIVERY_DATE_BOB, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid tracking number
        assertParseFailure(parser, " " + INVALID_TRACKING_NUMBER_DESC + INVALID_NAME_DESC
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_BOB + STATUS_DESC_COMPLETED
                + TAG_DESC_FROZEN + TAG_DESC_FLAMMABLE, TrackingNumber.MESSAGE_TRACKING_NUMBER_CONSTRAINTS);

        // invalid name
        assertParseFailure(parser, " " + TRACKING_NUMBER_DESC_BOB + INVALID_NAME_DESC
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_BOB + STATUS_DESC_COMPLETED
                + TAG_DESC_FROZEN + TAG_DESC_FLAMMABLE, Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_BOB
                + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_BOB
                + STATUS_DESC_COMPLETED + TAG_DESC_FROZEN + TAG_DESC_FLAMMABLE, Phone.MESSAGE_PHONE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_BOB
                + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_BOB
                + TAG_DESC_FROZEN + TAG_DESC_FLAMMABLE, Email.MESSAGE_EMAIL_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC + DELIVERY_DATE_DESC_BOB
                + STATUS_DESC_COMPLETED + TAG_DESC_FROZEN + TAG_DESC_FLAMMABLE, Address.MESSAGE_ADDRESS_CONSTRAINTS);

        // invalid delivery date
        assertParseFailure(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + INVALID_DELIVERY_DATE_DESC
                + STATUS_DESC_COMPLETED + TAG_DESC_FROZEN + TAG_DESC_FLAMMABLE,
                DeliveryDate.MESSAGE_DELIVERY_DATE_CONSTRAINTS);

        // invalid status
        assertParseFailure(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_BOB + INVALID_STATUS_DESC
                + TAG_DESC_FROZEN + TAG_DESC_FLAMMABLE, Status.MESSAGE_STATUS_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, " " + TRACKING_NUMBER_DESC_BOB + NAME_DESC_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_BOB
                + INVALID_TAG_DESC + VALID_TAG_FLAMMABLE, Tag.MESSAGE_TAG_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, " " + TRACKING_NUMBER_DESC_BOB + INVALID_NAME_DESC
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC + INVALID_DELIVERY_DATE_DESC,
                Name.MESSAGE_NAME_CONSTRAINTS);
    }

    /**
     * Asserts that the parsing of {@code userInput} by {@code parser} is successful and the command created
     * equals to {@code expectedCommand}.
     */
    public static void assertParseSuccess(ParcelParser parser, String userInput, ReadOnlyParcel expectedParcel) {
        try {
            ReadOnlyParcel parsedParcel = parser.parse(userInput);
            assertEquals(parsedParcel, expectedParcel);
        } catch (ParseException pe) {
            throw new IllegalArgumentException("Invalid userInput.", pe);
        }
    }

    /**
     * Asserts that the parsing of {@code userInput} by {@code parser} is unsuccessful and the error message
     * equals to {@code expectedMessage}.
     */
    public static void assertParseFailure(ParcelParser parser, String userInput, String expectedMessage) {
        try {
            parser.parse(userInput);
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(expectedMessage, pe.getMessage());
        }
    }
}
