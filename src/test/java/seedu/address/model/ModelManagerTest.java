package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PARCELS;
import static seedu.address.testutil.TypicalParcels.ALICE;
import static seedu.address.testutil.TypicalParcels.BENSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.parcel.NameContainsKeywordsPredicate;
import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.TypicalParcels;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getFilteredParcelList_modifyList_throwsUnsupportedOperationException() {
        ModelManager modelManager = new ModelManager();
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredParcelList().remove(0);
    }

    //@@author kennard123661
    @Test
    public void addAllParcelsTest() {
        AddressBook addressBook = new AddressBookBuilder().withParcel(ALICE).withParcel(BENSON).build();
        UserPrefs userPrefs = new UserPrefs();

        ModelManager modelManager = new ModelManager(addressBook, userPrefs);

        List<ReadOnlyParcel> parcels = TypicalParcels.getTypicalParcels();
        List<ReadOnlyParcel> parcelsAdded = new ArrayList<>();
        List<ReadOnlyParcel> duplicateParcels = new ArrayList<>();
        List<ReadOnlyParcel> expectedParcels = TypicalParcels.getTypicalParcels();

        // logic test
        modelManager.addAllParcels(parcels, parcelsAdded, duplicateParcels);
        assertEquals(8, parcels.size());
        assertEquals(6, parcelsAdded.size());
        assertEquals(2, duplicateParcels.size());

        // elements in parcels test
        for (int i = 0; i < expectedParcels.size(); i++) {
            assertEquals(expectedParcels.get(i), parcels.get(i));
        }

        // ensure that addressbook updated
        assertEquals(3, modelManager.getAddressBook().getTagList().size());
        assertEquals(8, modelManager.getAddressBook().getParcelList().size());
        assertEquals(1, modelManager.getFilteredDeliveredParcelList().size());
        assertEquals(7, modelManager.getFilteredUndeliveredParcelList().size());

        assertEquals(modelManager.getActiveList(), modelManager.getFilteredUndeliveredParcelList());
        modelManager.setActiveList(true);
        assertEquals(modelManager.getActiveList(), modelManager.getFilteredDeliveredParcelList());
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withParcel(ALICE).withParcel(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        ModelManager modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredParcelList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredParcelList(PREDICATE_SHOW_ALL_PARCELS);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookName("differentName");
        assertTrue(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }

}
