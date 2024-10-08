package parser;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import chatterboxexceptions.ChatterboxExceptions;
import command.AllTagsCommand;
import command.ByeCommand;
import command.Command;
import command.DeadlineCommand;
import command.DeleteCommand;
import command.EventCommand;
import command.FindCommand;
import command.FindTagCommand;
import command.InvalidCommand;
import command.ListCommand;
import command.MarkCommand;
import command.RemoveTagCommand;
import command.TagCommand;
import command.TodoCommand;
import command.UnmarkCommand;



/**
 * The Parser class provides methods to parse user input into commands and dates.
 */
public class Parser {

    private static final DateTimeFormatter DASHFORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm");
    private static final DateTimeFormatter SLASHFORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
    private static final DateTimeFormatter SINGLEDIGITDATESLASH = new DateTimeFormatterBuilder()
            .appendPattern("d/M/yyyy") // Single or double-digit day and month
            .toFormatter()
            .withResolverStyle(ResolverStyle.SMART);

    private static final DateTimeFormatter SINGLEDIGITDATEDASH = new DateTimeFormatterBuilder()
            .appendPattern("d-M-yyyy") // Single or double-digit day and month
            .toFormatter()
            .withResolverStyle(ResolverStyle.SMART);

    private static final DateTimeFormatter SINGLEDIGITDATETIMESLASH = new DateTimeFormatterBuilder()
            .appendPattern("d/M/yyyy") // Single or double-digit day and month
            .appendPattern(" HHmm") // Time in 24-hour format, without a separator
            .toFormatter()
            .withResolverStyle(ResolverStyle.SMART);

    private static final DateTimeFormatter SINGLEDIGITDATETIMEDASH = new DateTimeFormatterBuilder()
            .appendPattern("d-M-yyyy") // Single or double-digit day and month
            .appendPattern(" HHmm") // Time in 24-hour format, without a separator
            .toFormatter()
            .withResolverStyle(ResolverStyle.SMART);
    private static final DateTimeFormatter DASHONLYDATE = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter SLASHONLYDATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter PRINTDATEFORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy, HH:mm");
    private static final DateTimeFormatter[] DATE_TIME_FORMATTERS =
        new DateTimeFormatter[] {DASHFORMATTER, SLASHFORMATTER, PRINTDATEFORMATTER,
            SINGLEDIGITDATETIMESLASH, SINGLEDIGITDATETIMEDASH};
    private static final DateTimeFormatter[] DATE_ONLY_FORMATTERS =
        new DateTimeFormatter[] {DASHONLYDATE, SLASHONLYDATE, SINGLEDIGITDATESLASH, SINGLEDIGITDATEDASH};


    /**
     * Parses a string for possible DateTime objects.
     *
     * @param dateTimeString String that could contain a date time.
     * @return LocalDateTime representing time in string or null if no valid date time found.
     *
     */

    public LocalDateTime parseDateTime(String dateTimeString) {

        dateTimeString = dateTimeString.trim();

        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {

                return LocalDateTime.parse(dateTimeString, formatter);
            } catch (DateTimeParseException e) {
                //do nothing, try next one
            }
        }
        for (DateTimeFormatter formatter : DATE_ONLY_FORMATTERS) {
            try {
                return LocalDate.parse(dateTimeString, formatter).atStartOfDay();
            } catch (DateTimeParseException e) {
                //do nothing try next one
            }
        }
        //if no matching format is found
        return null;
    }



    /**
     * Parses a string of text to check if it has a valid command.
     *
     * @param input the input to be parsed.
     * @return corresponding Command object.
     */
    public Command parseCommandType(String input) {
        String text = input.trim().toLowerCase();
        if (text.startsWith("bye")) {
            return new ByeCommand();
        } else if (text.startsWith("list")) {
            return new ListCommand();
        } else if (text.startsWith("mark")) {
            return new MarkCommand();
        } else if (text.startsWith("unmark")) {
            return new UnmarkCommand();
        } else if (text.startsWith("todo")) {
            return new TodoCommand();
        } else if (text.startsWith("deadline")) {
            return new DeadlineCommand();
        } else if (text.startsWith("event")) {
            return new EventCommand();
        } else if (text.startsWith("delete")) {
            return new DeleteCommand();
        } else if (text.startsWith("findtag")) {
            return new FindTagCommand();
        } else if (text.startsWith("find")) {
            return new FindCommand();
        } else if (text.startsWith("tag")) {
            return new TagCommand();
        } else if (text.startsWith("alltags")) {
            return new AllTagsCommand();
        } else if (text.startsWith("removetag")) {
            return new RemoveTagCommand();
        } else {
            return new InvalidCommand();

        }
    }

    /**
     * Extracts the integer index for mark and unmark commands.
     *
     * @param input the input of format mark/unmark {int}.
     * @return the integer index in the input string.
     * @throws ChatterboxExceptions.ChatterBoxInvalidInput if no number or a negative number is found.
     */
    public int extractNum(String input) throws ChatterboxExceptions.ChatterBoxInvalidInput {
        assert input != null;

        int length = input.length();
        assert length > 0;
        StringBuilder numberBuild = new StringBuilder();
        Boolean isNegative = false;
        for (int i = length - 1; i >= 0; i--) {
            char currentChar = input.charAt(i);
            if (Character.isDigit(currentChar)) {
                numberBuild.insert(0, currentChar);
            } else if (currentChar == '-') {
                isNegative = true;
                break;
            } else {
                break;
            }
        }
        if (numberBuild.length() == 0) {
            throw new ChatterboxExceptions.ChatterBoxInvalidInput("No number found");
        }
        if (isNegative) {
            throw new ChatterboxExceptions.ChatterBoxInvalidInput("Negative number found");
        }
        return Integer.parseInt(numberBuild.toString());
    }

    /**
     * Parses a string to obtain the description for a todo command.
     *
     * @param desc the input of format todo {text}.
     * @return the substring of the task description.
     */
    public String parseTodo(String desc) {
        return desc.substring(4).trim();
    }

    /**
     * Parses a string to obtain text for deadline parameters.
     *
     * @param desc the input of format deadline [text] /by [text].
     * @return a String array with the description at index 0 and the deadline at index 1.
     * @throws ChatterboxExceptions.ChatterBoxMissingParameter if no deadline date is found.
     */
    public String[] parseDeadline(String desc) throws ChatterboxExceptions.ChatterBoxMissingParameter {


        int endDate = desc.indexOf("/by");
        if (endDate < 0) {
            throw new ChatterboxExceptions.ChatterBoxMissingParameter("Deadline date");
        }
        StringBuilder plainDesc = new StringBuilder();
        StringBuilder deadline = new StringBuilder();
        boolean isByCommandPresent = false;
        for (int i = 8; i < desc.length(); i++) {

            if (i < endDate) {
                plainDesc.append(desc.charAt(i));
                continue;

            }
            if (desc.charAt(i) == '/' && !isByCommandPresent) {
                i += 2;
                isByCommandPresent = true;
                continue;
            }
            deadline.append(desc.charAt(i));

        }
        return new String[] {plainDesc.toString().trim(), deadline.toString().trim()};
    }


    /**
     * Parses the event string for the description, from, and to time strings.
     *
     * @param desc the input of format text /from text /to text.
     * @return a String array with the description at index 0, the from time at index 1,
     * and the to time at index 2.
     * @throws ChatterboxExceptions.ChatterBoxMissingParameter if any parameters are not detected.
     */
    public String[] parseEvent(String desc) throws ChatterboxExceptions.ChatterBoxMissingParameter {


        int fromStart = desc.indexOf("/from");
        if (fromStart < 0) {
            throw new ChatterboxExceptions.ChatterBoxMissingParameter("Event Start Date");
        }
        int toStart = desc.indexOf("/to");
        if (toStart < 0) {
            throw new ChatterboxExceptions.ChatterBoxMissingParameter("Event End Date");
        }
        if (toStart < fromStart) {
            throw new ChatterboxExceptions.ChatterBoxMissingParameter("Wrong argument order");
        }
        StringBuilder plainDesc = new StringBuilder();
        StringBuilder startDate = new StringBuilder();
        StringBuilder endDate = new StringBuilder();
        boolean isFromCommandPresent = false;
        boolean isToCommandPresent = false;
        //start with 5 to go past event
        for (int i = 5; i < desc.length(); i++) {
            if (i < fromStart) {
                plainDesc.append(desc.charAt(i));
                continue;
            }
            if (i < toStart) {
                if (desc.charAt(i) == '/' && !isFromCommandPresent) {
                    i += 4;
                    isFromCommandPresent = true;
                    continue;
                }
                startDate.append(desc.charAt(i));
                continue;
            }


            if (desc.charAt(i) == '/' && !isToCommandPresent) {
                i += 2;
                isToCommandPresent = true;
                continue;
            }
            endDate.append(desc.charAt(i));
        }

        return new String[] {plainDesc.toString().trim(), startDate.toString().trim(), endDate.toString().trim()};
    }

    /**
     * Parses the tag name from a findtag command.
     *
     * @param desc the input of format findtag {text}.
     * @return the text used to search for the tag name.
     */
    public String findTagParseTagName(String desc) {
        return desc.substring(7).trim();
    }



    /**
     * Parses the tag text from a tag command.
     *
     * @param desc the input of format tag /i {index} /t {text}.
     * @return the text after /t.
     * @throws ChatterboxExceptions.ChatterBoxMissingParameter if no tag text is found.
     */
    public String tagCommandParseTagName(String desc) throws ChatterboxExceptions.ChatterBoxMissingParameter {

        int start = desc.indexOf("/t");
        if (start < 0) {
            throw new ChatterboxExceptions.ChatterBoxMissingParameter("Tag text missing");
        }

        return desc.substring(start + 2).trim();
    }

    /**
     * Parses the tag index from a tag command.
     *
     * @param desc the input of format tag /i {index} /t {text}.
     * @return the index after /i.
     * @throws ChatterboxExceptions.ChatterBoxMissingParameter if no index is found.
     */
    public int tagCommandParseTaskIndex(String desc) throws ChatterboxExceptions.ChatterBoxMissingParameter {
        int start = desc.indexOf("/i");
        int end = desc.indexOf("/t");
        if (start < 0 || end < 0) {
            throw new ChatterboxExceptions.ChatterBoxMissingParameter("tag / index missing");
        }
        String sub = desc.substring(start + 2, end).trim();
        if (sub.isEmpty() || !sub.matches("\\d+")) {
            throw new ChatterboxExceptions.ChatterBoxMissingParameter("missing index");
        }

        return Integer.parseInt(sub.trim());

    }


    /**
     * Parses for keywords in a find command.
     *
     * @param command the input starting with find.
     * @return the keywords after find.
     */
    public String parseFind(String command) {
        return command.substring(4);
    }


    /**
     * Parses for the index in a delete command.
     *
     * @param desc the input of format removetag /i {index} /t {text}.
     * @return the index of the task to delete the tag from.
     * @throws ChatterboxExceptions.ChatterBoxMissingParameter if the index is missing or in the wrong order.
     */
    public int parseRemoveTagIndex(String desc) throws ChatterboxExceptions.ChatterBoxMissingParameter {
        int start = desc.indexOf("/i");
        int end = desc.indexOf("/t");
        if (start > end) {
            throw new ChatterboxExceptions.ChatterBoxMissingParameter("Wrong argument order");
        }

        if (start < 0 || end < 0) {
            throw new ChatterboxExceptions.ChatterBoxMissingParameter("Tag input missing");
        }

        String sub = desc.substring(start + 2, end).trim();
        if (sub.isEmpty() || !sub.matches("\\d+")) {
            throw new ChatterboxExceptions.ChatterBoxMissingParameter("missing tag index");
        }

        return Integer.parseInt(sub);
    }

    /**
     * Parses for the tag name in a remove tag command.
     *
     * @param desc the input of format removetag /i {index} /t {text}.
     * @return the tag name.
     * @throws ChatterboxExceptions.ChatterBoxMissingParameter if no tag text is found.
     */
    public String parseRemoveTagName(String desc) throws ChatterboxExceptions.ChatterBoxMissingParameter {
        int start = desc.indexOf("/t");
        if (start < 0) {
            throw new ChatterboxExceptions.ChatterBoxMissingParameter("Tag text missing");
        }
        return desc.substring(start + 2).trim();
    }
    /**
     * Returns a DateTimeFormatter used for printing LocalDateTime objects
     *
     * @return standard formatter for dates.
     */
    public static DateTimeFormatter getPrintDateFormatter() {

        return PRINTDATEFORMATTER;
    }

}
