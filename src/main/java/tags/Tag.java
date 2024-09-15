package tags;

import java.util.ArrayList;

import tasks.Task;


/**
 * Represents a tag that can be added to tasks
 */
public class Tag {

    private final String tagName;

    private ArrayList<Task> taggedTasks = new ArrayList<>();


    public Tag(String tagName) {
        this.tagName = tagName;
    }

    /**
     * Returns the name of the tag
     * @return the name of the tag
     */
    public String getTagName() {
        return tagName;
    }

    @Override
    public String toString() {
        return tagName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tag) {
            return this.tagName.equals(((Tag) obj).tagName);
        }
        return false;
    }

    /**
     * Adds a task to the list of tagged tasks
     * @param task the task to be added
     */
    public void tagTask(Task task) {
        taggedTasks.add(task);
    }

    /**
     * Gets string with of all tagged tasks descriptions
     * @return string of all tagged tasks descriptions
     */
    public String getTaggedTasks() {
        StringBuilder taggedTasksString = new StringBuilder();
        for (Task task : taggedTasks) {
            taggedTasksString.append(task.getDescription()).append("\n");
        }
        return taggedTasksString.toString();
    }

    /**
     * Checks if a task is under the tag
     * @param task the task to be checked
     */
    public boolean isTagged(Task task) {
        return taggedTasks.contains(task);
    }
}
