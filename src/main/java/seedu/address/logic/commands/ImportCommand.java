package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.parcel.ReadOnlyParcel;

/**
 * Imports the parcels and tags of parcelsToAdd to the current AddressBook
 */
public class ImportCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds the data of a storage file stored in "
            + "data/dataImports/ directory into Ark.\n"
            + "Parameters: FILE (Must be a valid addressbook file stored in xml format)\n"
            + "Example: " + COMMAND_WORD + " addressbook.xml";

    public static final String MESSAGE_SUCCESS = "Summary: %1$d parcels added and %2$d duplicate parcels not added.\n"
            + "Parcels added: %3$s\nDuplicate Parcels: %4$s";
    public static final String MESSAGE_DUPLICATE_PARCELS = "All parcels in the imported save file will create "
            + "duplicate parcels";

    private final List<ReadOnlyParcel> parcelsToAdd;

    /**
     * Creates an ImportCommand to add the parcels in parcelList {@code ReadOnlyParcel}
     */
    public ImportCommand(List<ReadOnlyParcel> parcelList) {
        parcelsToAdd = parcelList;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);

        List<ReadOnlyParcel> addedParcels = new ArrayList<>(); // list of added parcels
        List<ReadOnlyParcel> duplicateParcels = new ArrayList<>(); // list of duplicate parcels that are not added
        model.addAllParcels(parcelsToAdd, addedParcels, duplicateParcels);

        // check if all parcels are duplicates
        boolean areAllParcelsDuplicates = (duplicateParcels.size() == parcelsToAdd.size());
        if (areAllParcelsDuplicates) {
            throw new CommandException(MESSAGE_DUPLICATE_PARCELS);
        }

        String addedParcelsString = ""; // formatted string of parcels added
        for (ReadOnlyParcel parcel : addedParcels) {
            addedParcelsString = addedParcelsString + "\n  " + parcel.toString();
        }

        String duplicatedParcelsString = (duplicateParcels.size() > 0) ? "" : "\n  (none)"; // string of duplicates
        for (ReadOnlyParcel parcel : duplicateParcels) {
            duplicatedParcelsString = duplicatedParcelsString + "\n  " + parcel.toString();
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, addedParcels.size(), duplicateParcels.size(),
                addedParcelsString, duplicatedParcelsString));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ImportCommand // instanceof handles nulls
                && hasSameParcels(parcelsToAdd, ((ImportCommand) other).parcelsToAdd));
    }

    /**
     * check if the elements of parcels and otherParcels have the same elements, disregarding order.
     */
    public boolean hasSameParcels(List<ReadOnlyParcel> parcels, List<ReadOnlyParcel> otherParcels) {
        // check # of parcels are equal
        if (parcels.size() != otherParcels.size()) {
            return false;
        }

        return parcels.containsAll(otherParcels);
    }
}
