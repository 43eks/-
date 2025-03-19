package pg;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class TodoappGUI {
    private JFrame frame;
    private JTextField taskField;
    private JList<Task> todoList;
    private DefaultListModel<Task> todoListModel;
    private List<Task> tasks;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> filterComboBox;

    public TodoappGUI() {
        tasks = loadTasksFromFile();
        frame = new JFrame("Todoリストアプリ");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // タスクリストを表示するためのリストモデルとJList
        todoListModel = new DefaultListModel<>();
        todoList = new JList<>(todoListModel);
        todoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(todoList);
        frame.add(scrollPane, BorderLayout.CENTER);

        // カテゴリ選択用のコンボボックス（タスク追加用）
        String[] categories = {"仕事", "勉強", "買い物", "趣味", "その他"};
        categoryComboBox = new JComboBox<>(categories);
        frame.add(categoryComboBox, BorderLayout.WEST);

        // フィルタリング用のコンボボックス
        String[] filterCategories = {"すべて", "仕事", "勉強", "買い物", "趣味", "その他"};
        filterComboBox = new JComboBox<>(filterCategories);
        filterComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterTasksByCategory((String) filterComboBox.getSelectedItem());
            }
        });
        frame.add(filterComboBox, BorderLayout.NORTH);

        // タスク入力フィールド
        taskField = new JTextField();
        frame.add(taskField, BorderLayout.SOUTH);

        // ボタンパネル
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // 「追加」ボタン
        JButton addButton = new JButton("タスクを追加");
        addButton.addActionListener(e -> addTask());
        buttonPanel.add(addButton);

        // 「完了」ボタン
        JButton completeButton = new JButton("タスクを完了");
        completeButton.addActionListener(e -> markTaskAsCompleted());
        buttonPanel.add(completeButton);

        // 「削除」ボタン
        JButton deleteButton = new JButton("タスクを削除");
        deleteButton.addActionListener(e -> deleteTask());
        buttonPanel.add(deleteButton);

        // 「編集」ボタン
        JButton editButton = new JButton("タスクを編集");
        editButton.addActionListener(e -> editTask());
        buttonPanel.add(editButton);

        // 「保存」ボタン
        JButton saveButton = new JButton("タスクを保存");
        saveButton.addActionListener(e -> saveTasksToFile());
        buttonPanel.add(saveButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        loadTasks();
    }

    private void loadTasks() {
        todoListModel.clear();
        for (Task task : tasks) {
            todoListModel.addElement(task);
        }
    }

    private void addTask() {
        String taskName = taskField.getText().trim();
        String category = (String) categoryComboBox.getSelectedItem();

        if (!taskName.isEmpty()) {
            Task task = new Task(taskName, category);
            tasks.add(task);
            todoListModel.addElement(task);
            taskField.setText("");
            saveTasksToFile();
        }
    }

    private void markTaskAsCompleted() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task selectedTask = todoListModel.getElementAt(selectedIndex);
            selectedTask.setCompleted(true);
            todoList.repaint();
            saveTasksToFile();
        }
    }

    private void deleteTask() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task selectedTask = todoListModel.getElementAt(selectedIndex);
            tasks.remove(selectedTask);
            todoListModel.remove(selectedIndex);
            saveTasksToFile();
        }
    }

    private void editTask() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task selectedTask = todoListModel.getElementAt(selectedIndex);
            String newTaskName = JOptionPane.showInputDialog(frame, "新しいタスク名を入力:", selectedTask.getName());
            if (newTaskName != null && !newTaskName.trim().isEmpty()) {
                selectedTask.setName(newTaskName);
                todoList.repaint();
                saveTasksToFile();
            }
        }
    }

    private void filterTasksByCategory(String category) {
        todoListModel.clear();
        for (Task task : tasks) {
            if (category.equals("すべて") || task.getCategory().equals(category)) {
                todoListModel.addElement(task);
            }
        }
    }

    private void saveTasksToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tasks.dat"))) {
            out.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Task> loadTasksFromFile() {
        List<Task> loadedTasks = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("tasks.dat"))) {
            loadedTasks = (List<Task>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loadedTasks;
    }

    public void display() {
        frame.setVisible(true);
    }

    // タスククラス
    public static class Task implements Serializable {
        private String name;
        private String category;
        private boolean isCompleted;

        public Task(String name, String category) {
            this.name = name;
            this.category = category;
            this.isCompleted = false;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCategory() { return category; }
        public boolean isCompleted() { return isCompleted; }
        public void setCompleted(boolean completed) { isCompleted = completed; }

        @Override
        public String toString() {
            return (isCompleted ? "[完了]" : "[未完了]") + " [" + category + "] " + name;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TodoappGUI todoApp = new TodoappGUI();
            todoApp.display();
        });
    }
}