package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_FILE_NOT_FOUND;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_DATA;
import static seedu.address.testutil.TypicalParcels.ALICE;
import static seedu.address.testutil.TypicalParcels.AMY;
import static seedu.address.testutil.TypicalParcels.BENSON;
import static seedu.address.testutil.TypicalParcels.BOB;
import static seedu.address.testutil.TypicalParcels.CARL;
import static seedu.address.testutil.TypicalParcels.IDA;
import static seedu.address.testutil.TypicalParcels.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.model.tag.Tag;
import seedu.address.storage.XmlAddressBookStorage;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.ParcelBuilder;

//@@author kennard123661
public class ImportCommandSystemTest extends AddressBookSystemTest {

    private static final String STORAGE_FILE = "testAddressBookForImportSystem.xml";

    @Before
    public void start() throws Exception {
        // reset the storage file used in ImportCommandSystemTest
        AddressBook addressBook = new AddressBookBuilder().build();
        new XmlAddressBookStorage("./data/import/" + STORAGE_FILE).saveAddressBook(addressBook);
    }

    @Test
    public void importTest() throws Exception {
        Model model = getModel();

        /* Case: import parcels without tags to a non-empty address book, command with leading spaces and trailing
         * spaces -> added
         */
        AddressBook addressBook = new AddressBookBuilder().withParcel(AMY).withParcel(BOB).build();
        XmlAddressBookStorage storage = new XmlAddressBookStorage(
                "./data/import/" + STORAGE_FILE);
        storage.saveAddressBook(addressBook);

        List<ReadOnlyParcel> parcelsAdded;
        List<ReadOnlyParcel> duplicateParcels = new ArrayList<>();
        List<ReadOnlyParcel> parcels = addressBook.getParcelList();

        String command = "   " + ImportCommand.COMMAND_WORD + "  " + STORAGE_FILE + "   ";
        assertCommandSuccess(command, parcels, parcels, new ArrayList<>());

        /* Case: undo importing testAddressBookForImportSystem.xml data to the list -> Amy and Bob deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding Amy to the list -> Amy added again */
        command = RedoCommand.COMMAND_WORD;
        model.addAllParcels(parcels, new ArrayList<>(), new ArrayList<>());
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add a duplicate parcel -> rejected */
        command = ImportCommand.COMMAND_WORD + " " + STORAGE_FILE;
        assertCommandFailure(command, ImportCommand.MESSAGE_DUPLICATE_PARCELS);

        /* Case: import an addressbook xml file containing duplicate parcels except with different tags -> rejected */
        // AddressBook#addAllParcels(List<ReadOnlyParcel>)
        addressBook = new AddressBookBuilder().withParcel(new ParcelBuilder(AMY).withTags(Tag.HEAVY.toString()).build())
                .withParcel(new ParcelBuilder(BOB).withTags(Tag.FRAGILE.toString()).build()).build();
        storage.saveAddressBook(addressBook);
        command = ImportCommand.COMMAND_WORD + " " + STORAGE_FILE;
        assertCommandFailure(command, ImportCommand.MESSAGE_DUPLICATE_PARCELS);

        /* Case: imports parcels with all fields same as other parcels in the address book except name -> imported */
        addressBook = new AddressBookBuilder().withParcel(new ParcelBuilder(AMY).withName("Kyle").build())
                .withParcel(new ParcelBuilder(BOB).withName("John").build()).build();
        storage.saveAddressBook(addressBook);
        parcelsAdded = addressBook.getParcelList();
        assertCommandSuccess(command, parcelsAdded, parcelsAdded, duplicateParcels);

        /* Case: imports parcels with all fields same as other parcels in the address book except phone -> imported */
        addressBook = new AddressBookBuilder().withParcel(new ParcelBuilder(AMY).withPhone("98547321").build())
                .withParcel(new ParcelBuilder(BOB).withPhone("96758989").build()).build();
        storage.saveAddressBook(addressBook);
        parcelsAdded = addressBook.getParcelList();
        assertCommandSuccess(command, parcelsAdded, parcelsAdded, duplicateParcels);

        /* Case: imports parcels with all fields same as other parcels in the address book except email -> imported */
        addressBook = new AddressBookBuilder().withParcel(new ParcelBuilder(AMY).withEmail("amy@gmail.com").build())
                .withParcel(new ParcelBuilder(BOB).withEmail("bob@gmail.com").build()).build();
        storage.saveAddressBook(addressBook);
        parcelsAdded = addressBook.getParcelList();
        assertCommandSuccess(command, parcelsAdded, parcelsAdded, duplicateParcels);

        /* Case: imports parcels with all fields same as another parcel in the address book
         * except address -> imported
         */
        addressBook = new AddressBookBuilder().withParcel(new ParcelBuilder(AMY).withAddress("Amy Street S123661")
                .build()).withParcel(new ParcelBuilder(BOB).withAddress("BOB Street S456123").build()).build();
        storage.saveAddressBook(addressBook);
        parcelsAdded = addressBook.getParcelList();
        assertCommandSuccess(command, parcelsAdded, parcelsAdded, duplicateParcels);

        /* Case: add a parcel with all fields same as another parcel in the address book
         * except tracking number -> imported
         */
        addressBook = new AddressBookBuilder().withParcel(new ParcelBuilder(AMY).withTrackingNumber("RR123564897SG")
                .build()).withParcel(new ParcelBuilder(BOB).withTrackingNumber("RR123564897SG").build()).build();
        storage.saveAddressBook(addressBook);
        parcelsAdded = addressBook.getParcelList();
        assertCommandSuccess(command, parcelsAdded, parcelsAdded, duplicateParcels);

        /* Case: add a parcel with all fields same as another parcel in the address book
         * except delivery date -> imported
         */
        addressBook = new AddressBookBuilder().withParcel(new ParcelBuilder(AMY).withDeliveryDate("12-01-2017")
                .build()).withParcel(new ParcelBuilder(BOB).withDeliveryDate("12-01-2017").build()).build();
        storage.saveAddressBook(addressBook);
        parcelsAdded = addressBook.getParcelList();
        assertCommandSuccess(command, parcelsAdded, parcelsAdded, duplicateParcels);

        /* Case: filters the parcel list before importing -> imported */
        executeCommand(FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER);
        assert getModel().getFilteredParcelList().size() < getModel().getAddressBook().getParcelList().size();
        addressBook = new AddressBookBuilder().withParcel(IDA).build();
        storage.saveAddressBook(addressBook);
        parcelsAdded = addressBook.getParcelList();
        assertCommandSuccess(command, parcelsAdded, parcelsAdded, duplicateParcels);

        /* Case: imports to ann empty address book -> added */
        executeCommand(ClearCommand.COMMAND_WORD);
        addressBook = new AddressBookBuilder().withParcel(IDA).withParcel(BENSON).build();
        parcelsAdded = addressBook.getParcelList();
        storage.saveAddressBook(addressBook);
        assert getModel().getAddressBook().getParcelList().size() == 0;
        assertCommandSuccess(command, parcelsAdded, parcelsAdded, duplicateParcels);

        /* Case: selects first card in the parcel list, import parcels -> import, card selection remains unchanged */
        executeCommand(SelectCommand.COMMAND_WORD + " 1");
        assert getParcelListPanel().isAnyCardSelected();
        addressBook = new AddressBookBuilder().withParcel(CARL).withParcel(ALICE).build();
        storage.saveAddressBook(addressBook);
        parcelsAdded = addressBook.getParcelList();
        assertCommandSuccess(command, parcelsAdded, parcelsAdded, duplicateParcels);

        /* Case: invalid keyword -> rejected */
        command = "imports " + STORAGE_FILE;
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: missing file -> rejected */
        command = "import " + "missing.xml";
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ImportCommand.MESSAGE_USAGE) + "\nMore Info: " + MESSAGE_FILE_NOT_FOUND);

        /* Case: invalid xml file -> rejected */
        command = "import " + "testNotXmlFormatAddressBook.xml";
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ImportCommand.MESSAGE_USAGE) + "\nMore Info: " + String.format(MESSAGE_INVALID_DATA,
                "./data/import/testNotXmlFormatAddressBook.xml"));

    }

    @After
    public void clear() throws Exception {
        // reset the storage file used in ImportCommandSystemTest
        AddressBook addressBook = new AddressBookBuilder().build();
        new XmlAddressBookStorage("./data/import/" + STORAGE_FILE).saveAddressBook(addressBook);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String)}. Executes {@code command}
     * instead.
     */
    private void assertCommandSuccess(String command, List<ReadOnlyParcel> parcelsToAdd,
                                      List<ReadOnlyParcel> expectedAddedParcels,
                                      List<ReadOnlyParcel> expectedDuplicateParcels) {

        Model expectedModel = getModel();
        List<ReadOnlyParcel> duplicateParcels = new ArrayList<>();
        List<ReadOnlyParcel> addedParcels = new ArrayList<>();

        expectedModel.addAllParcels(parcelsToAdd, addedParcels, duplicateParcels);
        String expectedResultMessage = String.format(ImportCommand.MESSAGE_SUCCESS, expectedAddedParcels.size(),
                expectedDuplicateParcels.size(), ImportCommand.getImportFormattedParcelListString(expectedAddedParcels),
                ImportCommand.getImportFormattedParcelListString(expectedDuplicateParcels));

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String)} except that the result
     * display box displays {@code expectedResultMessage} and the model related components equal to
     * {@code expectedModel}.
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
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
