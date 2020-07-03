package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TaskManager {

    static final String TASKFILE = "tasks.csv";

    static String[][] tasks;

    public static void main(String[] args) {

        tasks = readTasksFromFile(TASKFILE);
        showMenu();

    }

    public static String[][] readTasksFromFile(String fileName) {

        String[][] taskTab = null;
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            System.out.println("File doesn't exist.");
            System.exit(0);
        }

        try {
            List<String> strings = Files.readAllLines(path);
            taskTab = new String[strings.size()][strings.get(0).split(",").length];

            for (int i = 0; i < strings.size(); i++) {
                String[] line = strings.get(i).split(",");
                for (int j = 0; j < line.length; j++) {
                    taskTab[i][j] = line[j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return taskTab;
    }

    public static void options() {

        System.out.println(ConsoleColors.BLUE + "Please select an option:\n" +
                ConsoleColors.RESET + "add\nremove\nlist\nexit\n");

    }

    public static void showMenu() {

        Scanner scan = new Scanner(System.in);

        options();

        while (scan.hasNextLine()) {
            String input = scan.next();
            switch (input) {
                case ("add"):
                    addTask();
                    break;
                case ("remove"):
                    removeTask(tasks);
                    break;
                case ("list"):
                    listTasks(tasks);
                    break;
                case ("exit"):
                    exitTaskManager();
                    break;
                default:
                    System.out.println(ConsoleColors.RED + "Please select a correct option!");
            }
            options();
        }
    }

    public static void addTask() {

        Scanner scan = new Scanner(System.in);

        System.out.println("Please add task description");
        String taskDescription = scan.nextLine()+" ";

        System.out.println("Please add task due date (yyyy-mm-dd)");
        String dueDate = "";
        if (validateJavaDate(dueDate)) {
            dueDate = scan.nextLine()+" ";
        } else
            System.out.println("incorrect date format, please input yyyy-mm-dd");

        System.out.println("Is your task important? true/false");
        String taskImportance = "";
        try {
            taskImportance = scan.nextBoolean() + "";
        } catch (InputMismatchException e) {
            System.out.println("please state true or false");
        }

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = taskDescription;
        tasks[tasks.length - 1][1] = dueDate;
        tasks[tasks.length - 1][2] = taskImportance;
        System.out.println();
    }

    public static void saveTaskFile() {
        Path path = Paths.get(TASKFILE);

        String[] lines = new String [tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            lines[i] = String.join(",", tasks[i]);
        }
        try {
            Files.write(path, Arrays.asList(lines));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean validateJavaDate(String dueDate) {

        if (!dueDate.trim().equals("")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
            simpleDateFormat.setLenient(false);

            try {
                Date javaDate = simpleDateFormat.parse(dueDate);

            } catch (ParseException e) {

                return false;
            }
        }
        return true;
    }

    public static void exitTaskManager() {
        saveTaskFile();
        System.out.println(ConsoleColors.RED + "See you later!");
        System.exit(0);
    }

    public static void listTasks(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + ": ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + "");
            }
            System.out.println();


        } System.out.println();

    }

    public static void removeTask(String[][] tab) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Which task would you like to delete?");
        int input = scan.nextInt();

        if (input < tab.length) {
            tasks = ArrayUtils.remove(tab, input);
            System.out.println();

        } else System.out.println("Please input correct task index, check 'list' for a list of tasks\n");
    }
}





