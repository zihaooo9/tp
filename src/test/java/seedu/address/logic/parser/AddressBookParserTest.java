package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.LETTER_DESC_STUDENT;
import static seedu.address.logic.commands.CommandTestUtil.LETTER_DESC_TUTOR;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditStudentDescriptor;
import seedu.address.logic.commands.EditCommand.EditTutorDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Student;
import seedu.address.model.person.Tutor;
import seedu.address.testutil.EditStudentDescriptorBuilder;
import seedu.address.testutil.EditTutorDescriptorBuilder;
import seedu.address.testutil.StudentBuilder;
import seedu.address.testutil.StudentUtil;
import seedu.address.testutil.TutorBuilder;
import seedu.address.testutil.TutorUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Tutor tutor = new TutorBuilder().build();
        Student student = new StudentBuilder().build();

        AddCommand commandTutor = (AddCommand) parser.parseCommand(TutorUtil.getAddCommand(tutor));
        AddCommand commandStudent = (AddCommand) parser.parseCommand(StudentUtil.getAddCommand(student));

        assertEquals(new AddCommand(tutor, PersonType.TUTOR), commandTutor);
        assertEquals(new AddCommand(student, PersonType.STUDENT), commandStudent);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand commandTutor = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + LETTER_DESC_TUTOR + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON, PersonType.TUTOR), commandTutor);
        DeleteCommand commandStudent = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + LETTER_DESC_STUDENT + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON, PersonType.STUDENT), commandStudent);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Tutor tutor = new TutorBuilder().build();
        Student student = new StudentBuilder().build();

        EditTutorDescriptor tutorDescriptor = new EditTutorDescriptorBuilder(tutor).build();
        EditStudentDescriptor studentDescriptor = new EditStudentDescriptorBuilder(student).build();

        EditCommand commandTutor = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD
                + LETTER_DESC_TUTOR + " " + INDEX_FIRST_PERSON.getOneBased() + " "
                + TutorUtil.getEditTutorDescriptorDetails(tutorDescriptor));
        EditCommand commandStudent = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD
                + LETTER_DESC_STUDENT + " " + INDEX_FIRST_PERSON.getOneBased() + " "
                + StudentUtil.getEditStudentDescriptorDetails(studentDescriptor));

        assertEquals(new EditCommand(INDEX_FIRST_PERSON, tutorDescriptor, PersonType.TUTOR), commandTutor);
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, studentDescriptor, PersonType.STUDENT), commandStudent);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find_student() throws Exception {
        List<String> keywords = List.of("foo");

        // Test COMMAND_WORD
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + LETTER_DESC_STUDENT + " n/" + String.join(" ", keywords));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords), PersonType.STUDENT), command);

        // Test COMMAND_ALIAS
        command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_ALIAS + LETTER_DESC_STUDENT + " n/" + String.join(" ", keywords));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords), PersonType.STUDENT), command);
    }

    @Test
    public void parseCommand_find_tutor() throws Exception {
        List<String> keywords = List.of("foo");

        // Test COMMAND_WORD
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + LETTER_DESC_TUTOR + " n/" + String.join(" ", keywords));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords), PersonType.TUTOR), command);

        // Test COMMAND_ALIAS
        command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_ALIAS + LETTER_DESC_TUTOR + " n/" + String.join(" ", keywords));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords), PersonType.TUTOR), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
