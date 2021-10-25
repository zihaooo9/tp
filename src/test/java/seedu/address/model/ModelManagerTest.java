package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TUTORS;
import static seedu.address.model.Model.PREDICATE_SHOW_NO_PERSON;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.IDA;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Student;
import seedu.address.model.person.TagsContainTagPredicate;
import seedu.address.model.person.Tutor;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.TypicalPersons;

public class ModelManagerTest {
    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasTutor_nullTutor_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasTutor(null));
    }

    @Test
    public void hasStudent_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasStudent(null));
    }

    @Test
    public void hasTutor_tutorNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasTutor(ALICE));
    }

    @Test
    public void hasStudent_studentNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasStudent(DANIEL));
    }

    @Test
    public void hasTutor_tutorInAddressBook_returnsTrue() {
        modelManager.addTutor(ALICE);
        assertTrue(modelManager.hasTutor(ALICE));
    }

    @Test
    public void hasStudent_studentInAddressBook_returnsTrue() {
        modelManager.addStudent(DANIEL);
        assertTrue(modelManager.hasStudent(DANIEL));
    }

    @Test
    public void getFilteredTutorList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredTutorList().remove(
                INDEX_FIRST_PERSON.getZeroBased()));
    }

    @Test
    public void getFilteredStudentList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredStudentList().remove(
                INDEX_FIRST_PERSON.getZeroBased()));
    }

    @Test
    public void getMatchedTutorList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getMatchedTutorList().remove(
                INDEX_FIRST_PERSON.getZeroBased()));
    }

    @Test
    public void updateFilteredTutorList_modifyList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.updateFilteredTutorList(null));
    }

    @Test
    public void updateFilteredStudentList_modifyList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.updateFilteredStudentList(null));
    }

    @Test
    public void updateMatchedTutor_modifyList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.updateMatchedTutor(null, null));
    }

    @Test
    public void updateFilteredTutorList_modifyList_success() {
        ArrayList<String> ls = new ArrayList<>();
        ls.add("Alice");
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(ls);
        TypicalPersons.getTypicalTutors().stream().forEach(tutor -> modelManager.addTutor(tutor));
        AddressBook addressBook = new AddressBook();
        addressBook.addTutor(ALICE);
        FilteredList<Tutor> expectedTutorList = new FilteredList<>(addressBook.getTutorList());
        modelManager.updateFilteredTutorList(predicate);
        assertEquals(expectedTutorList, modelManager.getFilteredTutorList());
    }

    @Test
    public void updateFilteredStudentList_modifyList_success() {
        ArrayList<String> ls = new ArrayList<>();
        ls.add("Daniel");
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(ls);
        TypicalPersons.getTypicalStudents().stream().forEach(student -> modelManager.addStudent(student));
        AddressBook addressBook = new AddressBook();
        addressBook.addStudent(DANIEL);
        FilteredList<Student> expectedStudentList = new FilteredList<>(addressBook.getStudentList());
        modelManager.updateFilteredStudentList(predicate);
        assertEquals(expectedStudentList, modelManager.getFilteredStudentList());
    }

    @Test
    public void updateMatchedTutor_modifyList_success() {
        ArrayList<Tag> ls = new ArrayList<>();
        ls.addAll(DANIEL.getTags());
        TagsContainTagPredicate predicate = new TagsContainTagPredicate(ls);
        TypicalPersons.getTypicalTutors().stream().forEach(tutor -> modelManager.addTutor(tutor));
        AddressBook addressBook = new AddressBook();
        addressBook.addTutor(ALICE);
        FilteredList<Tutor> expectedTutorList = new FilteredList<>(addressBook.getTutorList());
        modelManager.updateMatchedTutor(predicate, ls);
        assertEquals(expectedTutorList, modelManager.getMatchedTutorList());
    }

    @Test
    public void updateMatchedTutor_emptyMatchedList_success() {
        ArrayList<Tag> ls = new ArrayList<>();
        ls.addAll(GEORGE.getTags());
        TagsContainTagPredicate predicate = new TagsContainTagPredicate(ls);
        TypicalPersons.getTypicalTutors().stream().forEach(tutor -> modelManager.addTutor(tutor));
        AddressBook addressBook = new AddressBook();
        FilteredList<Tutor> expectedTutorList = new FilteredList<>(addressBook.getTutorList());
        modelManager.updateMatchedTutor(predicate, ls);
        assertEquals(expectedTutorList, modelManager.getMatchedTutorList());
    }

    @Test
    public void updateMatchedTutor_modifyList_throwAssertionError() {
        ArrayList<Tag> ls = new ArrayList<>();
        ls.addAll(DANIEL.getTags());
        TagsContainTagPredicate predicate = new TagsContainTagPredicate(ls);
        TypicalPersons.getTypicalTutors().stream().forEach(tutor -> modelManager.addTutor(tutor));
        assertThrows(AssertionError.class, () -> modelManager.updateMatchedTutor(predicate, new ArrayList<>()));
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withTutor(ALICE).withStudent(DANIEL).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList (tutor) -> returns false
        String[] keywords = BENSON.getName().fullName.split("\\s+");
        modelManager.updateFilteredTutorList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // different filteredList (student) -> returns false
        keywords = IDA.getName().fullName.split("\\s+");
        modelManager.updateFilteredStudentList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTutorList(PREDICATE_SHOW_ALL_TUTORS);
        modelManager.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        modelManager.updateMatchedTutor(PREDICATE_SHOW_NO_PERSON, new ArrayList<>());

        Set<Tag> studentTag = DANIEL.getTags();
        List<Tag> ls = new ArrayList<>();
        studentTag.stream().forEach(tag -> ls.add(tag));
        modelManager.updateMatchedTutor(new TagsContainTagPredicate(ls), ls);
        assertNotEquals(modelManager, new ModelManager(addressBook, userPrefs));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTutorList(PREDICATE_SHOW_ALL_TUTORS);
        modelManager.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        modelManager.updateMatchedTutor(PREDICATE_SHOW_NO_PERSON, new ArrayList<>());

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }
}
