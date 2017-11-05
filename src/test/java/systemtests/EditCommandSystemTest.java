package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DELIVERY_DATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STATUS_COMPLETED;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STATUS_DELIVERING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STATUS_PENDING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FLAMMABLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FROZEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TRACKING_NUMBER_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PARCELS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PARCEL;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PARCEL;
import static seedu.address.testutil.TypicalParcels.ADDRESS_DESC_BENSON;
import static seedu.address.testutil.TypicalParcels.AMY;
import static seedu.address.testutil.TypicalParcels.DELIVERY_DATE_DESC_BENSON;
import static seedu.address.testutil.TypicalParcels.EMAIL_DESC_BENSON;
import static seedu.address.testutil.TypicalParcels.KEYWORD_MATCHING_MEIER;
import static seedu.address.testutil.TypicalParcels.NAME_DESC_BENSON;
import static seedu.address.testutil.TypicalParcels.PHONE_DESC_BENSON;
import static seedu.address.testutil.TypicalParcels.TRACKING_NUMBER_DESC_BENSON;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.parcel.Address;
import seedu.address.model.parcel.DeliveryDate;
import seedu.address.model.parcel.Email;
import seedu.address.model.parcel.Name;
import seedu.address.model.parcel.Parcel;
import seedu.address.model.parcel.Phone;
import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.model.parcel.Status;
import seedu.address.model.parcel.TrackingNumber;
import seedu.address.model.parcel.exceptions.DuplicateParcelException;
import seedu.address.model.parcel.exceptions.ParcelNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.ParcelBuilder;
import seedu.address.testutil.ParcelUtil;

public class EditCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void edit() throws Exception {
        Model model = getModel();

        /* ----------------- Performing edit operation while an unfiltered list is being shown ---------------------- */

        /* Case: edit all fields, command with leading spaces, trailing spaces and multiple spaces between each field
         * -> edited
         */
        Index index = Index.fromOneBased(model.getActiveList().size());
        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  " + TRACKING_NUMBER_DESC_BOB
                + " " + NAME_DESC_BOB + "  " + PHONE_DESC_BOB + " " + EMAIL_DESC_BOB + "  " + ADDRESS_DESC_BOB + " "
                + DELIVERY_DATE_DESC_BOB + STATUS_DESC_PENDING + TAG_DESC_FROZEN + " ";
        Parcel editedParcel = new ParcelBuilder().withTrackingNumber(VALID_TRACKING_NUMBER_BOB).withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withDeliveryDate(VALID_DELIVERY_DATE_BOB).withStatus(VALID_STATUS_PENDING)
                .withTags(VALID_TAG_FROZEN).build();
        assertCommandSuccess(command, index, editedParcel);

        /* Case: undo editing the last parcel in the list -> last parcel restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo editing the last parcel in the list -> last parcel edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.updateParcel(
                getModel().getActiveList().get(index.getZeroBased()), editedParcel);
        model.maintainSorted();
        model.forceSelectParcel(editedParcel);
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: edit some fields -> edited */
        index = INDEX_FIRST_PARCEL;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + TAG_DESC_FLAMMABLE;
        ReadOnlyParcel parcelToEdit = getModel().getActiveList().get(index.getZeroBased());
        editedParcel = new ParcelBuilder(parcelToEdit).withTags(VALID_TAG_FLAMMABLE).build();
        assertCommandSuccess(command, index, editedParcel);

        /* Case: clear tags -> cleared */
        index = INDEX_FIRST_PARCEL;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + PREFIX_TAG.getPrefix();
        editedParcel = new ParcelBuilder(parcelToEdit).withTags().build();
        assertCommandSuccess(command, index, editedParcel);

        /* ------------------ Performing edit operation while a filtered list is being shown ------------------------ */

        /* Case: filtered parcel list, edit index within bounds of address book and parcel list -> edited */
        showParcelsWithName(KEYWORD_MATCHING_MEIER);
        index = INDEX_FIRST_PARCEL;
        assertTrue(index.getZeroBased() < getModel().getActiveList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + NAME_DESC_BOB;
        parcelToEdit = getModel().getActiveList().get(index.getZeroBased());
        editedParcel = new ParcelBuilder(parcelToEdit).withName(VALID_NAME_BOB).build();
        assertCommandSuccess(command, index, editedParcel);

        /* Case: filtered parcel list, edit index within bounds of address book but out of bounds of parcel list
         * -> rejected
         */
        showParcelsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getActiveList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB,
                Messages.MESSAGE_INVALID_PARCEL_DISPLAYED_INDEX);

        /* --------------------- Performing edit operation while a parcel card is selected -------------------------- */

        /* Case: selects first card in the parcel list, edit a parcel -> edited, card selection remains unchanged but
         * browser url changes
         */
        showAllParcels();
        index = INDEX_FIRST_PARCEL;
        selectParcel(index);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING
                + TAG_DESC_FLAMMABLE;
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new parcel's name
        assertCommandSuccess(command, index, AMY, index);

        /* --------------------------------- Performing invalid edit operation -------------------------------------- */

        /* Case: edit a parcel with new values same as existing values -> edited */
        index = INDEX_SECOND_PARCEL;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + TRACKING_NUMBER_DESC_BENSON + NAME_DESC_BENSON
                + PHONE_DESC_BENSON + EMAIL_DESC_BENSON + ADDRESS_DESC_BENSON + DELIVERY_DATE_DESC_BENSON
                + STATUS_DESC_PENDING + TAG_DESC_FROZEN + TAG_DESC_FLAMMABLE;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_PARCEL);

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getActiveList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB,
                Messages.MESSAGE_INVALID_PARCEL_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PARCEL.getOneBased(),
                EditCommand.MESSAGE_NOT_EDITED);

        /* Case: invalid article number -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PARCEL.getOneBased()
                + INVALID_TRACKING_NUMBER_DESC, TrackingNumber.MESSAGE_TRACKING_NUMBER_CONSTRAINTS);

        /* Case: invalid name -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PARCEL.getOneBased()
                + INVALID_NAME_DESC, Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid phone -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PARCEL.getOneBased()
                + INVALID_PHONE_DESC, Phone.MESSAGE_PHONE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PARCEL.getOneBased()
                + INVALID_EMAIL_DESC, Email.MESSAGE_EMAIL_CONSTRAINTS);

        /* Case: invalid address -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PARCEL.getOneBased()
                + INVALID_ADDRESS_DESC, Address.MESSAGE_ADDRESS_CONSTRAINTS);

        /* Case: invalid delivery date -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PARCEL.getOneBased()
                + INVALID_DELIVERY_DATE_DESC, DeliveryDate.MESSAGE_DELIVERY_DATE_CONSTRAINTS);

        /* Case: invalid status -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PARCEL.getOneBased()
                + INVALID_STATUS_DESC, Status.MESSAGE_STATUS_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PARCEL.getOneBased()
                + INVALID_TAG_DESC, Tag.MESSAGE_TAG_CONSTRAINTS);

        /* Case: edit a parcel with new values same as another parcel's values -> rejected */
        executeCommand(ParcelUtil.getAddCommand(AMY));
        assertTrue(getModel().getAddressBook().getParcelList().contains(AMY));
        index = Index.fromOneBased(getModel().getActiveList().size());
        assertFalse(getModel().getActiveList().get(index.getZeroBased()).equals(AMY));
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING
                + TAG_DESC_FLAMMABLE;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_PARCEL);

        /* Case: edit a parcel with new values same as another parcel's values but with different tags -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + TRACKING_NUMBER_DESC_AMY + NAME_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + DELIVERY_DATE_DESC_AMY + STATUS_DESC_DELIVERING
                + TAG_DESC_FROZEN;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_PARCEL);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, ReadOnlyParcel, Index)} except that
     * the browser url and selected card remain unchanged.
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, ReadOnlyParcel, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, ReadOnlyParcel editedParcel) {
        assertCommandSuccess(command, toEdit, editedParcel, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the parcel at index {@code toEdit} being
     * updated to values specified {@code editedParcel}.<br>
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, ReadOnlyParcel editedParcel,
            Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        try {
            expectedModel.editParcelCommand(expectedModel.getActiveList().get(toEdit.getZeroBased()), editedParcel);
//            expectedModel.updateParcel(
//                    expectedModel.getActiveList().get(toEdit.getZeroBased()), editedParcel);
//            expectedModel.maintainSorted();
//            expectedModel.updateFilteredParcelList(PREDICATE_SHOW_ALL_PARCELS);
//            expectedModel.forceSelectParcel(editedParcel);
        } catch (DuplicateParcelException dpe) {
            throw new IllegalArgumentException(
                    "editedParcel is a duplicate in expectedModel.");
        } catch (ParcelNotFoundException pnfe) {
            throw new IllegalArgumentException(
                    "editedParcel is not found in the Model");
        }

        assertCommandSuccess(command, expectedModel,
                String.format(EditCommand.MESSAGE_EDIT_PARCEL_SUCCESS, editedParcel), expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card update accordingly depending on the card at
     * {@code expectedSelectedCardIndex}.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see AddressBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
            Index expectedSelectedCardIndex) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            // assertSelectedCardUnchanged();
        }
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 5. Asserts that the command box has the error style.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
