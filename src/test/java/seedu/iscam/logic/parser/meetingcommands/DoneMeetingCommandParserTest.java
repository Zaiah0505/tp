package seedu.iscam.logic.parser.meetingcommands;

import static seedu.iscam.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.iscam.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.iscam.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.iscam.testutil.TypicalIndexes.INDEX_FIRST_ITEM;

import org.junit.jupiter.api.Test;

import seedu.iscam.logic.commands.DoneMeetingCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteMeetingCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteMeetingCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
class DoneMeetingCommandParserTest {
    private DoneMeetingCommandParser parser = new DoneMeetingCommandParser();

    @Test
    public void parse_validArgs_returnsDoneMeetingCommand() {
        assertParseSuccess(parser, "1", new DoneMeetingCommand(INDEX_FIRST_ITEM));
    }

    @Test
    public void parse_indexNotANumber_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_INDEX, DoneMeetingCommand.MESSAGE_USAGE));
    }
}