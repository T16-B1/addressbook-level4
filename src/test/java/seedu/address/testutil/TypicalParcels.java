package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.DATE_TODAY;
import static seedu.address.logic.commands.CommandTestUtil.DATE_TOMORROW;
import static seedu.address.logic.commands.CommandTestUtil.DATE_YESTERDAY;
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
import static seedu.address.logic.commands.CommandTestUtil.VALID_STATUS_DELIVERING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STATUS_OVERDUE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STATUS_PENDING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FLAMMABLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FROZEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HEAVY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TRACKING_NUMBER_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TRACKING_NUMBER_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELIVERY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRACKING_NUMBER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.model.parcel.exceptions.DuplicateParcelException;

/**
 * A utility class containing a list of {@code Parcel} objects to be used in tests.
 */
public class TypicalParcels {
    //@@author kennard123661
    // Parcels that are PENDING.
    public static final String VALID_TRACKING_NUMBER_ALICE = "RR000111000SG";
    public static final String VALID_NAME_ALICE = "Alice Pauline";
    public static final String VALID_PHONE_ALICE = "85355255";
    public static final String VALID_EMAIL_ALICE = "alice@example.com";
    public static final String VALID_ADDRESS_ALICE = "6, Jurong West Ave 1, #08-111 S649520";
    public static final String VALID_DELIVERY_DATE_ALICE = "05-05-2018";

    public static final String VALID_TRACKING_NUMBER_BENSON = "RR111000111SG";
    public static final String VALID_NAME_BENSON = "Benson Meier";
    public static final String VALID_PHONE_BENSON = "98765432";
    public static final String VALID_EMAIL_BENSON = "johnd@example.com";
    public static final String VALID_ADDRESS_BENSON = "336, Clementi Ave 2, #02-25 s120336";
    public static final String VALID_DELIVERY_DATE_BENSON = DATE_TOMORROW;
    public static final String TRACKING_NUMBER_DESC_BENSON = " " + PREFIX_TRACKING_NUMBER
            + VALID_TRACKING_NUMBER_BENSON;
    public static final String NAME_DESC_BENSON = " " + PREFIX_NAME + VALID_NAME_BENSON;
    public static final String PHONE_DESC_BENSON = " " + PREFIX_PHONE + VALID_PHONE_BENSON;
    public static final String EMAIL_DESC_BENSON = " " + PREFIX_EMAIL + VALID_EMAIL_BENSON;
    public static final String ADDRESS_DESC_BENSON = " " + PREFIX_ADDRESS + VALID_ADDRESS_BENSON;
    public static final String DELIVERY_DATE_DESC_BENSON = " " + PREFIX_DELIVERY_DATE + VALID_DELIVERY_DATE_BENSON;

    // Parcels that are OVERDUE
    public static final String VALID_TRACKING_NUMBER_CARL = "RR222000111SG";
    public static final String VALID_NAME_CARL = "Carl Kurz";
    public static final String VALID_PHONE_CARL = "95352563";
    public static final String VALID_EMAIL_CARL = "heinz@example.com";
    public static final String VALID_ADDRESS_CARL = "18 Marina Blvd, S018980";
    public static final String VALID_DELIVERY_DATE_CARL = DATE_YESTERDAY;

    public static final String VALID_TRACKING_NUMBER_DANIEL = "RR111321123SG";
    public static final String VALID_NAME_DANIEL = "Daniel Meier";
    public static final String VALID_PHONE_DANIEL = "87652533";
    public static final String VALID_EMAIL_DANIEL = "cornelia@example.com";
    public static final String VALID_ADDRESS_DANIEL = "59 Namly Garden S267387";
    public static final String VALID_DELIVERY_DATE_DANIEL = DATE_YESTERDAY;

    // parcels that are DELIVERING
    public static final String VALID_TRACKING_NUMBER_ELLE = "RR111321124SG";
    public static final String VALID_NAME_ELLE = "Elle Meyer";
    public static final String VALID_PHONE_ELLE = "9482224";
    public static final String VALID_EMAIL_ELLE = "werner@example.com";
    public static final String VALID_ADDRESS_ELLE = "2 Finlayson Green, S049247";
    public static final String VALID_DELIVERY_DATE_ELLE = DATE_TODAY;

    public static final String VALID_TRACKING_NUMBER_FIONA = "RR999123555SG";
    public static final String VALID_NAME_FIONA = "Fiona Kunz";
    public static final String VALID_PHONE_FIONA = "9482427";
    public static final String VALID_EMAIL_FIONA = "lydia@example.com";
    public static final String VALID_ADDRESS_FIONA = "48 Upper Dickson Rd S207502";
    public static final String VALID_DELIVERY_DATE_FIONA = DATE_TODAY;

    // Parcels that are COMPLETED
    public static final String VALID_TRACKING_NUMBER_GEORGE = "RR696969696SG";
    public static final String VALID_NAME_GEORGE = "George Best";
    public static final String VALID_PHONE_GEORGE = "9482442";
    public static final String VALID_EMAIL_GEORGE = "anna@example.com";
    public static final String VALID_ADDRESS_GEORGE = "Block 532 HDB Upper Cross Street s050532";
    public static final String VALID_DELIVERY_DATE_GEORGE = DATE_YESTERDAY;

    public static final String VALID_TRACKING_NUMBER_HOON = "RR121212124SG";
    public static final String VALID_NAME_HOON = "Hoon Meier";
    public static final String VALID_PHONE_HOON = "8482424";
    public static final String VALID_EMAIL_HOON = "stefan@example.com";
    public static final String VALID_ADDRESS_HOON = "522 Hougang Ave 6 s530522";
    public static final String VALID_DELIVERY_DATE_HOON = DATE_YESTERDAY;

    // Parcels to be manually added
    public static final String VALID_TRACKING_NUMBER_IDA = "RR111333888SG";
    public static final String VALID_NAME_IDA = "Ida Mueller";
    public static final String VALID_PHONE_IDA = "8482131";
    public static final String VALID_EMAIL_IDA = "hans@example.com";
    public static final String VALID_ADDRESS_IDA = "3 River Valley Rd, S179024";
    public static final String VALID_DELIVERY_DATE_IDA = DATE_TODAY;

    public static final String VALID_TRACKING_NUMBER_JOHN = "RR998877665SG";
    public static final String VALID_NAME_JOHN = "John Doe";
    public static final String VALID_PHONE_JOHN = "99999991";
    public static final String VALID_EMAIL_JOHN = "jd@example.com";
    public static final String VALID_ADDRESS_JOHN = "3 River Valley Rd, S179024";
    public static final String VALID_DELIVERY_DATE_JOHN = DATE_TOMORROW;

    public static final ReadOnlyParcel ALICE = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_ALICE)
            .withName(VALID_NAME_ALICE).withAddress(VALID_ADDRESS_ALICE)
            .withEmail(VALID_EMAIL_ALICE).withPhone(VALID_PHONE_ALICE).withDeliveryDate(VALID_DELIVERY_DATE_ALICE)
            .withStatus(VALID_STATUS_PENDING).withTags(VALID_TAG_FROZEN).build();
    public static final ReadOnlyParcel BENSON = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_BENSON)
            .withName(VALID_NAME_BENSON).withAddress(VALID_ADDRESS_BENSON).withEmail(VALID_EMAIL_BENSON)
            .withPhone(VALID_PHONE_BENSON).withDeliveryDate(VALID_DELIVERY_DATE_BENSON).withStatus(VALID_STATUS_PENDING)
            .withTags(VALID_TAG_FROZEN, VALID_TAG_FLAMMABLE).build();
    public static final ReadOnlyParcel CARL = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_CARL)
            .withName(VALID_NAME_CARL).withPhone(VALID_PHONE_CARL).withEmail(VALID_EMAIL_CARL)
            .withAddress(VALID_ADDRESS_CARL).withStatus(VALID_STATUS_DELIVERING)
            .withDeliveryDate(VALID_DELIVERY_DATE_CARL).build();
    public static final ReadOnlyParcel DANIEL = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_DANIEL)
            .withName(VALID_NAME_DANIEL).withPhone(VALID_PHONE_DANIEL).withEmail(VALID_EMAIL_DANIEL)
            .withAddress(VALID_ADDRESS_DANIEL).withStatus(VALID_STATUS_OVERDUE)
            .withDeliveryDate(VALID_DELIVERY_DATE_DANIEL).build();
    public static final ReadOnlyParcel ELLE = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_ELLE)
            .withName(VALID_NAME_ELLE).withPhone(VALID_PHONE_ELLE).withEmail(VALID_EMAIL_ELLE)
            .withAddress(VALID_ADDRESS_ELLE).withTags(VALID_TAG_HEAVY).withDeliveryDate(VALID_DELIVERY_DATE_ELLE)
            .build();
    public static final ReadOnlyParcel FIONA = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_FIONA)
            .withName(VALID_NAME_FIONA).withPhone(VALID_PHONE_FIONA).withEmail(VALID_EMAIL_FIONA)
            .withAddress(VALID_ADDRESS_FIONA).withDeliveryDate(VALID_DELIVERY_DATE_FIONA)
            .withStatus(VALID_STATUS_DELIVERING).build();
    public static final ReadOnlyParcel GEORGE = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_GEORGE)
            .withName(VALID_NAME_GEORGE).withPhone(VALID_PHONE_GEORGE).withEmail(VALID_EMAIL_GEORGE)
            .withAddress(VALID_ADDRESS_GEORGE).withDeliveryDate(VALID_DELIVERY_DATE_GEORGE)
            .withTags(VALID_TAG_HEAVY).withStatus(VALID_STATUS_COMPLETED).build();
    public static final ReadOnlyParcel HOON = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_HOON)
            .withName(VALID_NAME_HOON).withPhone(VALID_PHONE_HOON).withEmail(VALID_EMAIL_HOON)
            .withAddress(VALID_ADDRESS_HOON).withDeliveryDate(VALID_DELIVERY_DATE_HOON)
            .withStatus(VALID_STATUS_COMPLETED).build();

    // Manually added
    public static final ReadOnlyParcel IDA = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_IDA)
            .withName(VALID_NAME_IDA).withPhone(VALID_PHONE_IDA).withEmail(VALID_EMAIL_IDA)
            .withAddress(VALID_ADDRESS_IDA).withDeliveryDate(VALID_DELIVERY_DATE_IDA)
            .withStatus(VALID_STATUS_PENDING).build();
    public static final ReadOnlyParcel JOHN = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_JOHN)
            .withName(VALID_NAME_JOHN).withPhone(VALID_PHONE_JOHN).withEmail(VALID_EMAIL_JOHN)
            .withAddress(VALID_ADDRESS_JOHN).withDeliveryDate(VALID_DELIVERY_DATE_JOHN)
            .withTags(VALID_TAG_HEAVY, VALID_TAG_FLAMMABLE).withStatus(VALID_STATUS_COMPLETED).build();
    //@@author

    // Manually added - Parcel's details found in {@code CommandTestUtil}
    public static final ReadOnlyParcel AMY = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_AMY)
            .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
            .withAddress(VALID_ADDRESS_AMY).withDeliveryDate(VALID_DELIVERY_DATE_AMY)
            .withStatus(VALID_STATUS_DELIVERING).withTags(VALID_TAG_FLAMMABLE).build();
    public static final ReadOnlyParcel BOB = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_BOB)
            .withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
            .withAddress(VALID_ADDRESS_BOB).withDeliveryDate(VALID_DELIVERY_DATE_BOB)
            .withStatus(VALID_STATUS_COMPLETED).withTags(VALID_TAG_FROZEN, VALID_TAG_FLAMMABLE).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalParcels() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical parcels.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (ReadOnlyParcel parcel : getTypicalParcels()) {
            try {
                ab.addParcel(parcel);
            } catch (DuplicateParcelException e) {
                assert false : "not possible";
            }
        }
        return ab;
    }

    public static List<ReadOnlyParcel> getTypicalParcels() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE, HOON));
    }
}
