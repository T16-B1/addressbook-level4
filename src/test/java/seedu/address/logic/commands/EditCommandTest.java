package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAB_COMPLETED_PARCELS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DELIVERY_DATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STATUS_COMPLETED;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STATUS_DELIVERING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FROZEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TRACKING_NUMBER_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showFirstParcelInActiveListOnly;
import static seedu.address.model.ModelManager.getDeliveredPredicate;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PARCEL;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PARCEL;
import static seedu.address.testutil.TypicalParcels.getTypicalAddressBook;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.EditCommand.EditParcelDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.parcel.Parcel;
import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.testutil.EditParcelDescriptorBuilder;
import seedu.address.testutil.ParcelBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullParcel_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new EditCommand(null, null);
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Parcel editedParcel = new ParcelBuilder().build();
        EditCommand.EditParcelDescriptor descriptor = new EditParcelDescriptorBuilder(editedParcel).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_PARCEL, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PARCEL_SUCCESS, editedParcel);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateParcel(model.getFilteredParcelList().get(0), editedParcel);
        expectedModel.maintainSorted();

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastParcel = Index.fromOneBased(model.getActiveList().size());
        ReadOnlyParcel lastParcel = model.getActiveList().get(indexLastParcel.getZeroBased());

        ParcelBuilder parcelInList = new ParcelBuilder(lastParcel);
        Parcel editedParcel = parcelInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_FROZEN).withTrackingNumber(VALID_TRACKING_NUMBER_BOB)
                .withStatus(VALID_STATUS_COMPLETED).build();

        EditCommand.EditParcelDescriptor descriptor = new EditParcelDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_FROZEN).withTrackingNumber(VALID_TRACKING_NUMBER_BOB)
                .withStatus(VALID_STATUS_COMPLETED).build();
        EditCommand editCommand = prepareCommand(indexLastParcel, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PARCEL_SUCCESS, editedParcel);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        expectedModel.updateParcel(lastParcel, editedParcel);
        expectedModel.maintainSorted();
        expectedModel.setActiveList(true);
        expectedModel.forceSelectParcel(editedParcel);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);

    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = prepareCommand(INDEX_FIRST_PARCEL, new EditCommand.EditParcelDescriptor());
        ReadOnlyParcel editedParcel = model.getFilteredParcelList().get(INDEX_FIRST_PARCEL.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PARCEL_SUCCESS, editedParcel);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.maintainSorted();
        expectedModel.forceSelectParcel(editedParcel);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showFirstParcelInActiveListOnly(model);

        ReadOnlyParcel parcelInFilteredList = model.getActiveList().get(INDEX_FIRST_PARCEL.getZeroBased());
        Parcel editedParcel = new ParcelBuilder(parcelInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_PARCEL,
                new EditParcelDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PARCEL_SUCCESS, editedParcel);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateParcel(model.getFilteredParcelList().get(0), editedParcel);
        expectedModel.maintainSorted();
        expectedModel.forceSelectParcel(editedParcel);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateParcelUnfilteredList_failure() {
        Parcel firstParcel = new Parcel(model.getFilteredParcelList().get(INDEX_FIRST_PARCEL.getZeroBased()));
        EditParcelDescriptor descriptor = new EditParcelDescriptorBuilder(firstParcel).build();
        EditCommand editCommand = prepareCommand(INDEX_SECOND_PARCEL, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PARCEL);
    }

    @Test
    public void execute_duplicateParcelFilteredList_failure() {
        showFirstParcelInActiveListOnly(model);

        // edit parcel in filtered list into a duplicate in address book
        ReadOnlyParcel parcelInList = model.getAddressBook().getParcelList().get(INDEX_SECOND_PARCEL.getZeroBased());
        EditCommand editCommand = prepareCommand(INDEX_FIRST_PARCEL,
                new EditParcelDescriptorBuilder(parcelInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PARCEL);
    }

    @Test
    public void execute_invalidParcelIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredParcelList().size() + 1);
        EditCommand.EditParcelDescriptor descriptor = new EditParcelDescriptorBuilder().withName(VALID_NAME_BOB)
                .build();
        EditCommand editCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PARCEL_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidParcelIndexFilteredList_failure() {
        showFirstParcelInActiveListOnly(model);
        Index outOfBoundIndex = INDEX_SECOND_PARCEL;

        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getParcelList().stream()
                .filter(getDeliveredPredicate().negate()).count());

        EditCommand editCommand = prepareCommand(outOfBoundIndex,
                new EditParcelDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PARCEL_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PARCEL, DESC_AMY);

        // same values -> returns true
        EditParcelDescriptor copyDescriptor = new EditCommand.EditParcelDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PARCEL, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PARCEL, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PARCEL, DESC_BOB)));
    }

    //@@author fustilio
    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private EditCommand prepareCommand(Index index, EditCommand.EditParcelDescriptor descriptor) {
        EditCommand editCommand = new EditCommand(index, descriptor);
        editCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editCommand;
    }
    //@@author
}
