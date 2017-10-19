package seedu.address.logic.commands;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static seedu.address.logic.commands.ImportCommand.MESSAGE_DUPLICATE_PARCELS;
import static seedu.address.logic.commands.ImportCommand.MESSAGE_SUCCESS;
import static seedu.address.testutil.TypicalParcels.ALICE;
import static seedu.address.testutil.TypicalParcels.BENSON;
import static seedu.address.testutil.TypicalParcels.HOON;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.TypicalParcels;

public class ImportCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void executeImportCommand_throwsCommandException() throws CommandException {
        AddressBook addressBook = new AddressBookBuilder().withParcel(ALICE).withParcel(BENSON).build();
        UserPrefs userPrefs = new UserPrefs();

        ModelManager modelManager = new ModelManager(addressBook, userPrefs);

        List<ReadOnlyParcel> parcels = new ArrayList<>();
        parcels.add(ALICE);
        parcels.add(BENSON);

        ImportCommand importCommand = getImportCommandForParcel(parcels, modelManager);
        thrown.expect(CommandException.class);
        thrown.expectMessage(MESSAGE_DUPLICATE_PARCELS);
        importCommand.execute();
    }

    @Test
    public void executeImportCommandSuccess() throws CommandException {
        AddressBook addressBook = new AddressBookBuilder().build();
        UserPrefs userPrefs = new UserPrefs();

        ModelManager modelManager = new ModelManager(addressBook, userPrefs);

        List<ReadOnlyParcel> parcels = new ArrayList<>();
        parcels.add(ALICE);
        parcels.add(BENSON);

        // importing without any duplicates
        ImportCommand importCommand = getImportCommandForParcel(parcels, modelManager);
        assertEquals(importCommand.execute().feedbackToUser, new CommandResult(String.format(MESSAGE_SUCCESS, 2, 0,
                "\n  " + ALICE.toString() + "\n  " + BENSON.toString(), "\n  (none)")).feedbackToUser);

        // importing with some duplicates
        parcels.add(HOON);
        assertEquals(importCommand.execute().feedbackToUser, new CommandResult(String.format(MESSAGE_SUCCESS, 1, 2,
                "\n  " + HOON.toString(), "\n  " + ALICE.toString() + "\n  " + BENSON.toString())).feedbackToUser);
    }

    @Test
    public void equals() {
        List<ReadOnlyParcel> parcels = TypicalParcels.getTypicalParcels();
        List<ReadOnlyParcel> sameParcels = TypicalParcels.getTypicalParcels();
        List<ReadOnlyParcel> otherParcels = new ArrayList<>();

        otherParcels.add(TypicalParcels.ALICE);
        otherParcels.add(TypicalParcels.AMY);

        ImportCommand importCommand = new ImportCommand(parcels);
        ImportCommand importCommandWithSameParcels = new ImportCommand(sameParcels);
        ImportCommand importCommandWithDifferentParcels = new ImportCommand(otherParcels);

        // basic equality
        assertEquals(importCommand, importCommand);
        assertEquals(importCommand, importCommandWithSameParcels);

        assertFalse(importCommand.equals(importCommandWithDifferentParcels));

        // shift parcel in the list.
        ReadOnlyParcel parcel = sameParcels.get(0);
        sameParcels.remove(parcel);
        sameParcels.add(parcel);

        // test equality checks elements and not order
        assertEquals(importCommand, importCommandWithSameParcels);
    }

    /**
     * Generates a new ImportCommand with the details of the given parcel.
     */
    private ImportCommand getImportCommandForParcel(List<ReadOnlyParcel> parcels, Model model) {
        ImportCommand command = new ImportCommand(parcels);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

}
