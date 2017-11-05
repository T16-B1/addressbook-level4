package seedu.address.model;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.model.parcel.exceptions.DuplicateParcelException;
import seedu.address.model.parcel.exceptions.ParcelNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagNotFoundException;

/**
 * A default model stub that have all of the methods failing.
 */
public class ModelStub implements Model {
    @Override
    public void addParcel(ReadOnlyParcel parcel) throws DuplicateParcelException {
        fail("This method should not be called.");
    }

    @Override
    public void addAllParcels(List<ReadOnlyParcel> parcels, List<ReadOnlyParcel> parcelsAdded,
                              List<ReadOnlyParcel> duplicateParcels) {
        fail("This method should not be called.");
    }

    @Override
    public void setActiveList(boolean isDelivered) {
        fail("This method should not be called.");
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        fail("This method should not be called.");
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        fail("This method should not be called.");
        return null;
    }

    @Override
    public void deleteParcel(ReadOnlyParcel target) throws ParcelNotFoundException {
        fail("This method should not be called.");
    }

    @Override
    public void deleteTag(Tag target) throws TagNotFoundException {
        fail("This method should not be called.");
    }

    @Override
    public void updateParcel(ReadOnlyParcel target, ReadOnlyParcel editedParcel)
            throws DuplicateParcelException {
        fail("This method should not be called.");
    }

    @Override
    public ObservableList<ReadOnlyParcel> getFilteredParcelList() {
        fail("This method should not be called.");
        return null;
    }

    //@@author kennard123661
    @Override
    public ObservableList<ReadOnlyParcel> getFilteredDeliveredParcelList() {
        fail("This method should not be called.");
        return null;
    }

    @Override
    public ObservableList<ReadOnlyParcel> getActiveList() {
        fail("This method should not be called.");
        return null;
    }

    @Override
    public ObservableList<ReadOnlyParcel> getFilteredUndeliveredParcelList() {
        fail("This method should not be called.");
        return null;
    }
    //@@author

    @Override
    public void updateFilteredParcelList(Predicate<ReadOnlyParcel> predicate) {
        fail("This method should not be called.");
    }

    @Override
    public void maintainSorted() {
        fail("This method should not be called.");
    }

    @Override
    public void forceSelect(Index target) {
        fail("This method should not be called.");
    }

    @Override
    public void forceSelectParcel(ReadOnlyParcel target) {
        fail("This method should not be called.");
    }

    @Override
    public void setTabIndex(Index index) {
        fail("This method should not be called.");
    }

    @Override
    public Index getTabIndex() {
        fail("This method should not be called.");
        return null;
    }

    @Override
    public void addParcelCommand(ReadOnlyParcel parcel) throws DuplicateParcelException {
        fail("This method should not be called.");
    }

    @Override
    public void editParcelCommand(ReadOnlyParcel parcelToEdit, ReadOnlyParcel editedParce) throws DuplicateParcelException, ParcelNotFoundException {
        fail("This method should not be called.");
    }

    /*
    @Override
    public boolean hasSelected() {
        fail("This method should not be called.");
        return false;
    }

    @Override
    public void select() {
        fail("This method should not be called.");
    }

    @Override
    public void unselect() {
        fail("This method should not be called.");
    }

    @Override
    public ReadOnlyParcel getPrevSelectedParcel() {
        fail("This method should not be called.");
        return null;
    }

    @Override
    public void setPrevSelectedParcel(ReadOnlyParcel parcel) {
        fail("This method should not be called.");
    }

    @Override
    public void reselect(ReadOnlyParcel parcel) {
        fail("This method should not be called.");
    }
    */
}
