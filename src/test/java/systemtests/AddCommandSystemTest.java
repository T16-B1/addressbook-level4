package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
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
import static seedu.address.logic.commands.CommandTestUtil.STATUS_DESC_PENDING;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FLAMMABLE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FROZEN;
import static seedu.address.logic.commands.CommandTestUtil.TRACKING_NUMBER_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TRACKING_NUMBER_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DELIVERY_DATE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DELIVERY_DATE_AMY_FULLSTOPS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DELIVERY_DATE_AMY_SLASHES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DELIVERY_DATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STATUS_DELIVERING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FLAMMABLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TRACKING_NUMBER_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TRACKING_NUMBER_BOB;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PARCEL;
import static seedu.address.testutil.TypicalParcels.ALICE;
import static seedu.address.testutil.TypicalParcels.AMY;
import static seedu.address.testutil.TypicalParcels.BOB;
import static seedu.address.testutil.TypicalParcels.CARL;
import static seedu.address.testutil.TypicalParcels.HOON;
import static seedu.address.testutil.TypicalParcels.IDA;
import static seedu.address.testutil.TypicalParcels.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.parcel.Address;
import seedu.address.model.parcel.DeliveryDate;
import seedu.address.model.parcel.Email;
import seedu.address.model.parcel.Name;
import seedu.address.model.parcel.Phone;
import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.model.parcel.Status;
import seedu.address.model.parcel.TrackingNumber;
import seedu.address.model.parcel.exceptions.DuplicateParcelException;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.ParcelBuilder;
import seedu.address.testutil.ParcelUtil;

public class AddCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void add() throws Exception {
        Model model = getModel();
        /*
         * Case: add a parcel without tags to a non-empty address book, command with leading spaces and
         * trailing spaces -> added
         */
        ReadOnlyParcel toAdd = AMY;
        String command = "   " + AddCommand.COMMAND_WORD + "  " + TRACKING_NUMBER_DESC_AMY + "  "
                + NAME_DESC_AMY + "  " + PHONE_DESC_AMY + " " + EMAIL_DESC_AMY + "   "
                + ADDRESS_DESC_AMY + "   " + DELIVERY_DATE_DESC_AMY + " " + STATUS_DESC_DELIVERING + "   "
                + TAG_DESC_FLAMMABLE + " ";
        assertCommandSuccess(command, toAdd);

        /* Case: undo adding Amy to the list -> Amy deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding Amy to the list -> Amy added again */
        command = RedoCommand.COMMAND_WORD;
        model.addParcel(toAdd);
        model.maintainSorted();
        model.forceSelectParcel(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add a duplicate parcel -> rejected */
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING
                + TAG_DESC_FLAMMABLE;
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PARCEL);

        /*
         * Case: add a parcel with all fields same as another parcel in the address book except status ->
         * rejected
         */
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + STATUS_DESC_COMPLETED
                + TAG_DESC_FLAMMABLE;
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PARCEL);

        /* Case: add a duplicate parcel except with different tags -> rejected */
        // "friends" is an existing tag used in the default model, see TypicalParcels#ALICE
        // This test will fail is a new tag that is not in the model is used, see the bug documented in
        // AddressBook#addParcel(ReadOnlyParcel)
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + ADDRESS_DESC_AMY + NAME_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY
                + STATUS_DESC_DELIVERING +  " " + TAG_DESC_FROZEN;
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PARCEL);

        /*
         * Case: add a parcel with all fields same as another parcel in the address book
         * except tracking number -> added
         */
        toAdd = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_BOB).withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withDeliveryDate(VALID_DELIVERY_DATE_AMY).withStatus(VALID_STATUS_DELIVERING)
                .withTags(VALID_TAG_FLAMMABLE).build();
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_BOB + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING
                + TAG_DESC_FLAMMABLE;
        assertCommandSuccess(command, toAdd);

        /*
         * Case: add a parcel with all fields same as another parcel in the address book except name
         * -> added
         */
        toAdd = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_AMY).withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withDeliveryDate(VALID_DELIVERY_DATE_AMY).withStatus(VALID_STATUS_DELIVERING)
                .withTags(VALID_TAG_FLAMMABLE).build();
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING
                + TAG_DESC_FLAMMABLE;
        assertCommandSuccess(command, toAdd);

        /* Case: add a parcel with all fields same as another parcel in the address book except phone ->
         * added
         */
        toAdd = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_AMY).withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withDeliveryDate(VALID_DELIVERY_DATE_AMY).withStatus(VALID_STATUS_DELIVERING)
                .withTags(VALID_TAG_FLAMMABLE).build();
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_BOB
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING
                + TAG_DESC_FLAMMABLE;
        assertCommandSuccess(command, toAdd);

        /* Case: add a parcel with all fields same as another parcel in the address book except email ->
         * added
         */
        toAdd = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_AMY).withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_AMY)
                .withDeliveryDate(VALID_DELIVERY_DATE_AMY).withStatus(VALID_STATUS_DELIVERING)
                .withTags(VALID_TAG_FLAMMABLE).build();
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_BOB
                + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING + TAG_DESC_FLAMMABLE;
        assertCommandSuccess(command, toAdd);

        /*
         * Case: add a parcel with all fields same as another parcel in the address book except address ->
         * added
         */
        toAdd = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_AMY).withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_BOB)
                .withDeliveryDate(VALID_DELIVERY_DATE_AMY).withStatus(VALID_STATUS_DELIVERING)
                .withTags(VALID_TAG_FLAMMABLE).build();
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_BOB + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING
                + TAG_DESC_FLAMMABLE;
        assertCommandSuccess(command, toAdd);

        /*
         * Case: add a parcel with all fields same as another parcel in the address book
         * except delivery date -> added
         */
        toAdd = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_AMY).withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withDeliveryDate(VALID_DELIVERY_DATE_BOB).withStatus(VALID_STATUS_DELIVERING)
                .withTags(VALID_TAG_FLAMMABLE).build();
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_BOB + STATUS_DESC_DELIVERING
                + TAG_DESC_FLAMMABLE;
        assertCommandSuccess(command, toAdd);

        /* Case: add a parcel with all fields except phone -> added with phone set to default value of 'NIL' */
        toAdd = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_AMY)
                .withName(VALID_NAME_AMY).withPhone(Phone.PHONE_DEFAULT_VALUE).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withDeliveryDate(VALID_DELIVERY_DATE_AMY)
                .withStatus(VALID_STATUS_DELIVERING).withTags(VALID_TAG_FLAMMABLE).build();
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING + TAG_DESC_FLAMMABLE;
        assertCommandSuccess(command, toAdd);

        /* Case: add a parcel with all fields except email -> added with email set to default value of 'NIL' */
        toAdd = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_AMY)
                .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY).withEmail(Email.EMAIL_DEFAULT_VALUE)
                .withAddress(VALID_ADDRESS_AMY).withDeliveryDate(VALID_DELIVERY_DATE_AMY)
                .withStatus(VALID_STATUS_DELIVERING).withTags(VALID_TAG_FLAMMABLE).build();
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY
                + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING + TAG_DESC_FLAMMABLE;
        assertCommandSuccess(command, toAdd);

        /* Case: filters the parcel list before adding -> added */
        executeCommand(FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER);
        assert getModel().getFilteredParcelList().size()
                < getModel().getAddressBook().getParcelList().size();
        assertCommandSuccess(IDA);

        /* Case: add to empty address book -> added */
        executeCommand(ClearCommand.COMMAND_WORD);
        assert getModel().getAddressBook().getParcelList().size() == 0;
        assertCommandSuccess(ALICE);

        /* Case: add a parcel with tags, command with parameters in random order -> added */
        toAdd = BOB;
        command = AddCommand.COMMAND_WORD + TAG_DESC_FLAMMABLE + PHONE_DESC_BOB + ADDRESS_DESC_BOB
                + STATUS_DESC_COMPLETED + NAME_DESC_BOB + TAG_DESC_FROZEN + EMAIL_DESC_BOB
                + TRACKING_NUMBER_DESC_BOB + DELIVERY_DATE_DESC_BOB;
        assertCommandSuccess(command, toAdd);

        /*
         * Case: selects first card in the parcel list, add a parcel -> added, card selection
         * changes to recently added card
         */
        executeCommand(SelectCommand.COMMAND_WORD + " 1");
        selectParcel(INDEX_FIRST_PARCEL);
        assert getParcelListPanel().isAnyCardSelected();
        assertCommandSuccess(CARL);

        /* Case: add a parcel, missing tags -> added */
        assertCommandSuccess(HOON);

        /* Case: add a parcel with all fields but using delivery date with fullstops
           -> added with delivery date with hyphens */
        toAdd = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_AMY)
                .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withDeliveryDate(VALID_DELIVERY_DATE_AMY_FULLSTOPS)
                .withStatus(VALID_STATUS_DELIVERING).withTags(VALID_TAG_FLAMMABLE).build();
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING + TAG_DESC_FLAMMABLE;
        assertCommandSuccess(command, toAdd);

        /* Case: add a parcel with all fields but using delivery date with forward slash
           -> added with delivery date with forward slash */
        executeCommand(ClearCommand.COMMAND_WORD);
        assert getModel().getAddressBook().getParcelList().size() == 0;
        assert getModel().getActiveList().size() == 0;
        toAdd = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_AMY)
                .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withDeliveryDate(VALID_DELIVERY_DATE_AMY_SLASHES)
                .withStatus(VALID_STATUS_DELIVERING).withTags(VALID_TAG_FLAMMABLE).build();
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING + TAG_DESC_FLAMMABLE;
        assertCommandSuccess(command, toAdd);


        /* Case: missing tracking number -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddCommand.MESSAGE_USAGE));

        /* Case: missing name -> rejected */
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddCommand.MESSAGE_USAGE));

        /* Case: missing address -> rejected */
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddCommand.MESSAGE_USAGE));

        /* Case: missing delivery date -> rejected */
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + STATUS_DESC_DELIVERING;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddCommand.MESSAGE_USAGE));

        /* Case: missing status -> accepted */
        toAdd = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_BOB).withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withDeliveryDate(VALID_DELIVERY_DATE_AMY).withTags(VALID_TAG_FLAMMABLE).build();
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_BOB + NAME_DESC_AMY + PHONE_DESC_BOB
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + TAG_DESC_FLAMMABLE;
        assertCommandSuccess(command, toAdd);

        /* Case: invalid keyword -> rejected */
        command = "adds " + ParcelUtil.getParcelDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid tracking number -> rejected */
        command = AddCommand.COMMAND_WORD + INVALID_TRACKING_NUMBER_DESC + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY;
        assertCommandFailure(command, TrackingNumber.MESSAGE_TRACKING_NUMBER_CONSTRAINTS);

        /* Case: invalid name -> rejected */
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + INVALID_NAME_DESC + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY;
        assertCommandFailure(command, Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid phone -> rejected */
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + INVALID_PHONE_DESC
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY;
        assertCommandFailure(command, Phone.MESSAGE_PHONE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY
                + INVALID_EMAIL_DESC + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY;
        assertCommandFailure(command, Email.MESSAGE_EMAIL_CONSTRAINTS);

        /* Case: invalid address -> rejected */
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + INVALID_ADDRESS_DESC + DELIVERY_DATE_DESC_AMY;
        assertCommandFailure(command, Address.MESSAGE_ADDRESS_CONSTRAINTS);

        /* Case: invalid delivery date -> rejected */
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + INVALID_DELIVERY_DATE_DESC;
        assertCommandFailure(command, DeliveryDate.MESSAGE_DELIVERY_DATE_CONSTRAINTS);

        /* Case: invalid status -> rejected */
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING
                + INVALID_STATUS_DESC;
        assertCommandFailure(command, Status.MESSAGE_STATUS_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        command = AddCommand.COMMAND_WORD + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + INVALID_TAG_DESC;
        assertCommandFailure(command, Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and verifies that the command box
     * displays an empty string, the result display box displays the success message of executing
     * {@code AddCommand} with the details of {@code toAdd}, and the model related components equal to the
     * current model added with {@code toAdd}. These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the command box has the default style class, the status bar's sync status changes,
     * the browser url and selected card remains unchanged.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(ReadOnlyParcel toAdd) {
        assertCommandSuccess(ParcelUtil.getAddCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(ReadOnlyParcel)}.
     * Executes {@code command} instead.
     * @see AddCommandSystemTest#assertCommandSuccess(ReadOnlyParcel)
     */
    private void assertCommandSuccess(String command, ReadOnlyParcel toAdd) {
        Model expectedModel = getModel();
        try {
            expectedModel.addParcelCommand(toAdd);
        } catch (DuplicateParcelException dpe) {
            throw new IllegalArgumentException("toAdd already exists in the model.");
        }
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS, toAdd);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, ReadOnlyParcel)} except that
     * the result display box displays {@code expectedResultMessage} and the model related components equal to
     * {@code expectedModel}.
     * @see AddCommandSystemTest#assertCommandSuccess(String, ReadOnlyParcel)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        // assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box
     * has the error style.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        // assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
