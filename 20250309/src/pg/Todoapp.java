/**
 * 
 */
package pg;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Todoapp {
    private List<String> todoList;
    private Scanner scanner;

    public Todoapp() {
        todoList = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    viewTasks();
                    break;
                case 3:
                    removeTask();
                    break;
                case 4:
                    System.out.println("終了します...");
                    return;
                default:
                    System.out.println("無効な選択肢です。");
            }
        }
    }

    private void showMenu() {
        System.out.println("\nTODOリストアプリ");
        System.out.println("1. タスクを追加");
        System.out.println("2. タスクを表示");
        System.out.println("3. タスクを削除");
        System.out.println("4. 終了");
        System.out.print("選択してください: ");
    }

    private void addTask() {
        System.out.print("追加したいタスクを入力してください: ");
        String task = scanner.nextLine();
        todoList.add(task);
        System.out.println("タスクが追加されました!");
    }

    private void viewTasks() {
        if (todoList.isEmpty()) {
            System.out.println("タスクはありません。");
        } else {
            System.out.println("\n現在のタスク:");
            for (int i = 0; i < todoList.size(); i++) {
                System.out.println((i + 1) + ". " + todoList.get(i));
            }
        }
    }

    private void removeTask() {
        System.out.print("削除したいタスク番号を入力してください: ");
        int taskNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (taskNumber >= 1 && taskNumber <= todoList.size()) {
            todoList.remove(taskNumber - 1);
            System.out.println("タスクが削除されました!");
        } else {
            System.out.println("無効な番号です。");
        }
    }

    public static void main(String[] args) {
        Todoapp app = new Todoapp();
        app.start();
    }
}
