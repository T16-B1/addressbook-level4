package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showFirstParcelInActiveListOnly;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PARCEL;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PARCEL;
import static seedu.address.testutil.TypicalParcels.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.parcel.ReadOnlyParcel;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        model.maintainSorted();
        ReadOnlyParcel parcelToDelete = model.getActiveList().get(INDEX_FIRST_PARCEL.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_PARCEL);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PARCEL_SUCCESS, parcelToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.maintainSorted();
        expectedModel.deleteParcel(parcelToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    //@@author kennard123661
    @Test
    public void execute_validIndexActiveList_success() throws Exception {
        // active list will be valid.
        model.maintainSorted();
        ReadOnlyParcel parcelToDelete = model.getActiveList().get(Index.fromOneBased(model.getActiveList().size())
                .getZeroBased());
        Index activeListValidIndex = Index.fromOneBased(model.getActiveList().size());
        DeleteCommand deleteCommand = prepareCommand(activeListValidIndex);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PARCEL_SUCCESS, parcelToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.maintainSorted();
        expectedModel.deleteParcel(parcelToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }
    //@@author

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        // getting directly from the master list (all parcels will throw an error since its not the active list)
        // # of parcels in active list =/= # of parcels in master list (list of all parcels)
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredParcelList().size());
        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PARCEL_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexActiveList_throwsCommandException() throws Exception {
        // getting directly from the master list (all parcels will throw an error since its not the active list)
        Index outOfBoundIndex = Index.fromOneBased(model.getActiveList().size() + 1);
        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PARCEL_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showFirstParcelInActiveListOnly(model);

        ReadOnlyParcel parcelToDelete = model.getActiveList().get(INDEX_FIRST_PARCEL.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_PARCEL);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PARCEL_SUCCESS, parcelToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteParcel(parcelToDelete);
        showNoParcel(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showFirstParcelInActiveListOnly(model);

        Index outOfBoundIndex = INDEX_SECOND_PARCEL;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getParcelList().size());

        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PARCEL_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PARCEL);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PARCEL);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PARCEL);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different parcel -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private DeleteCommand prepareCommand(Index index) {
        DeleteCommand deleteCommand = new DeleteCommand(index);
        deleteCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoParcel(Model model) {
        model.updateFilteredParcelList(p -> false);

        assert model.getFilteredParcelList().isEmpty();
    }
}
