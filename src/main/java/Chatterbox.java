import java.time.DateTimeException;
import java.util.*;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
public class Chatterbox {



    public static class ChatterBoxError extends Exception {
        public ChatterBoxError (String message) {
            super(message);
        }
    }
    public static class ChatterBoxNoInput extends ChatterBoxError {
        public ChatterBoxNoInput (String message) {
            super(message);
        }
    }

    public static class ChatterBoxUnknownCommand extends ChatterBoxError {
        public ChatterBoxUnknownCommand (String message) {
            super(message);
        }
    }

    public static class ChatterBoxMissingParameter extends ChatterBoxError {
        public ChatterBoxMissingParameter(String para) {
            super("Missing parameter: " + para);
        }
    }
    public static void checkMessage(String msg) throws ChatterBoxUnknownCommand{
        throw new ChatterBoxUnknownCommand("Error: Unknown command");
    }



    /**
     * Class UI used to handle the printing and formatting of text in the UI
     */

    protected static class TaskList {
        private final ArrayList<Task> userTasks;

        public TaskList(ArrayList <Task> userTasks) {
            this.userTasks = userTasks;
        }

        /**
         * used to retrieve the users task list
         * @return an ArrayList<Task>
         */
        public ArrayList<Task> getTasks() {
            return userTasks;
        }

        /**
         * Marks task at index to be complete
         * @param index of task to be marked complete
         * @return returns the task that was marked
         */
        public Task markTask(int index) {
            userTasks.get(index).setStatus(true);
            return userTasks.get(index);
        }

        /**
         * Marks task at index to be not complete
         * @param index to be mark incomplete
         * @return the task that was unmarked
         */
        public Task unmarkTask(int index) {
            userTasks.get(index).setStatus(false);
            return userTasks.get(index);
        }

        public Todo addTodo(String desc) throws ChatterBoxNoInput{
            Todo nextTodo = new Todo(desc);
            userTasks.add(nextTodo);
            return nextTodo;
        }

        /**
         * Gets the task at index
         * @param index
         * @return Task at index
         */
        public Task getTask(int index) {
            return userTasks.get(index);
        }

        /**
         * returns the description of a task
         * @param index
         * @return
         */
        public String getTaskDescription(int index) {
            return userTasks.get(index).getDescription();
        }

        /**
         * Get size of task list
         * @return size of task list
         */
        public int size() {
            return userTasks.size();
        }


    }
    protected static class UI {
        private final static String LINE_SEPARATOR = "____________________________________________________________";
        private final static String BOTNAME = "Chatterbox";


        private static final DateTimeFormatter PRINTDATEFORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy, HH:mm");
        public UI() {

        }

        /**
         * greeting() used to display the default greeting when running the bot
         * @return string format of default greeting
         */
        public void greeting() {
            System.out.println(String.format("""
____________________________________________________________
 Hello! I'm %s
 What can I do for you?""", Chatterbox.BOTNAME));
        }

        /**
         * Displays the default goodbye message
         *
         */
        public void goodBye() {
            System.out.println("""
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
""");
        }



        /** Returns the default line separator used
         *
         * @return a line separator
         */
        protected static String getLineseperator() {
            return UI.LINE_SEPARATOR;
        }

        /**
         * Takes in the current List of Task objects and prints them in list format
         * @param userList is a List of Tasks
         *
         */
        protected void displayList(ArrayList <Task> userList) {
            System.out.println(UI.getLineseperator());
            System.out.println("Current Tasks in List: ");
            for (int i = 0; i < userList.size(); i++) {
                System.out.println(String.format(i + 1 + ". " + "[%s][%s] %s", userList.get(i).getTaskSymbol(), userList.get(i).getStatus() ? "X" : " ", userList.get(i).getDescription()));
            }

        }

        protected void displayTaskDescription(Task task) {
            System.out.println(task.getDescription());
        }

        /**
         * Displays the text for marking task as done
         * @param task marked done
         */
        protected void markMsg(Task task) {
            System.out.println(UI.getLineseperator());
            System.out.println("Marked Task as done");
            System.out.println(task.getDescription());
        }

        /**
         * Displays text for unmarking a task
         * @param task marked as undone
         */
        protected void unmarkMsg(Task task) {
            System.out.println(UI.getLineseperator());
            System.out.println("Marked Task as undone");
            System.out.println(task.getDescription());
        }

        protected void addTaskMsg(String type, int size) {
            System.out.println(UI.getLineseperator());
            System.out.println(String.format("Added %s to Tasks", type));
            System.out.println(String.format("Currently %d Tasks in List", size));
        }


    }

    /**
     * Handles the storage of Task history
     */
    private static class Storage {
        private String HISTFILE = Paths.get(System.getProperty("user.dir"),"data" , "command1.txt").toString();


        public Storage() {

        }

        /**
         * Initializes with path to txt file containing commands
         * @param filePath path to a file with command inputs
         */
        public Storage(String filePath) {
            this.HISTFILE = filePath;
        }


        public ArrayList<Task> load() throws FileNotFoundException{
            File f = new File(this.HISTFILE);
            Scanner s = new Scanner(f);
            ArrayList <Task> loadedTasks = new ArrayList<>();
            while (s.hasNext()) {
                String nextLine = s.nextLine();
                //parse the Line for task
                char type = nextLine.charAt(0);
                


            }
        }


        /**
         * Used to check for directory used to store data and create if not present,
         * @param None
         */
        private static void checkDirectory() {

            try {
                Files.createDirectories(Paths.get(System.getProperty("user.dir"), "data"));
            } catch (IOException e) {
                System.out.println("Error creating data directory: " + e.getMessage());
            }
        }

        /** Saves the tasks to a file based on the userList, creates a new directory data
         *
         * @param userList contains a list of Task objects
         */
        private  void saveHistory(ArrayList<Task> userList) {



            checkDirectory();

            File file = new File(HISTFILE);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter writer = new FileWriter(file);

                StringBuilder history = new StringBuilder();
                for (int i = 0; i < userList.size(); i++) {

                    Task currentTask = userList.get(i);
//                System.out.println(currentTask.getDescription());
                    String taskStr = String.format("%s | %s | %s", currentTask.getTaskSymbol(), currentTask.getStatus()? "X" : " ", currentTask.getDescription());
                    history.append(taskStr);
                    history.append(System.lineSeparator());
                }
                writer.write(history.toString());
                writer.close();

            } catch (IOException e) {
                System.out.println("Error has occurred " + e.getMessage());

            }
        }

    }


    private static final DateTimeFormatter DASHFORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm");
    private static final DateTimeFormatter SLASHFORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");

    private static final DateTimeFormatter DASHONLYDATE = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter SLASHONLYDATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter[] DATE_TIME_FORMATTERS = new DateTimeFormatter[] {DASHFORMATTER, SLASHFORMATTER};
    private static final DateTimeFormatter[] DATE_ONLY_FORMATTERS = new DateTimeFormatter[] {DASHONLYDATE, SLASHONLYDATE};
    private static final DateTimeFormatter PRINTDATEFORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy, HH:mm");



    private static LocalDateTime parseDateTime(String dateTimeString) {
        /**
         * Takes in a string, trims it, and parses LocalDateTime if possible, if not null
         *
         * @param dateTimeString String that should contain a date time
         * @return LocalDateTime representing time in string
         * @throws None
         */
        System.out.println("called");

        dateTimeString = dateTimeString.trim();
        System.out.println(dateTimeString);
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

            }
        }
        return null;
    }

    public static class Parser {
        public enum VALID_COMMAND {
            BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, INVALID;
        }


        public VALID_COMMAND parseCommand(String text) {


            if (text.startsWith("bye")) {
                return VALID_COMMAND.BYE;
            } else if (text.startsWith("list")) {
                return VALID_COMMAND.LIST;
            } else if (text.startsWith("mark")) {
                return VALID_COMMAND.MARK;
            } else if (text.startsWith("unmark")) {
                return VALID_COMMAND.UNMARK;
            } else if (text.startsWith("todo")) {
                return VALID_COMMAND.TODO;
            } else if (text.startsWith("deadline")) {
                return VALID_COMMAND.DEADLINE;
            } else if (text.startsWith("event")) {
                return VALID_COMMAND.EVENT;
            } else if (text.startsWith("delete")) {
                return VALID_COMMAND.DELETE;
            } else {
                return VALID_COMMAND.INVALID;
            }

        }

        /**
         * Extracts the index for mark and unmark
         * @param input of format mark/unmark {int}
         * @return the int in the input string
         */
        public int extractNum(String input) {
            int length = input.length();
            StringBuilder numberBuild = new StringBuilder();
            for (int i = length - 1; i >= 0; i--) {
                char currentChar = input.charAt(i);
                if (Character.isDigit(currentChar)) {
                    numberBuild.insert(0, currentChar);
                } else {
                    break;
                }
            }
            return Integer.parseInt(numberBuild.toString());
        }

        /**
         * Used to parse a string for the description of a task
         * @param desc of format todo {text}
         * @return the text description
         */
        protected String parseTODO(String desc) {
            return desc.substring(4).trim();
        }


    }













    private abstract static class Task {
        private Boolean status;
        private String desc;


        public Task(String desc) throws ChatterBoxNoInput{
            if (desc.trim().isEmpty()) {
                throw new ChatterBoxNoInput("Error: No input for task");
            }
            this.status = false;
            this.desc = desc;
        }

        public String getDescription() {
            return this.desc;
        }
        public Boolean getStatus() {
            return this.status;
        }

        public void setStatus(Boolean stat) {
            this.status = stat;
        }
        public String getTaskSymbol() { return "";}

        @Override
        public String toString() {
            return this.getDescription();
        }
    }


    private static class Todo extends Task {


        public Todo(String desc) throws ChatterBoxNoInput{
            super(desc);

        }

        @Override
        public String getTaskSymbol() { return "T"; }

    }

    /** used to parse deadlines
     *
     * @param String of format deadline text /by text
     * @return String[] with the [0] as the description and [1] as by {text}
     * @throws None, does not throw any exceptions
     */
    private static String[] parseDeadline(String desc) throws ChatterBoxMissingParameter{


        int endDate = desc.indexOf("/by");
        if (endDate < 0) {
            throw new ChatterBoxMissingParameter("Deadline date");
        }
        StringBuilder plainDesc = new StringBuilder();
        StringBuilder deadline = new StringBuilder();
        for (int i = 9; i < desc.length(); i++) {
            if (i < endDate) {
                plainDesc.append(desc.charAt(i));

            } else {
                if (desc.charAt(i) != '/') {
                    deadline.append(desc.charAt(i));
                }
            }
        }
        return new String[] {plainDesc.toString(), deadline.toString()} ;
    }

    private static class Deadline extends Task {
        private final LocalDateTime dueDateObj;
        private final String dueDate;

        public Deadline(String desc, String dueDate) throws ChatterBoxNoInput{
            super(desc);
            this.dueDate =dueDate;
            this.dueDateObj = null;


        }

        public Deadline(String desc, LocalDateTime dueDateObj) throws ChatterBoxNoInput{
            super(desc);
            this.dueDateObj = dueDateObj;
            this.dueDate = null;
        }

        @Override
        public String getTaskSymbol() {
            return "D";
        }
        @Override
        public String getDescription() {
            if (this.dueDateObj != null) {
                return super.getDescription() + String.format("( by %s ) ", this.dueDateObj.format(PRINTDATEFORMATTER));
            }
            return super.getDescription() + String.format(" ( %s )", this.dueDate);
        }

    }
    //when response start with event
    /** Parses the event string for the desc, from and to time
     *
     * @param desc takes in a string of format text /from text /to text
     * @return String[] with 0 being the first text, 1 the from text and 2 being the to text
     * @throws ChatterBoxMissingParameter if any of parameters not detected
     */
    private static String[] parseEvent(String desc) throws ChatterBoxMissingParameter{


        int fromStart = desc.indexOf("/from");
        if (fromStart < 0) {
            throw new ChatterBoxMissingParameter("Event Start Date");
        }
        int toStart = desc.indexOf("/to");
        if (toStart < 0) {
            throw new ChatterBoxMissingParameter("Event End Date");
        }
        StringBuilder plainDesc = new StringBuilder();
        StringBuilder startDate = new StringBuilder();
        StringBuilder endDate = new StringBuilder();
        //start with 5 to go past event
        for (int i = 5; i < desc.length(); i++) {
            if (i < fromStart) {
                plainDesc.append(desc.charAt(i));
            } else if (i < toStart) {
                if (desc.charAt(i) != '/') {
                    startDate.append(desc.charAt(i));

                }
            } else {
                if (desc.charAt(i) != '/') {
                    endDate.append(desc.charAt(i));

                }
            }
        }
        return new String[] {plainDesc.toString(), startDate.toString(), endDate.toString()};
    }
    private static class Event extends Task {

        private final LocalDateTime startDateObj;
        private final String startDate;

        private final LocalDateTime endDateObj;
        private final String endDate;




        public Event(String desc, String startDate, String endDate) throws ChatterBoxNoInput{
            super(desc);
            this.startDate = startDate;
            this.endDate = endDate;
            this.startDateObj = null;
            this.endDateObj = null;
        }

        public Event(String desc, LocalDateTime startDateObj, LocalDateTime endDateObj) throws ChatterBoxNoInput{
            super(desc);
            this.startDate = null;
            this.endDate = null;
            this.startDateObj = startDateObj;
            this.endDateObj = endDateObj;
        }

        public String getStartDate() {
            return startDate;
        }

        private String getEndDate() {
            return this.endDate;
        }

        @Override
        public String getTaskSymbol() {
            return "E";
        }
        @Override
        public String getDescription() {
            if (this.startDateObj != null && this.endDateObj != null) {
                return super.getDescription() + String.format("( from %s to %s )", this.startDateObj.format(PRINTDATEFORMATTER)
                , this.endDateObj.format(PRINTDATEFORMATTER));
            }
            return super.getDescription() + String.format(" ( %s %s )", this.startDate, this.endDate);
        }
    }

    public static void main(String[] args) {

        UI ui = new Chatterbox.UI();
        Parser parser = new Parser();
        Scanner scanner = new Scanner(System.in);
        ui.greeting();
        TaskList tasks = new TaskList(new ArrayList<Task>());

        try {
            while (true) {
                String response = scanner.nextLine();
                Parser.VALID_COMMAND command = parser.parseCommand(response);
                int index;
                switch (command){

                    case BYE: return;

                    case LIST:
                        ui.displayList(tasks.getTasks());
                        break;

                    case MARK:
                        response = response.trim();
                        index = parser.extractNum(response) - 1; // -1 as the display  start from 1
                        ui.markMsg(tasks.markTask(index));

                        break;

                    case UNMARK:
                        response = response.trim();
                        index = parser.extractNum(response) - 1; // -1 as the display  start from 1
                        ui.unmarkMsg(tasks.unmarkTask(index));

                        break;

                    case TODO:



                        tasks.addTodo(parser.parseTODO(response.trim()));
                        ui.addTaskMsg("Todo", tasks.size());

                        break;

                    case DEADLINE:
                        String[] parsed = parseDeadline(response);

                        System.out.println(Chatterbox.LINESEPERATOR);
                        LocalDateTime deadlineDate = parseDateTime(parsed[1].substring(2));

                        if (deadlineDate == null) {
                            userList.add(new Deadline(parsed[0], parsed[1]));

                        } else {
                            //add back by for string

                            userList.add(new Deadline(parsed[0], deadlineDate));
                        }

                        current++;
                        System.out.println("Added Deadline to Todo");
                        System.out.println(String.format("Currently %d tasks in list", userList.size()));
                        break;
                    case EVENT:
                        String[] eventParsed = parseEvent(response);

                        LocalDateTime startDate = parseDateTime(eventParsed[1].substring(4)); //from 4
                        LocalDateTime endDate = parseDateTime(eventParsed[2].substring(2));
                        if (startDate == null || endDate == null) {
                            userList.add(new Event(eventParsed[0], eventParsed[1], eventParsed[2]));

                        } else {
                            userList.add(new Event(eventParsed[0], startDate, endDate));
                        }
                        current++;
                        System.out.println(Chatterbox.LINESEPERATOR);

                        System.out.println("Added Event to Todo");
                        System.out.println(String.format("Currently %d tasks in list", userList.size()));
                        break;
                    case DELETE:
                        response = response.trim();
                        int delIndex = parser.extractNum(response) - 1;
                        System.out.println(LINESEPERATOR);
                        System.out.println("Removing Task: ");
                        System.out.println(userList.get(delIndex).toString());
                        userList.remove(delIndex);
                        System.out.println(String.format("List has %d tasks", userList.size()));
                        break;
                    case INVALID:
                        checkMessage(response);
                        break;
                }
//                System.out.println("saving");
                saveHistory(userList);

            }
        } catch (ChatterBoxError e){
            System.out.println(e.getMessage());
        } finally {
            ui.goodBye();
            scanner.close();
        }



    }
}
