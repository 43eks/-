package pg;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

// 📖 日記の1つのエントリを表すクラス
class DiaryEntry {
    private String date;
    private String content;

    public DiaryEntry(String date, String content) {
        this.date = date;
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return date;
    }
}

// 📝 GUI版 日記アプリ
public class DiaryappGUI extends JFrame {
    private static final String FILE_NAME = "diary.txt";
    private List<DiaryEntry> diaryEntries = new ArrayList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> diaryList;
    private JTextArea contentArea;
    private JTextField dateField;

    public DiaryappGUI() {
        setTitle("📔 日記アプリ");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 📅 上部パネル（新しい日記の入力）
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        dateField = new JTextField(10);
        JButton addButton = new JButton("追加");
        topPanel.add(new JLabel("日付:"));
        topPanel.add(dateField);
        topPanel.add(addButton);
        add(topPanel, BorderLayout.NORTH);

        // 📜 中央パネル（リスト＆内容表示）
        diaryList = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(diaryList);
        contentArea = new JTextArea();
        contentArea.setEditable(false);
        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, contentScrollPane);
        splitPane.setDividerLocation(150);
        add(splitPane, BorderLayout.CENTER);

        // 🔧 下部パネル（編集・削除ボタン）
        JPanel bottomPanel = new JPanel();
        JButton editButton = new JButton("編集");
        JButton deleteButton = new JButton("削除");
        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // 🗂 データをロード
        loadDiaryFromFile();

        // 📌 イベントリスナー（ボタン操作）
        addButton.addActionListener(e -> addDiaryEntry());
        diaryList.addListSelectionListener(e -> displayEntry());
        editButton.addActionListener(e -> editDiaryEntry());
        deleteButton.addActionListener(e -> deleteDiaryEntry());

        setVisible(true);
    }

    // 🔄 ファイルから日記データを読み込む
    private void loadDiaryFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("::", 2);
                if (parts.length == 2) {
                    diaryEntries.add(new DiaryEntry(parts[0], parts[1]));
                    listModel.addElement(parts[0]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("データファイルが見つかりません。新しいファイルを作成します。");
        } catch (IOException e) {
            System.out.println("ファイルの読み込みエラー。");
        }
    }

    // 💾 ファイルに日記を保存
    private void saveDiaryToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (DiaryEntry entry : diaryEntries) {
                writer.write(entry.getDate() + "::" + entry.getContent());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("ファイル保存中にエラーが発生しました。");
        }
    }

    // ➕ 日記を追加
    private void addDiaryEntry() {
        String date = dateField.getText().trim();
        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "日付を入力してください。");
            return;
        }
        String content = JOptionPane.showInputDialog(this, "日記の内容を入力してください:");
        if (content == null || content.trim().isEmpty()) return;

        diaryEntries.add(new DiaryEntry(date, content));
        listModel.addElement(date);
        saveDiaryToFile();
        dateField.setText("");
    }

    // 📖 選択した日記を表示
    private void displayEntry() {
        int index = diaryList.getSelectedIndex();
        if (index >= 0) {
            contentArea.setText(diaryEntries.get(index).getContent());
        }
    }

    // ✏️ 日記を編集
    private void editDiaryEntry() {
        int index = diaryList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "編集する日記を選択してください。");
            return;
        }

        String newContent = JOptionPane.showInputDialog(this, "新しい内容を入力してください:", diaryEntries.get(index).getContent());
        if (newContent != null && !newContent.trim().isEmpty()) {
            diaryEntries.get(index).setContent(newContent);
            saveDiaryToFile();
            displayEntry();
        }
    }

    // ❌ 日記を削除
    private void deleteDiaryEntry() {
        int index = diaryList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "削除する日記を選択してください。");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "本当に削除しますか？", "確認", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            diaryEntries.remove(index);
            listModel.remove(index);
            contentArea.setText("");
            saveDiaryToFile();
        }
    }

    // 🚀 メインメソッド（プログラムの起動）
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DiaryappGUI::new);
    }
}
