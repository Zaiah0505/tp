package seedu.iscam.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.iscam.logic.commands.CommandTestUtil.DESC_AMY_MEETING;
import static seedu.iscam.logic.commands.CommandTestUtil.DESC_BOB_MEETING;
import static seedu.iscam.logic.commands.CommandTestUtil.VALID_LOCATION_BOB;
import static seedu.iscam.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.iscam.logic.commands.CommandTestUtil.VALID_TAG_URGENT;
import static seedu.iscam.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.iscam.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.iscam.logic.commands.CommandTestUtil.showMeetingAtIndex;
import static seedu.iscam.testutil.TypicalClients.getTypicalClientBook;
import static seedu.iscam.testutil.TypicalIndexes.INDEX_FIRST_CLIENT;
import static seedu.iscam.testutil.TypicalIndexes.INDEX_SECOND_MEETING;
import static seedu.iscam.testutil.TypicalMeetings.getTypicalMeetingBook;
import static seedu.iscam.testutil.TypicalIndexes.INDEX_FIRST_MEETING;

import org.junit.jupiter.api.Test;

import seedu.iscam.commons.core.Messages;
import seedu.iscam.commons.core.index.Index;
import seedu.iscam.logic.commands.EditMeetingCommand.EditMeetingDescriptor;
import seedu.iscam.model.Model;
import seedu.iscam.model.ModelManager;
import seedu.iscam.model.meeting.Meeting;
import seedu.iscam.model.user.UserPrefs;
import seedu.iscam.model.util.clientbook.ClientBook;
import seedu.iscam.model.util.meetingbook.MeetingBook;
import seedu.iscam.testutil.EditMeetingDescriptorBuilder;
import seedu.iscam.testutil.MeetingBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditMeetingCommand.
 */
class EditMeetingCommandTest {
    private Model model = new ModelManager(getTypicalClientBook(), getTypicalMeetingBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Meeting editedMeeting = new MeetingBuilder().build();
        EditMeetingDescriptor descriptor = new EditMeetingDescriptorBuilder(editedMeeting).build();
        EditMeetingCommand editMeetingCommand = new EditMeetingCommand(INDEX_FIRST_MEETING, descriptor);

        String expectedMessage = String.format(EditMeetingCommand.MESSAGE_EDIT_MEETING_SUCCESS, editedMeeting);

        Model expectedModel = new ModelManager(new ClientBook(model.getClientBook()),
                new MeetingBook(model.getMeetingBook()), new UserPrefs());
        expectedModel.setMeeting(model.getFilteredMeetingList().get(0), editedMeeting);

        assertCommandSuccess(editMeetingCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Meeting firstMeeting = model.getFilteredMeetingList().get(INDEX_SECOND_MEETING.getZeroBased());

        assertFalse(firstMeeting.getStatus().isComplete(), "Test case meeting cannot be complete");

        MeetingBuilder meetingInList = new MeetingBuilder(firstMeeting);
        Meeting editedMeeting = meetingInList.withName(VALID_NAME_BOB).withLocation(VALID_LOCATION_BOB)
                .withTags(VALID_TAG_URGENT).build();

        EditMeetingDescriptor descriptor = new EditMeetingDescriptorBuilder().withName(VALID_NAME_BOB)
                .withLocation(VALID_LOCATION_BOB)
                .withTags(VALID_TAG_URGENT).build();
        EditMeetingCommand editMeetingCommand = new EditMeetingCommand(INDEX_SECOND_MEETING, descriptor);

        String expectedMessage = String.format(EditMeetingCommand.MESSAGE_EDIT_MEETING_SUCCESS, editedMeeting);

        Model expectedModel = new ModelManager(new ClientBook(model.getClientBook()),
                new MeetingBook(model.getMeetingBook()), new UserPrefs());
        expectedModel.setMeeting(firstMeeting, editedMeeting);

        assertCommandSuccess(editMeetingCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditMeetingCommand editMeetingCommand =
                new EditMeetingCommand(INDEX_FIRST_MEETING, new EditMeetingDescriptor());

        assertCommandFailure(editMeetingCommand, model, EditMeetingCommand.MESSAGE_NO_CHANGES);
    }

    @Test
    public void execute_filteredList_success() {
        showMeetingAtIndex(model, INDEX_FIRST_MEETING);

        Meeting meetingInFilteredList = model.getFilteredMeetingList().get(INDEX_FIRST_CLIENT.getZeroBased());
        Meeting editedMeeting = new MeetingBuilder(meetingInFilteredList).withName(VALID_NAME_BOB).build();
        EditMeetingCommand editMeetingCommand = new EditMeetingCommand(INDEX_FIRST_CLIENT,
                new EditMeetingDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditMeetingCommand.MESSAGE_EDIT_MEETING_SUCCESS, editedMeeting);

        Model expectedModel = new ModelManager(new ClientBook(model.getClientBook()),
                new MeetingBook(model.getMeetingBook()), new UserPrefs());
        expectedModel.setMeeting(model.getFilteredMeetingList().get(0), editedMeeting);

        assertCommandSuccess(editMeetingCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_completedMeetingUnfilteredList_failure() {
        Index indexLastMeeting = Index.fromOneBased(model.getFilteredMeetingList().size());
        Meeting lastMeeting = model.getFilteredMeetingList().get(indexLastMeeting.getZeroBased());
        assertTrue(lastMeeting.getStatus().isComplete(), "Test case meeting must be complete");

        EditMeetingDescriptor descriptor = new EditMeetingDescriptorBuilder().build();
        EditMeetingCommand editMeetingCommand = new EditMeetingCommand(indexLastMeeting, descriptor);

        assertCommandFailure(editMeetingCommand, model, EditMeetingCommand.MESSAGE_NOT_ALLOWED);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of iscam book
     */
    @Test
    public void execute_invalidClientMeetingIndexFilteredList_failure() {
        showMeetingAtIndex(model, INDEX_FIRST_MEETING);
        Index outOfBoundIndex = INDEX_SECOND_MEETING;
        // ensures that outOfBoundIndex is still in bounds of iscam book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getMeetingBook().getMeetingList().size());

        EditMeetingCommand editMeetingCommand = new EditMeetingCommand(outOfBoundIndex,
                new EditMeetingDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editMeetingCommand, model, Messages.MESSAGE_INVALID_MEETING_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditMeetingCommand standardCommand = new EditMeetingCommand(INDEX_FIRST_MEETING, DESC_AMY_MEETING);

        // same values -> returns true
        EditMeetingDescriptor copyDescriptor = new EditMeetingDescriptor(DESC_AMY_MEETING);
        EditMeetingCommand commandWithSameValues = new EditMeetingCommand(INDEX_FIRST_MEETING, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditMeetingCommand(INDEX_SECOND_MEETING, DESC_AMY_MEETING)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditMeetingCommand(INDEX_FIRST_MEETING, DESC_BOB_MEETING)));
    }
}