package pg;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public TodoappGUI() {
        tasks = loadTasksFromFile();
        frame = new JFrame("Todoリストアプリ");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // タスクリスト
        todoListModel = new DefaultListModel<>();
        todoList = new JList<>(todoListModel);
        todoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(todoList);
        frame.add(scrollPane, BorderLayout.CENTER);

        // カテゴリ選択コンボボックス
        String[] categories = {"仕事", "勉強", "買い物", "趣味", "その他"};
        categoryComboBox = new JComboBox<>(categories);
        frame.add(categoryComboBox, BorderLayout.WEST);

        // フィルタリング用コンボボックス
        String[] filterCategories = {"すべて", "仕事", "勉強", "買い物", "趣味", "その他"};
        filterComboBox = new JComboBox<>(filterCategories);
        filterComboBox.addActionListener(e -> filterTasksByCategory((String) filterComboBox.getSelectedItem()));
        frame.add(filterComboBox, BorderLayout.NORTH);

        // タスク入力フィールド
        taskField = new JTextField();
        frame.add(taskField, BorderLayout.SOUTH);

        // ボタンパネル
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("タスクを追加");
        addButton.addActionListener(e -> addTask());
        buttonPanel.add(addButton);

        JButton completeButton = new JButton("完了");
        completeButton.addActionListener(e -> markTaskAsCompleted());
        buttonPanel.add(completeButton);

        JButton deleteButton = new JButton("削除");
        deleteButton.addActionListener(e -> deleteTask());
        buttonPanel.add(deleteButton);

        JButton editButton = new JButton("編集");
        editButton.addActionListener(e -> editTask());
        buttonPanel.add(editButton);

        JButton deadlineButton = new JButton("期日変更");
        deadlineButton.addActionListener(e -> changeTaskDeadline());
        buttonPanel.add(deadlineButton);

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> saveTasksToFile());
        buttonPanel.add(saveButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        loadTasks();
    }

    private void loadTasks() {
        todoListModel.clear();
        sortTasksByDeadline();
        for (Task task : tasks) {
            todoListModel.addElement(task);
        }
    }

    private void addTask() {
        String taskName = taskField.getText().trim();
        String category = (String) categoryComboBox.getSelectedItem();

        if (!taskName.isEmpty()) {
            String deadlineStr = JOptionPane.showInputDialog(frame, "期日を入力 (YYYY-MM-DD):");
            Date deadline = parseDate(deadlineStr);

            Task task = new Task(taskName, category, deadline);
            tasks.add(task);
            sortTasksByDeadline();
            loadTasks();
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

    private void changeTaskDeadline() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task selectedTask = todoListModel.getElementAt(selectedIndex);
            String newDeadlineStr = JOptionPane.showInputDialog(frame, "新しい期日を入力 (YYYY-MM-DD):");
            Date newDeadline = parseDate(newDeadlineStr);
            if (newDeadline != null) {
                selectedTask.setDeadline(newDeadline);
                sortTasksByDeadline();
                loadTasks();
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

    private void sortTasksByDeadline() {
        tasks.sort(Comparator.comparing(Task::getDeadline, Comparator.nullsLast(Comparator.naturalOrder())));
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

    private Date parseDate(String dateStr) {
        try {
            return dateStr == null ? null : dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public void display() {
        frame.setVisible(true);
    }

    public static class Task implements Serializable {
        private String name;
        private String category;
        private boolean isCompleted;
        private Date deadline;

        public Task(String name, String category, Date deadline) {
            this.name = name;
            this.category = category;
            this.isCompleted = false;
            this.deadline = deadline;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCategory() { return category; }
        public boolean isCompleted() { return isCompleted; }
        public void setCompleted(boolean completed) { isCompleted = completed; }
        public Date getDeadline() { return deadline; }
        public void setDeadline(Date deadline) { this.deadline = deadline; }

        @Override
        public String toString() {
            return (isCompleted ? "[完了]" : "[未完了]") + " [" + category + "] " + name + " (期限: " + (deadline == null ? "なし" : new SimpleDateFormat("yyyy-MM-dd").format(deadline)) + ")";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TodoappGUI().display());
    }
}