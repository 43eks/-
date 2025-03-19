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
import javax.swing.JFrame;
import javax.swing.JList;
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

    public TodoappGUI() {
        tasks = loadTasksFromFile();  // アプリ起動時にタスクをロード
        frame = new JFrame("Todoリストアプリ");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // タスクリストを表示するためのリストモデルとJList
        todoListModel = new DefaultListModel<>();  // 初期化
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
     // 「削除」ボタン
        JButton deleteButton = new JButton("タスクを削除");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteTask();
            }
        });
        buttonPanel.add(deleteButton);
        buttonPanel.add(addButton);

        // 「完了」ボタン
        JButton completeButton = new JButton("タスクを完了");
        completeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                markTaskAsCompleted();
            }
        });
        buttonPanel.add(completeButton);

        // 「保存」ボタン
        JButton saveButton = new JButton("タスクを保存");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveTasksToFile();
            }
        });
        buttonPanel.add(saveButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // 既存のタスクをロード
        loadTasks();
    }

    private void loadTasks() {
        todoListModel.clear();  // 既存のタスクをクリア
        for (Task task : tasks) {
            todoListModel.addElement(task);  // タスクをリストに追加
        }
    }

    private void addTask() {
        String taskName = taskField.getText().trim();
        if (!taskName.isEmpty()) {
            Task task = new Task(taskName);
            tasks.add(task);
            todoListModel.addElement(task);
            taskField.setText("");
            saveTasksToFile();  // タスクをファイルに保存
        }
    }
    private void deleteTask() {
        int selectedIndex = todoList.getSelectedIndex(); // 選択されたタスクのインデックスを取得
        if (selectedIndex != -1) {
            tasks.remove(selectedIndex); // リストから削除
            todoListModel.remove(selectedIndex); // GUIのリストから削除
            saveTasksToFile(); // 削除後にファイルを更新
        }
    }

    private void markTaskAsCompleted() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task selectedTask = todoListModel.getElementAt(selectedIndex);
            selectedTask.setCompleted(true);  // タスクを完了に設定
            todoList.repaint();  // JListを再描画
            saveTasksToFile();  // タスクをファイルに保存
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
            // ファイルが存在しない場合や読み込みエラーを無視
            e.printStackTrace();
        }
        return loadedTasks;
    }

    public void display() {
        frame.setVisible(true);
    }

    // Taskクラス（タスクの情報を保持）
    public static class Task implements Serializable {
        private String name;
        private boolean isCompleted;

        public Task(String name) {
            this.name = name;
            this.isCompleted = false;
        }

        public String getName() {
            return name;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public void setCompleted(boolean completed) {
            isCompleted = completed;
        }

        @Override
        public String toString() {
            return (isCompleted ? "[完了]" : "[未完了]") + " " + name;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TodoappGUI todoApp = new TodoappGUI();
                todoApp.display();
            }
        });
    }
}