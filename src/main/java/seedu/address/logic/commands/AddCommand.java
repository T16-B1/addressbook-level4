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

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.parcel.Parcel;
import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.model.parcel.exceptions.DuplicateParcelException;

/**
 * Adds a parcel to the address book.
 */
public class AddCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a parcel to the address book. "
            + "Parameters: "
            + PREFIX_TRACKING_NUMBER + "TRACKING_NUMBER "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "[PHONE] "
            + PREFIX_EMAIL + "[EMAIL] "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_DELIVERY_DATE + "DELIVERYDATE "
            + "[" + PREFIX_STATUS + "STATUS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TRACKING_NUMBER + "RR999999999SG "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 S123123 "
            + PREFIX_DELIVERY_DATE + "25-12-2000 "
            + PREFIX_STATUS + "PENDING "
            + PREFIX_TAG + "FROZEN "
            + PREFIX_TAG + "heavy";

    public static final String MESSAGE_SUCCESS = "New parcel added: %1$s";
    public static final String MESSAGE_DUPLICATE_PARCEL = "This parcel already exists in the address book";

    private final Parcel toAdd;

    /**
     * Creates an AddCommand to add the specified {@code ReadOnlyParcel}
     */
    public AddCommand(ReadOnlyParcel parcel) {
        toAdd = new Parcel(parcel);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.addParcelCommand(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateParcelException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PARCEL);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
