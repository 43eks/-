package pg;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

@SuppressWarnings("unused")
public class TodoappGUI {
    private JFrame frame;
    private DefaultListModel<String> todoListModel;
    private JList<String> todoList;
    private JTextField taskField;

    public TodoappGUI() {
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

        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addTask() {
        String task = taskField.getText().trim();
        if (!task.isEmpty()) {
            todoListModel.addElement(task);  // リストにタスクを追加
            taskField.setText("");  // 入力フィールドをクリア
        } else {
            JOptionPane.showMessageDialog(frame, "タスクを入力してください。");
        }
    }

    private void removeTask() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            todoListModel.remove(selectedIndex);  // 選択したタスクを削除
        } else {
            JOptionPane.showMessageDialog(frame, "削除するタスクを選択してください。");
        }
    }

    public void display() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        TodoappGUI app = new TodoappGUI();
        app.display();
    }
}