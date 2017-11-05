package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.ui.ParcelListPanel.INDEX_FIRST_TAB;
import static seedu.address.ui.ParcelListPanel.INDEX_SECOND_TAB;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.events.ui.JumpToTabRequestEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.parcel.Parcel;
import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.model.parcel.Status;
import seedu.address.model.parcel.exceptions.DuplicateParcelException;
import seedu.address.model.parcel.exceptions.ParcelNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagInternalErrorException;
import seedu.address.model.tag.exceptions.TagNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    private static final Predicate<ReadOnlyParcel> deliveredPredicate = p -> p.getStatus().equals(Status.COMPLETED);
    private static final Index TAB_ALL_PARCELS = Index.fromZeroBased(0);
    private static final Index TAB_COMPLETED_PARCELS = Index.fromZeroBased(1);

    // private static boolean selected = false;
    // private static ReadOnlyParcel prevSelectedParcel = null;
    private static Index tabIndex = Index.fromOneBased(1);
    private final AddressBook addressBook;

    private final FilteredList<ReadOnlyParcel> filteredParcels;
    private FilteredList<ReadOnlyParcel> filteredDeliveredParcels;
    private FilteredList<ReadOnlyParcel> filteredUndeliveredParcels;
    private FilteredList<ReadOnlyParcel> activeFilteredList; // references the current selected list
    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        filteredParcels = new FilteredList<>(this.addressBook.getParcelList());
        updatedDeliveredAndUndeliveredList();
        activeFilteredList = filteredUndeliveredParcels;
        ModelListener sl = new ModelListener(this);
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //@@author kennard123661
    /**
     * Updates Delivered and UndeliveredParcelList and resets the Active List to the correct reference
     */
    private void updatedDeliveredAndUndeliveredList() {
        // checks reference equality
        boolean isActiveDelivered = activeFilteredList == filteredDeliveredParcels;

        filteredDeliveredParcels = filteredParcels.filtered(deliveredPredicate);
        filteredUndeliveredParcels = filteredParcels.filtered(deliveredPredicate.negate());

        setActiveList(isActiveDelivered);
    }

    @Override
    public void setActiveList(boolean isDelivered) {
        activeFilteredList = isDelivered ? filteredDeliveredParcels : filteredUndeliveredParcels;
    }
    //@@author

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        addressBook.resetData(newData);
        updatedDeliveredAndUndeliveredList();
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(addressBook));
    }

    @Override
    public synchronized void deleteParcel(ReadOnlyParcel target) throws ParcelNotFoundException {
        addressBook.removeParcel(target);
        indicateAddressBookChanged();
    }

    //@@author fustilio
    /** Deletes the tag from every parcel in the address book */
    public void deleteTag(Tag target) throws TagNotFoundException, TagInternalErrorException {

        int tagsFound = 0;
        Iterator it = addressBook.getParcelList().iterator();
        while (it.hasNext()) {
            Parcel oldParcel = (Parcel) it.next();
            Parcel newParcel = new Parcel(oldParcel);
            Set<Tag> newTags = new HashSet<>(newParcel.getTags());
            if (newTags.contains(target)) {
                newTags.remove(target);
                tagsFound++;
            }

            newParcel.setTags(newTags);

            try {
                addressBook.updateParcel(oldParcel, newParcel);
            } catch (DuplicateParcelException | ParcelNotFoundException dpe) {
                throw new TagInternalErrorException();
            }
        }

        if (tagsFound == 0) {
            throw new TagNotFoundException();
        }

        indicateAddressBookChanged();
    }
    //@@author

    @Override
    public synchronized void addParcel(ReadOnlyParcel parcel) throws DuplicateParcelException {
        addressBook.addParcel(parcel);
        updateFilteredParcelList(PREDICATE_SHOW_ALL_PARCELS);
        indicateAddressBookChanged();
    }

    //@@author kennard123661
    @Override
    public synchronized void addAllParcels(List<ReadOnlyParcel> parcels, List<ReadOnlyParcel> parcelsAdded,
                                           List<ReadOnlyParcel> duplicateParcels) {

        for (ReadOnlyParcel parcel : parcels) {
            ReadOnlyParcel parcelToAdd = new Parcel(parcel);
            try {
                addressBook.addParcel(parcelToAdd);
                parcelsAdded.add(parcelToAdd);
            } catch (DuplicateParcelException ive) {
                duplicateParcels.add(parcelToAdd);
            }
        }

        updateFilteredParcelList(PREDICATE_SHOW_ALL_PARCELS);
        indicateAddressBookChanged();
    }
    //@@author

    @Override
    public void updateParcel(ReadOnlyParcel target, ReadOnlyParcel editedParcel)
            throws DuplicateParcelException, ParcelNotFoundException {
        requireAllNonNull(target, editedParcel);

        addressBook.updateParcel(target, editedParcel);
        updateFilteredParcelList(PREDICATE_SHOW_ALL_PARCELS);
        indicateAddressBookChanged();
    }


    //=========== Filtered Parcel List Accessors =============================================================
    /**
     * Returns an unmodifiable view of the list of {@code ReadOnlyParcel} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<ReadOnlyParcel> getFilteredParcelList() {
        return FXCollections.unmodifiableObservableList(filteredParcels);
    }

    //@@author kennard123661
    /**
     * Returns an unmodifiable view of the list of {@code ReadOnlyParcel} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<ReadOnlyParcel> getFilteredDeliveredParcelList() {
        return FXCollections.unmodifiableObservableList(filteredDeliveredParcels);
    }

    /**
     * Returns an unmodifiable view of the list of {@code ReadOnlyParcel} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<ReadOnlyParcel> getActiveList() {
        return FXCollections.unmodifiableObservableList(activeFilteredList);
    }

    /**
     * Returns an unmodifiable view of the list of {@code ReadOnlyParcel} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<ReadOnlyParcel> getFilteredUndeliveredParcelList() {
        return FXCollections.unmodifiableObservableList(filteredUndeliveredParcels);
    }
    //@@author

    @Override
    public void updateFilteredParcelList(Predicate<ReadOnlyParcel> predicate) {
        requireNonNull(predicate);
        filteredParcels.setPredicate(predicate);
        updatedDeliveredAndUndeliveredList();
    }

    //@@author fustilio
    @Override
    public void maintainSorted() {
        addressBook.sort();
        indicateAddressBookChanged();
    }

    @Override
    public void forceSelect(Index target) {
        EventsCenter.getInstance().post(new JumpToListRequestEvent(target));
    }

    @Override
    public void forceSelectParcel(ReadOnlyParcel target) {
        forceSelect(Index.fromZeroBased(findIndex(target)));
    }

    @Override
    public void setTabIndex(Index index) {
        this.tabIndex = index;
    }

    @Override
    public Index getTabIndex() {
        return this.tabIndex;
    }

    @Override
    public void addParcelCommand(ReadOnlyParcel toAdd) throws DuplicateParcelException {
        this.addParcel(toAdd);
        this.maintainSorted();
        this.handleTabChange(toAdd);
        this.forceSelectParcel(toAdd);
    }

    @Override
    public void editParcelCommand(ReadOnlyParcel parcelToEdit, ReadOnlyParcel editedParcel)
            throws DuplicateParcelException, ParcelNotFoundException {
        this.updateParcel(parcelToEdit, editedParcel);
        this.maintainSorted();
        this.handleTabChange(editedParcel);
        this.forceSelectParcel(editedParcel);
    }

    private void handleTabChange(ReadOnlyParcel targetParcel) {
        try {
            if (findStatus(targetParcel).equals(Status.getInstance("COMPLETED"))) {
                System.out.println("I think it's completed.");
                if (activeFilteredList.equals(filteredUndeliveredParcels)) {
                    System.out.println("But i'm at all parcels tab");
                    setActiveList(true);
                    EventsCenter.getInstance().post(new JumpToTabRequestEvent(INDEX_SECOND_TAB));
                }
            } else {
                System.out.println("I guess it's not completed");
                if (activeFilteredList.equals(filteredDeliveredParcels)) {
                    System.out.println("But I'm at completed tab");
                    setActiveList(false);
                    EventsCenter.getInstance().post(new JumpToTabRequestEvent(INDEX_FIRST_TAB));
                }
            }
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }

        System.out.println("IT'S HERE: " + findIndex(targetParcel));
    }

    private Status findStatus(ReadOnlyParcel target) {
        return target.getStatus();
    }

    /**
     * Method to retrieve the index of a given parcel in the active list.
     */
    private int findIndex(ReadOnlyParcel target) {
        return getActiveList().indexOf(target);
    }

    /*
    @Override
    public void reselect(ReadOnlyParcel parcel) {
        // With sorting, we lose our selected card. As such we have to reselect the
        // parcel that was previously selected. This leads to the need to have some way of
        // keeping track of which card had been previously selected. Hence the prevIndex
        // attribute in the ModelManager class and also it's corresponding to get and set it.
        // We first get the identity of the previously selected parcel.
        ReadOnlyParcel previous = getPrevSelectedParcel();
        // if the previous parcel belongs after the editedParcel, we just reselect the parcel
        // at the previous index because all the parcels get pushed down.
        forceSelect(Index.fromZeroBased(findIndex(previous)));
    }

    @Override
    public ReadOnlyParcel getPrevSelectedParcel() {
        return prevSelectedParcel;
    }

    @Override
    public void setPrevSelectedParcel(ReadOnlyParcel selectedParcel) {
        select();
        prevSelectedParcel = selectedParcel;
    }

    @Override
    public boolean hasSelected() {
        return selected;
    }

    @Override
    public void select() {
        selected = true;
    }

    @Override
    public void unselect() {
        selected = false;
    }
    */

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && filteredParcels.equals(other.filteredParcels)
                && filteredDeliveredParcels.equals(other.filteredDeliveredParcels)
                && filteredUndeliveredParcels.equals(other.filteredUndeliveredParcels);
    }
    //@@author

    public static Predicate<ReadOnlyParcel> getDeliveredPredicate() {
        return deliveredPredicate;
    }

}
