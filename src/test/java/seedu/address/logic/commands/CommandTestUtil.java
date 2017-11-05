package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELIVERY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRACKING_NUMBER;
import static seedu.address.model.ModelManager.getDeliveredPredicate;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.parcel.NameContainsKeywordsPredicate;
import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.model.parcel.exceptions.ParcelNotFoundException;
import seedu.address.testutil.EditParcelDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_TRACKING_NUMBER_AMY = "RR231476598SG";
    public static final String VALID_TRACKING_NUMBER_BOB = "RR123456897SG";
    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1 S123123";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3 S456456";
    public static final String VALID_DELIVERY_DATE_AMY = "01-01-2001";
    public static final String VALID_DELIVERY_DATE_BOB = "02-02-1990";
    public static final String VALID_DELIVERY_DATE_AMY_FULLSTOPS = "1.01.2001";
    public static final String VALID_DELIVERY_DATE_AMY_SLASHES = "01/1/2001";
    public static final String TRACKING_NUMBER_DESC_AMY = " " + PREFIX_TRACKING_NUMBER + VALID_TRACKING_NUMBER_AMY;
    public static final String TRACKING_NUMBER_DESC_BOB = " " + PREFIX_TRACKING_NUMBER + VALID_TRACKING_NUMBER_BOB;
    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String DELIVERY_DATE_DESC_AMY = " " + PREFIX_DELIVERY_DATE + VALID_DELIVERY_DATE_AMY;
    public static final String DELIVERY_DATE_DESC_BOB = " " + PREFIX_DELIVERY_DATE + VALID_DELIVERY_DATE_BOB;

    //@@author kennard123661
    // list of status
    public static final String VALID_STATUS_PENDING = "PENDING";
    public static final String VALID_STATUS_DELIVERING = "DELIVERING";
    public static final String VALID_STATUS_OVERDUE = "OVERDUE";
    public static final String VALID_STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_DESC_PENDING = " " + PREFIX_STATUS + VALID_STATUS_PENDING;
    public static final String STATUS_DESC_DELIVERING = " " + PREFIX_STATUS + VALID_STATUS_DELIVERING;
    public static final String STATUS_DESC_COMPLETED = " " + PREFIX_STATUS + VALID_STATUS_COMPLETED;

    // list of tags
    public static final String VALID_TAG_FROZEN = "FROZEN";
    public static final String VALID_TAG_FRAGILE = "FRAGILE";
    public static final String VALID_TAG_FLAMMABLE = "FLAMMABLE";
    public static final String VALID_TAG_HEAVY = "HEAVY";
    public static final String TAG_DESC_FLAMMABLE = " " + PREFIX_TAG + VALID_TAG_FLAMMABLE;
    public static final String TAG_DESC_FROZEN = " " + PREFIX_TAG + VALID_TAG_FROZEN;
    //@@author

    public static final String INVALID_TRACKING_NUMBER_DESC = " " + PREFIX_TRACKING_NUMBER
            + "SS123456789RR"; // prefix and postfix are reversed
    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_DELIVERY_DATE_DESC = " "
            + PREFIX_DELIVERY_DATE + "05-13-2010"; // No 13th month allowed
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags
    public static final String INVALID_STATUS_DESC = " " + PREFIX_STATUS + "happy";

    public static final EditCommand.EditParcelDescriptor DESC_AMY;
    public static final EditCommand.EditParcelDescriptor DESC_BOB;

    // list of Delivery Dates
    public static final DateTimeFormatter DATE_BASIC_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final String DATE_TODAY = LocalDate.now().format(DATE_BASIC_FORMATTER);
    public static final String DATE_YESTERDAY = LocalDate.now().minus(Period.ofDays(1)).format(DATE_BASIC_FORMATTER);
    public static final String DATE_TOMORROW = LocalDate.now().plus(Period.ofDays(1)).format(DATE_BASIC_FORMATTER);

    public static final String DELIVERY_DATE_DESC_TODAY = " " + PREFIX_DELIVERY_DATE + DATE_TODAY;
    public static final String DELIVERY_DATE_DESC_YESTERDAY = " " + PREFIX_DELIVERY_DATE + DATE_YESTERDAY;
    public static final String DELIVERY_DATE_DESC_TOMORROW = " " + PREFIX_DELIVERY_DATE + DATE_TOMORROW;

    public static final Index TAB_COMPLETED_PARCELS = Index.fromZeroBased(1);
    public static final Index TAB_ALL_PARCELS = Index.fromZeroBased(0);

    static {
        DESC_AMY = new EditParcelDescriptorBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_AMY)
                .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withDeliveryDate(VALID_DELIVERY_DATE_AMY)
                .withStatus(VALID_STATUS_DELIVERING).withTags(VALID_TAG_FLAMMABLE).build();
        DESC_BOB = new EditParcelDescriptorBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_BOB)
                .withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withDeliveryDate(VALID_DELIVERY_DATE_BOB)
                .withStatus(VALID_STATUS_COMPLETED).withTags(VALID_TAG_FROZEN, VALID_TAG_FLAMMABLE).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        try {
            CommandResult result = command.execute();
            assertEquals(expectedMessage, result.feedbackToUser);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book and the filtered parcel list in the {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<ReadOnlyParcel> expectedFilteredList = new ArrayList<>(actualModel.getFilteredParcelList());

        try {
            command.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedAddressBook, actualModel.getAddressBook());
            assertEquals(expectedFilteredList, actualModel.getFilteredParcelList());
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the first parcel in the {@code model}'s address book.
     */
    public static void showFirstParcelOnly(Model model) {
        ReadOnlyParcel parcel = model.getActiveList().get(0);
        final String[] splitName = parcel.getName().fullName.split("\\s+");
        model.updateFilteredParcelList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assert model.getFilteredParcelList().size() == 1;
    }

    //@@author kennard123661
    /**
     * Updates {@code model}'s filtered list to show only the first parcel in the {@code model}'s address book.
     * with status not completed (Active List default state references Undelivered parcels)
     */
    public static void showFirstParcelInActiveListOnly(Model model) {
        List<ReadOnlyParcel> parcels = model.getAddressBook().getParcelList();
        Predicate<ReadOnlyParcel> predicate = (model.getActiveList().equals(model.getFilteredUndeliveredParcelList()))
                ? getDeliveredPredicate().negate() : getDeliveredPredicate();

        // find the first parcel in the master list that meets the active list predicate
        Optional<ReadOnlyParcel> firstParcelOptional = parcels.stream().filter(predicate).findFirst();

        if (!firstParcelOptional.isPresent()) {
            throw new NullPointerException("No parcels in active list");
        }

        final String[] splitName = firstParcelOptional.get().getName().fullName.split("\\s+");
        model.updateFilteredParcelList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assert model.getActiveList().size() == 1;
        assert model.getFilteredParcelList().size() == 1;
    }
    //@@author

    /**
     * Deletes the first parcel in {@code model}'s filtered list from {@code model}'s address book.
     */
    public static void deleteFirstParcel(Model model) {
        ReadOnlyParcel firstParcel = model.getFilteredParcelList().get(0);
        try {
            model.deleteParcel(firstParcel);
        } catch (ParcelNotFoundException pnfe) {
            throw new AssertionError("Parcel in filtered list must exist in model.", pnfe);
        }
    }
}
