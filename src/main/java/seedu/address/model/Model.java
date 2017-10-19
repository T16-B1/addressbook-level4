package seedu.address.model;

import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.model.parcel.exceptions.DuplicateParcelException;
import seedu.address.model.parcel.exceptions.ParcelNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<ReadOnlyParcel> PREDICATE_SHOW_ALL_PARCELS = unused -> true;

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

    /**
     * Adds the given parcel
     */
    void addParcel(ReadOnlyParcel parcel) throws DuplicateParcelException;

    /**
     * Adds all Parcel objects in parcels to the AddressBook
     * @param parcels list of parcels to add
     * @param parcelsAdded parcels that are added without causing duplicates
     * @param duplicateParcels parcels that are not added because doing so will cause duplicates
     */
    void addAllParcels(List<ReadOnlyParcel> parcels, List<ReadOnlyParcel> parcelsAdded, List<ReadOnlyParcel>
            duplicateParcels);

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

    /**
     * Updates the filter of the filtered parcel list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredParcelList(Predicate<ReadOnlyParcel> predicate);
}


