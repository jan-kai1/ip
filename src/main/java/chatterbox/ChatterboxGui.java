package chatterbox;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;

import chatterboxexceptions.ChatterboxExceptions;
import command.Command;
import gui.GuiResponses;
import parser.Parser;
import storage.Storage;
import tags.TagList;
import tasks.Task;
import tasks.TaskList;

/**
 * main class that encapsulates all gui chatbot functionality
 */
public class ChatterboxGui {
    private final GuiResponses guiResponses;
    private final Parser parser;
    private final Storage storage;

    private final TaskList tasks;
    private final TagList userTags;

    /**
     * initiates Chatterbox with a prior history filepath
     * @param filepath contains the history of tasks
     */
    public ChatterboxGui(String filepath) {
        assert filepath != null;

        this.guiResponses = new GuiResponses();
        this.parser = new Parser();
        this.storage = new Storage(filepath);
        this.userTags = new TagList();
        ArrayList<Task> loadedTask = new ArrayList<>();
        TagList loadedTags = new TagList();
        try {
            storage.load(parser, loadedTask, loadedTags);
        } catch (FileNotFoundException e) {
            System.out.println("Error: No history file found at path");
        }


        this.tasks = new TaskList(loadedTask);


    }

    /**
     * Initiates Chatterbox with no prior history
     */
    public ChatterboxGui() {
        this.guiResponses = new GuiResponses();

        this.parser = new Parser();
        this.storage = new Storage();
        ArrayList<Task> loaded = new ArrayList<>();
        TagList loadedTags = new TagList();

        try {
            storage.load(parser, loaded, loadedTags);
        } catch (FileNotFoundException e) {
            System.out.println("Error: No history file found at path");
        }

        this.tasks = new TaskList(loaded);
        this.userTags = loadedTags;

    }


    /**
     * checks if the Chatterbox instance has any tasks
     * @return true if there are tasks else false
     */
    public boolean hasTasks() {
        if (tasks.size() > 0) {
            return true;
        }
        return false;
    }


    /**
     * Processes the user input to return the appropriate response
     */
    public String processInput(String input) {
        input = input.trim();
        Command currCommand = parser.parseCommandType(input);
        String result = null;
        try {
            result = currCommand.execute(input, guiResponses, userTags, tasks, parser);
        } catch (ChatterboxExceptions.ChatterBoxError e) {
            result = ("Sorry there was an error: " + e.getMessage());
        }
        storage.saveHistory(tasks.getTasks());
        return result;

    }


    /**
     * Dummy echo testing
     * @return repeats the string with haha:
     */
    public String getResponse(String input) {
        return "haha: " + input;
    }

    /**
     * Gets the greeting string
     * @return greeting String
     */
    public String getGreeting() {
        return guiResponses.greeting();
    }
    public static void main(String[] args) {

        Chatterbox myChat = new Chatterbox(
                Paths.get(System.getProperty("user.dir"), "data" , "command1.txt").toString());
        myChat.run();


    }
}
