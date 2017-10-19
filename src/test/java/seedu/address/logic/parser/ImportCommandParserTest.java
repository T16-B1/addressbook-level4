package seedu.address.logic.parser;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_FILE_NOT_FOUND;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_DATA;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ImportCommandParserTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseImportFilePath_invalidInput_throwsIllegalValueException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE)
                + "\nMore Info: " + MESSAGE_FILE_NOT_FOUND);
        new ImportCommandParser().parse("Missing.xml");
    }

    @Test
    public void parseImportFilePath_notXmlFormat_throwsIllegalValueException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE)
                + "\nMore Info: " + String.format(MESSAGE_INVALID_DATA,
                "./data/importData/NotXmlFormatAddressBook.xml"));

        new ImportCommandParser().parse("NotXmlFormatAddressBook.xml");
    }

    @Test
    public void parseImportFilePath_validInput_success() throws Exception {
        ImportCommand importCommand = new ImportCommandParser().parse("validAddressBook.xml");
        assertTrue(importCommand instanceof ImportCommand);
    }
}
