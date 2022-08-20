package duke.task;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import duke.command.DukeCommandType;
import duke.ui.DukeUi;

public class DukeTaskManager {
    private List<Task> dukeTasks;

    public DukeTaskManager() {
        this.dukeTasks = new ArrayList<Task>();
    }

    public DukeTaskManager(List<Task> dukeTasks) {
        this.dukeTasks = dukeTasks;
    }

    public List<Task> getTasks() {
        return dukeTasks;
    }

    public void dukeAddTask(DukeCommandType type, String args) {
        if (args.isEmpty()) {
            DukeUi.dukePrint("Description cannot be empty\n");
            return;
        }
        switch (type) {
        case TODO: {
            dukeAddToList(new Todo(args));
            return;
        }
        case DEADLINE: {
            try {
                Pattern p = Pattern.compile("(.+)/by(.+)");
                Matcher m = p.matcher(args);
                m.find();
                dukeAddToList(new Deadline(m.group(1).trim(), m.group(2).trim()));
            } catch (IllegalStateException e) {
                DukeUi.dukePrint("Are you missing a /by ?\n");
            }
            break;
        }
        case EVENT: {
            try {
                Pattern p = Pattern.compile("(.+)/at(.+)");
                Matcher m = p.matcher(args);
                m.find();
                dukeAddToList(new Event(m.group(1).trim(), m.group(2).trim()));
            } catch (IllegalStateException e) {
                DukeUi.dukePrint("Are you missing a /at ?\n");
            }
            break;
        }
        }
        return;
    }

    private void dukeAddToList(Task task) {
        dukeTasks.add(task);
        DukeUi.dukePrint(String.format("Got it. I've added this task: \n %s\n %s\n",
                task.toString(), getNoOfTasks()));
        return;
    }

    protected String getNoOfTasks() {
        int size = dukeTasks.size();
        if (size <= 1) {
            return String.format("Now you have %d task in the list\n", size);
        } else {
            return String.format("Now you have %d tasks in the list\n", size);
        }
    }

    public void dukeShowList() {
        String tasks = "List of tasks to be done:\n";
        for (int i = 0; i < dukeTasks.size(); i++) {
            tasks += String.format("%d. %s\n", i + 1, dukeTasks.get(i));
        }
        DukeUi.dukePrint(tasks);
    }

    private void dukeMarkTask(int i) {
        if ((0 <= i) && (i < dukeTasks.size())) {
            dukeTasks.get(i).markComplete();
            String str = dukeTasks.get(i).toString();
            DukeUi.dukePrint(String.format("Nice! I've marked this task as done:\n %s\n", str));
        } else {
            DukeUi.dukePrint("Error. duke.task.Task is not in the list\n");
        }
    }

    private void dukeUnmarkTask(int i) {
        if ((0 <= i) && (i < dukeTasks.size())) {
            dukeTasks.get(i).markIncomplete();
            String str = dukeTasks.get(i).toString();
            DukeUi.dukePrint(String.format("OK, I've marked this task as not done yet:\n %s\n", str));
        } else {
            DukeUi.dukePrint("Error. Task is not in the list\n");
        }
    }

    private void dukeRemoveTask(int i) {
        if ((0 <= i) && (i < dukeTasks.size())) {
            Task remove = dukeTasks.remove(i);
            String str = remove.toString();
            DukeUi.dukePrint(String.format("OK, I've remove this task:\n %s\n %s", str, getNoOfTasks()));
        } else {
            DukeUi.dukePrint("Error. Task is not in the list\n");
        }
    }

    public void dukeUpdateTaskStatus(DukeCommandType type, String str) {
        try {
            int index = Integer.parseInt(str.split(" ")[0]) - 1;
            switch (type) {
            case MARK: {
                dukeMarkTask(index);
                break;
            }
            case UNMARK: {
                dukeUnmarkTask(index);
                break;
            }
            case DELETE: {
                dukeRemoveTask(index);
                break;
            }
            }
        } catch (NumberFormatException e) {
            DukeUi.dukePrint("Invalid index. Index is not a number\n");
        }
    }

}
