package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELIVERY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRACKING_NUMBER;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PARCELS;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.parcel.Address;
import seedu.address.model.parcel.DeliveryDate;
import seedu.address.model.parcel.Email;
import seedu.address.model.parcel.Name;
import seedu.address.model.parcel.Parcel;
import seedu.address.model.parcel.Phone;
import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.model.parcel.Status;
import seedu.address.model.parcel.TrackingNumber;
import seedu.address.model.parcel.exceptions.DuplicateParcelException;
import seedu.address.model.parcel.exceptions.ParcelNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing parcel in the address book.
 */
public class EditCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the parcel identified "
            + "by the index number used in the last parcel listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_TRACKING_NUMBER + "TRACKING_NUMBER]"
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_DELIVERY_DATE + "DELIVERY_DATE] "
            + "[" + PREFIX_STATUS + "STATUS]"
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PARCEL_SUCCESS = "Edited Parcel: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PARCEL = "This parcel already exists in the address book.";

    private final Index index;
    private final EditParcelDescriptor editParcelDescriptor;

    /**
     * @param index of the parcel in the filtered parcel list to edit
     * @param editParcelDescriptor details to edit the parcel with
     */
    public EditCommand(Index index, EditParcelDescriptor editParcelDescriptor) {
        requireNonNull(index);
        requireNonNull(editParcelDescriptor);

        this.index = index;
        this.editParcelDescriptor = new EditParcelDescriptor(editParcelDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyParcel> lastShownList = model.getActiveList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PARCEL_DISPLAYED_INDEX);
        }

        ReadOnlyParcel parcelToEdit = lastShownList.get(index.getZeroBased());
        Parcel editedParcel = createEditedParcel(parcelToEdit, editParcelDescriptor);

        try {
            model.editParcelCommand(parcelToEdit, editedParcel);
        } catch (DuplicateParcelException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PARCEL);
        } catch (ParcelNotFoundException pnfe) {
            throw new AssertionError("The target parcel cannot be missing");
        }
        model.updateFilteredParcelList(PREDICATE_SHOW_ALL_PARCELS);
        return new CommandResult(String.format(MESSAGE_EDIT_PARCEL_SUCCESS, editedParcel));
    }

    /**
     * Creates and returns a {@code Parcel} with the details of {@code parcelToEdit}
     * edited with {@code editParcelDescriptor}.
     */
    private static Parcel createEditedParcel(ReadOnlyParcel parcelToEdit,
                                             EditParcelDescriptor editParcelDescriptor) {
        assert parcelToEdit != null;

        TrackingNumber updatedTrackingNumber = editParcelDescriptor.getTrackingNumber()
                .orElse(parcelToEdit.getTrackingNumber());
        Name updatedName = editParcelDescriptor.getName().orElse(parcelToEdit.getName());
        Phone updatedPhone = editParcelDescriptor.getPhone().orElse(parcelToEdit.getPhone());
        Email updatedEmail = editParcelDescriptor.getEmail().orElse(parcelToEdit.getEmail());
        Address updatedAddress = editParcelDescriptor.getAddress().orElse(parcelToEdit.getAddress());
        DeliveryDate updatedDeliveryDate = editParcelDescriptor.getDeliveryDate()
                                           .orElse(parcelToEdit.getDeliveryDate());
        Status updatedStatus = editParcelDescriptor.getStatus().orElse(parcelToEdit.getStatus());
        Set<Tag> updatedTags = editParcelDescriptor.getTags().orElse(parcelToEdit.getTags());

        return new Parcel(updatedTrackingNumber, updatedName, updatedPhone, updatedEmail, updatedAddress,
                          updatedDeliveryDate, updatedStatus, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editParcelDescriptor.equals(e.editParcelDescriptor);
    }

    /**
     * Stores the details to edit the parcel with. Each non-empty field value will replace the
     * corresponding field value of the parcel.
     */
    public static class EditParcelDescriptor {
        private TrackingNumber trackingNumber;
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private DeliveryDate deliveryDate;
        private Status status;
        private Set<Tag> tags;

        public EditParcelDescriptor() {}

        public EditParcelDescriptor(EditParcelDescriptor toCopy) {
            this.trackingNumber = toCopy.trackingNumber;
            this.name = toCopy.name;
            this.phone = toCopy.phone;
            this.email = toCopy.email;
            this.address = toCopy.address;
            this.deliveryDate = toCopy.deliveryDate;
            this.status = toCopy.status;
            this.tags = toCopy.tags;
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.trackingNumber, this.name, this.phone, this.email, this.address,
                    this.deliveryDate, this.status, this.tags);
        }

        public void setTrackingNumber(TrackingNumber trackingNumber) {
            this.trackingNumber = trackingNumber;
        }

        public Optional<TrackingNumber> getTrackingNumber() {
            return Optional.ofNullable(trackingNumber);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setDeliveryDate (DeliveryDate deliveryDate) {
            this.deliveryDate = deliveryDate;
        }

        public Optional<DeliveryDate> getDeliveryDate() {
            return Optional.ofNullable(deliveryDate);
        }

        public void setStatus (Status status) {
            this.status = status;
        }

        public Optional<Status> getStatus() {
            return Optional.ofNullable(status);
        }

        public void setTags(Set<Tag> tags) {
            this.tags = tags;
        }

        public Optional<Set<Tag>> getTags() {
            return Optional.ofNullable(tags);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditParcelDescriptor)) {
                return false;
            }

            // state check
            EditParcelDescriptor e = (EditParcelDescriptor) other;

            return getTrackingNumber().equals(e.getTrackingNumber())
                    && getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getDeliveryDate().equals(e.getDeliveryDate())
                    && getStatus().equals(e.getStatus())
                    && getTags().equals(e.getTags());
        }
    }

}
