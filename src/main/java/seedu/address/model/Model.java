package seedu.address.model;

import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;

import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.model.parcel.exceptions.DuplicateParcelException;
import seedu.address.model.parcel.exceptions.ParcelNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagInternalErrorException;
import seedu.address.model.tag.exceptions.TagNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<ReadOnlyParcel> PREDICATE_SHOW_ALL_PARCELS = unused -> true;

    //@@author kennard123661
    void setActiveList(boolean isDelivered);
    //@@author

    /**
     * Clears existing backing model and replaces with the provided new data.
     */
    void resetData(ReadOnlyAddressBook newData);

    /**
     * Returns the AddressBook
     */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Deletes the given parcel.
     */
    void deleteParcel(ReadOnlyParcel target) throws ParcelNotFoundException;

    //@@author fustilio
    /**
     * Deletes the given tag from every parcel.
     */
    void deleteTag(Tag target) throws TagNotFoundException, TagInternalErrorException;
    //@@author

    /**
     * Adds the given parcel
     */
    void addParcel(ReadOnlyParcel parcel) throws DuplicateParcelException;

    //@@author kennard123661
    /**
     * Adds all Parcel objects in parcels to the AddressBook
     * @param parcels list of parcels to add
     * @param parcelsAdded parcels that are added without causing duplicates
     * @param duplicateParcels parcels that are not added because doing so will cause duplicates
     */
    void addAllParcels(List<ReadOnlyParcel> parcels, List<ReadOnlyParcel> parcelsAdded, List<ReadOnlyParcel>
            duplicateParcels);
    //@@author

    /**
     * Replaces the given parcel {@code target} with {@code editedParcel}.
     *
     * @throws DuplicateParcelException if updating the parcel's details causes the parcel to be equivalent to
     *                                  another existing parcel in the list.
     * @throws ParcelNotFoundException  if {@code target} could not be found in the list.
     */
    void updateParcel(ReadOnlyParcel target, ReadOnlyParcel editedParcel)
            throws DuplicateParcelException, ParcelNotFoundException;

    /**
     * Returns an unmodifiable view of the filtered parcel list
     */
    ObservableList<ReadOnlyParcel> getFilteredParcelList();

    //@@author kennard123661
    /**
     * Returns an unmodifiable view of the filtered parcel list
     */
    ObservableList<ReadOnlyParcel> getFilteredDeliveredParcelList();

    ObservableList<ReadOnlyParcel> getActiveList();

    /**
     * Returns an unmodifiable view of the filtered parcel list
     */
    ObservableList<ReadOnlyParcel> getFilteredUndeliveredParcelList();
    //@@author

    /**
     * Updates the filter of the filtered parcel list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredParcelList(Predicate<ReadOnlyParcel> predicate);

    //@@author fustilio
    /**
     * Method to sort the lists of addresses by delivery date with the earliest date in front
     */
    void maintainSorted();

    /**
     * Method to force the model to select a card without using the select command.
     */
    void forceSelect(Index target);


    /**
     * Method to force the model to select a card without using the select command.
     */
    void forceSelectParcel(ReadOnlyParcel target);

    void setTabIndex(Index index);

    Index getTabIndex();

    void addParcelCommand(ReadOnlyParcel parcel) throws DuplicateParcelException;

    void editParcelCommand(ReadOnlyParcel parcelToEdit, ReadOnlyParcel editedParce)
            throws DuplicateParcelException, ParcelNotFoundException;

    /*
    void reselect(ReadOnlyParcel parcel);

    void setPrevSelectedParcel(ReadOnlyParcel parcel);

    boolean hasSelected();

    void select();

    void unselect();

    ReadOnlyParcel getPrevSelectedParcel();
    */
    //@@author
}


