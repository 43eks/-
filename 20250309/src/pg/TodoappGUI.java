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
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class TodoappGUI {
    private JFrame frame;
    private DefaultListModel<String> todoListModel;
    private JList<String> todoList;
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

        // 「削除」ボタン
        JButton removeButton = new JButton("タスクを削除");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeTask();
            }
        });
        buttonPanel.add(removeButton);

        // 「完了」ボタン
        JButton completeButton = new JButton("完了/未完了");
        completeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                markAsComplete();
            }
        });
        buttonPanel.add(completeButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addTask() {
        String task = taskField.getText().trim();
        if (!task.isEmpty()) {
            tasks.add(task);
            todoListModel.addElement(task);
            taskField.setText("");  // 入力フィールドをクリア
            saveTasks();  // タスクを保存
        } else {
            JOptionPane.showMessageDialog(frame, "タスクを入力してください。");
        }
    }

    private void removeTask() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            tasks.remove(selectedIndex);  // 選択したタスクを削除
            todoListModel.remove(selectedIndex);  // リストからも削除
            saveTasks();  // タスクを保存
        } else {
            JOptionPane.showMessageDialog(frame, "削除するタスクを選択してください。");
        }
    }

    private void markAsComplete() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            String task = todoListModel.get(selectedIndex);
            String markedTask = task.startsWith("[完了] ") ? task.substring(5) : "[完了] " + task;
            todoListModel.set(selectedIndex, markedTask);  // リスト内のタスクを更新

            // 完了マークをファイルに反映
            tasks.set(selectedIndex, markedTask);
            saveTasks();
        } else {
            JOptionPane.showMessageDialog(frame, "完了/未完了にするタスクを選択してください。");
        }
    }

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

    private List<String> loadTasks() {
        List<String> loadedTasks = new ArrayList<>();
        File file = new File("tasks.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    loadedTasks.add(line);
                    todoListModel.addElement(line);  // リストに追加
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return loadedTasks;
    }

    public void display() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        TodoappGUI app = new TodoappGUI();
        app.display();
    }
}