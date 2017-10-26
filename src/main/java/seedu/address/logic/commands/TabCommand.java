package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.JumpToTabRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;

import seedu.address.commons.core.index.Index;

/**
 * Changes the remark of an existing person in the address book.
 */
public class TabCommand extends Command {
    public static final String COMMAND_WORD = "tab";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": changed the tab displayed in the parcel list panel "
            + "Parameters: INDEX (must be a positive integer) "
            + "Example: " + COMMAND_WORD + " 1 ";

    public static final String MESSAGE_SELECT_TAB_SUCCESS = "Selected Tab: %1$s";

    public static final String MESSAGE_INVALID_TAB_INDEX = "Invalid Tab Number";

    private final Index targetIndex;

    /**
     * @param targetIndex of the tab in the TabPane to switch to
     */
    public TabCommand(Index targetIndex) { this.targetIndex = targetIndex; }

    @Override
    public CommandResult execute() throws CommandException {

        int NUM_TABS = 2;

        if (targetIndex.getZeroBased() >= NUM_TABS) {
            throw new CommandException(MESSAGE_INVALID_TAB_INDEX);
        }

        EventsCenter.getInstance().post(new JumpToTabRequestEvent(targetIndex));
        return new CommandResult(String.format(MESSAGE_SELECT_TAB_SUCCESS, targetIndex.getZeroBased()));
}
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TabCommand // instanceof handles nulls
                && this.targetIndex.equals(((TabCommand) other).targetIndex)); // state check
    }
}
