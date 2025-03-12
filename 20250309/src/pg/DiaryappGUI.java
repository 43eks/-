package pg;

import java.awt.BorderLayout;
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
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class DiaryappGUI {
    private JFrame frame;
    private DefaultListModel<String> listModel;
    private JList<String> diaryList;
    private JTextArea diaryContent;
    private JTextField searchField;
    private List<DiaryEntry> diaryEntries = new ArrayList<>();
    private static final String FILE_NAME = "diary.txt";

    // 日記のデータ構造
    static class DiaryEntry {
        String date;
        String content;

        public DiaryEntry(String date, String content) {
            this.date = date;
            this.content = content;
        }

        @Override
        public String toString() {
            return date;
        }
    }

    public DiaryappGUI() {
        // ウィンドウの基本設定
        frame = new JFrame("📔 日記アプリ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // 📜 日記一覧のリスト
        listModel = new DefaultListModel<>();
        diaryList = new JList<>(listModel);
        diaryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        diaryList.addListSelectionListener(e -> displaySelectedDiary());

        JScrollPane listScrollPane = new JScrollPane(diaryList);

        // 📖 日記の内容表示エリア
        diaryContent = new JTextArea();
        diaryContent.setLineWrap(true);
        diaryContent.setWrapStyleWord(true);
        JScrollPane textScrollPane = new JScrollPane(diaryContent);

        // 🔍 検索バー
        searchField = new JTextField();
        searchField.addActionListener(e -> searchDiary());
        JButton searchButton = new JButton("検索");
        searchButton.addActionListener(e -> searchDiary());

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // 🛠 ボタンパネル
        JButton addButton = new JButton("追加");
        addButton.addActionListener(e -> addDiaryEntry());

        JButton editButton = new JButton("編集");
        editButton.addActionListener(e -> editDiaryEntry());

        JButton deleteButton = new JButton("削除");
        deleteButton.addActionListener(e -> deleteDiaryEntry());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // 📦 レイアウト設定
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(listScrollPane, BorderLayout.WEST);
        frame.add(textScrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // 日記データの読み込み
        loadDiaryFromFile();

        frame.setVisible(true);
    }

    // 📖 選択した日記を表示
    private void displaySelectedDiary() {
        int index = diaryList.getSelectedIndex();
        if (index != -1) {
            diaryContent.setText(diaryEntries.get(index).content);
        }
    }

    // ➕ 日記を追加
    private void addDiaryEntry() {
        String date = JOptionPane.showInputDialog(frame, "📅 日付を入力 (例: 2025-03-12):");
        if (date == null || date.trim().isEmpty()) return;

        String content = JOptionPane.showInputDialog(frame, "📝 日記の内容を入力:");
        if (content == null || content.trim().isEmpty()) return;

        diaryEntries.add(new DiaryEntry(date, content));
        listModel.addElement(date);
        saveDiaryToFile();
    }

    // ✏️ 日記を編集
    private void editDiaryEntry() {
        int index = diaryList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(frame, "⚠️ 編集する日記を選択してください。");
            return;
        }

        String newContent = JOptionPane.showInputDialog(frame, "📝 新しい内容:", diaryEntries.get(index).content);
        if (newContent != null) {
            diaryEntries.get(index).content = newContent;
            saveDiaryToFile();
        }
    }

    // ❌ 日記を削除
    private void deleteDiaryEntry() {
        int index = diaryList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(frame, "⚠️ 削除する日記を選択してください。");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "⚠️ 本当に削除しますか？", "確認", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            diaryEntries.remove(index);
            listModel.remove(index);
            diaryContent.setText("");
            saveDiaryToFile();
        }
    }

    // 🔍 日記を検索
    private void searchDiary() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) return;

        listModel.clear();
        for (DiaryEntry entry : diaryEntries) {
            if (entry.date.contains(keyword) || entry.content.contains(keyword)) {
                listModel.addElement(entry.date);
            }
        }
    }

    // 🔄 ファイルから日記を読み込む
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
            System.out.println("📂 新しい日記ファイルを作成します。");
        } catch (IOException e) {
            System.out.println("⚠️ ファイル読み込みエラー");
        }
    }

    // 💾 日記をファイルに保存
    private void saveDiaryToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (DiaryEntry entry : diaryEntries) {
                writer.write(entry.date + "::" + entry.content);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("⚠️ ファイル保存エラー");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DiaryappGUI::new);
    }
}