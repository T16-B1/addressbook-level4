package seedu.address.logic.commands;

import seedu.address.model.parcel.NameContainsKeywordsPredicate;

/**
 * Finds and lists all parcels in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all parcels whose receiver's names "
            + "contain any of the specified keywords (case-sensitive) and displays them as a list with "
            + "index numbers.\n" + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final NameContainsKeywordsPredicate predicate;

    public FindCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredParcelList(predicate);
        return new CommandResult(getMessageForParcelListShownSummary(model.getActiveList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && this.predicate.equals(((FindCommand) other).predicate)); // state check
    }
}
