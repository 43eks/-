package pg;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class TodoappGUI {
    private JFrame frame;
    private DefaultListModel<JPanel> todoListModel;
    private JList<JPanel> todoList;
    private JTextField taskField;
    private List<String> tasks;
    
    public TodoappGUI() {
        tasks = loadTasks();  // アプリ起動時にタスクをロード
        frame = new JFrame("Todoリストアプリ");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // タスクリストを表示するためのリストモデルとJList
        todoListModel = new DefaultListModel<>();
        todoList = new JList<>(todoListModel);
        todoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // スクロールペインを使ってリストをスクロール可能に
        JScrollPane scrollPane = new JScrollPane(todoList);
        frame.add(scrollPane, BorderLayout.CENTER);

        // タスク入力フィールド
        taskField = new JTextField();
        frame.add(taskField, BorderLayout.NORTH);

        // ボタンパネル
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // 「追加」ボタン
        JButton addButton = new JButton("タスクを追加");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });
        buttonPanel.add(addButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    // タスクを追加
    private void addTask() {
        String task = taskField.getText().trim();
        if (!task.isEmpty()) {
            JPanel taskPanel = new JPanel();
            taskPanel.setLayout(new BorderLayout());

            JCheckBox checkBox = new JCheckBox(task);
            checkBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    markAsComplete(checkBox);
                }
            });
            taskPanel.add(checkBox, BorderLayout.CENTER);

            JButton removeButton = new JButton("削除");
            removeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    removeTask(taskPanel);
                }
            });
            taskPanel.add(removeButton, BorderLayout.EAST);

            todoListModel.addElement(taskPanel);
            tasks.add(task);
            taskField.setText("");  // 入力フィールドをクリア
            saveTasks();  // タスクを保存
        } else {
            JOptionPane.showMessageDialog(frame, "タスクを入力してください。");
        }
    }

    // 完了/未完了を管理
    private void markAsComplete(JCheckBox checkBox) {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            if (checkBox.isSelected()) {
                checkBox.setText("[完了] " + checkBox.getText());
            } else {
                checkBox.setText(checkBox.getText().replace("[完了] ", ""));
            }
            saveTasks();  // タスクを保存
        }
    }

    // タスクを削除
    private void removeTask(JPanel taskPanel) {
        todoListModel.removeElement(taskPanel);
        tasks.remove(todoListModel.indexOf(taskPanel));  // リストから削除
        saveTasks();  // タスクを保存
    }

    // タスクをファイルに保存
    private void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"))) {
            for (String task : tasks) {
                writer.write(task);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 起動時にタスクをファイルからロード
    private List<String> loadTasks() {
        List<String> loadedTasks = new ArrayList<>();
        File file = new File("tasks.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    loadedTasks.add(line);
                    JCheckBox checkBox = new JCheckBox(line);
                    todoListModel.addElement(createTaskPanel(checkBox));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return loadedTasks;
    }

    private JPanel createTaskPanel(JCheckBox checkBox) {
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BorderLayout());
        taskPanel.add(checkBox, BorderLayout.CENTER);

        JButton removeButton = new JButton("削除");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeTask(taskPanel);
            }
        });
        taskPanel.add(removeButton, BorderLayout.EAST);

        return taskPanel;
    }

    public void display() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        TodoappGUI app = new TodoappGUI();
        app.display();
    }
}