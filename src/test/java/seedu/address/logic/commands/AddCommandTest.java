package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Logger;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelStub;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.parcel.Parcel;
import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.model.parcel.exceptions.DuplicateParcelException;
import seedu.address.testutil.ParcelBuilder;

public class AddCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @Test
    public void constructor_nullParcel_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_parcelAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingParcelAdded modelStub = new ModelStubAcceptingParcelAdded();
        Parcel validParcel = new ParcelBuilder().build();

        CommandResult commandResult = getAddCommandForParcel(validParcel, modelStub).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validParcel), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validParcel), modelStub.parcelsAdded);
    }

    @Test
    public void execute_duplicateParcel_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateParcelException();
        Parcel validParcel = new ParcelBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_PARCEL);

        getAddCommandForParcel(validParcel, modelStub).execute();
    }

    @Test
    public void equals() {
        Parcel alice = new ParcelBuilder().withName("Alice").build();
        Parcel bob = new ParcelBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different parcel -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * Generates a new AddCommand with the details of the given parcel.
     */
    private AddCommand getAddCommandForParcel(Parcel parcel, Model model) {
        AddCommand command = new AddCommand(parcel);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A Model stub that always throw a DuplicateParcelException when trying to add a parcel.
     */
    private class ModelStubThrowingDuplicateParcelException extends ModelStub {
        @Override
        public void addParcel(ReadOnlyParcel parcel) throws DuplicateParcelException {
            throw new DuplicateParcelException();
        }

        @Override
        public void addParcelCommand(ReadOnlyParcel parcel) throws DuplicateParcelException {
            addParcel(parcel);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    //@@author fustilio
    /**
     * A Model stub that always accept the parcel being added.
     */
    private class ModelStubAcceptingParcelAdded extends ModelStub {
        final ArrayList<Parcel> parcelsAdded = new ArrayList<>();

        /*
        @Override
        public boolean hasSelected() {
            return false;
        }
        */
        @Override
        public void addParcelCommand(ReadOnlyParcel parcel) throws DuplicateParcelException {
            addParcel(parcel);
        }

        @Override
        public void maintainSorted() {
            Collections.sort(parcelsAdded);
        }

        @Override
        public void forceSelectParcel(ReadOnlyParcel parcel) {
            logger.info("Simulate force selection of parcel.");
        }

        @Override
        public void addParcel(ReadOnlyParcel parcel) throws DuplicateParcelException {
            parcelsAdded.add(new Parcel(parcel));
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
    //@@author

}
