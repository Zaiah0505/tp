package seedu.iscam.model.meeting;

import javafx.collections.ObservableList;

/**
 * Unmodifiable view of a meeting list
 */
public interface ReadOnlyMeetingList {

    /**
     * Returns an unmodifiable view of the meetings list.
     * This list will not contain any duplicate meetings.
     */
    ObservableList<Meeting> getMeetingList();
}
