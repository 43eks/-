package pg;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class DiaryappGUI {
    private JFrame frame;
    private JTree diaryTree;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    private JTextArea diaryContent;
    private JComboBox<String> sortBox;
    private Map<String, List<DiaryEntry>> diaryMap = new HashMap<>();
    private static final String FILE_NAME = "diary.txt";
    private String currentSortOrder = "日付昇順"; // 初期値は昇順

    static class DiaryEntry {
        String date;
        String content;

        public DiaryEntry(String date, String content) {
            this.date = date;
            this.content = content;
        }
    }

    public DiaryappGUI() {
        frame = new JFrame("📔 月別日記アプリ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // 📅 ツリー構造の作成
        root = new DefaultMutableTreeNode("日記一覧");
        treeModel = new DefaultTreeModel(root);
        diaryTree = new JTree(treeModel);
        diaryTree.addTreeSelectionListener(e -> displaySelectedDiary());

        JScrollPane treeScrollPane = new JScrollPane(diaryTree);

        // 📖 日記の内容表示エリア
        diaryContent = new JTextArea();
        diaryContent.setLineWrap(true);
        diaryContent.setWrapStyleWord(true);
        JScrollPane textScrollPane = new JScrollPane(diaryContent);

        // 🔽 ソート順選択ボックス
        String[] sortOptions = {"日付昇順", "日付降順"};
        sortBox = new JComboBox<>(sortOptions);
        sortBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                currentSortOrder = (String) sortBox.getSelectedItem();
                updateTree();
            }
        });

        // 🛠 ボタンパネル
        JButton addButton = new JButton("追加");
        addButton.addActionListener(e -> addDiaryEntry());

        JButton deleteButton = new JButton("削除");
        deleteButton.addActionListener(e -> deleteDiaryEntry());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sortBox);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        // 📦 レイアウト設定
        frame.add(treeScrollPane, BorderLayout.WEST);
        frame.add(textScrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // ファイルから日記を読み込む
        loadDiaryFromFile();

        frame.setVisible(true);
    }

    // 📖 選択した日記を表示
    private void displaySelectedDiary() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) diaryTree.getLastSelectedPathComponent();
        if (selectedNode == null || selectedNode.getParent() == root) return;

        String date = selectedNode.toString();
        for (DiaryEntry entry : diaryMap.getOrDefault(getYearMonth(date), new ArrayList<>())) {
            if (entry.date.equals(date)) {
                diaryContent.setText(entry.content);
                break;
            }
        }
    }

    // ➕ 日記を追加
    private void addDiaryEntry() {
        String date = JOptionPane.showInputDialog(frame, "📅 日付を入力 (例: 2025-03-12):");
        if (date == null || date.trim().isEmpty() || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(frame, "⚠️ 正しい日付形式（YYYY-MM-DD）で入力してください。");
            return;
        }

        String content = JOptionPane.showInputDialog(frame, "📝 日記の内容を入力:");
        if (content == null || content.trim().isEmpty()) return;

        DiaryEntry newEntry = new DiaryEntry(date, content);
        String yearMonth = getYearMonth(date);

        diaryMap.putIfAbsent(yearMonth, new ArrayList<>());
        diaryMap.get(yearMonth).add(newEntry);

        updateTree();
        saveDiaryToFile();
    }

    // ❌ 日記を削除
    private void deleteDiaryEntry() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) diaryTree.getLastSelectedPathComponent();
        if (selectedNode == null || selectedNode.getParent() == root) return;

        String date = selectedNode.toString();
        String yearMonth = getYearMonth(date);

        if (diaryMap.containsKey(yearMonth)) {
            diaryMap.get(yearMonth).removeIf(entry -> entry.date.equals(date));
            updateTree();
            saveDiaryToFile();
            diaryContent.setText("");
        }
    }

    // 📅 ツリーの更新（年月フォルダもソート）
    private void updateTree() {
        root.removeAllChildren();

        List<String> sortedYearMonths = new ArrayList<>(diaryMap.keySet());
        sortedYearMonths.sort(currentSortOrder.equals("日付昇順") ? Comparator.naturalOrder() : Comparator.reverseOrder());

        for (String yearMonth : sortedYearMonths) {
            DefaultMutableTreeNode monthNode = new DefaultMutableTreeNode(yearMonth);

            List<DiaryEntry> entries = new ArrayList<>(diaryMap.get(yearMonth));
            sortEntries(entries);

            for (DiaryEntry entry : entries) {
                monthNode.add(new DefaultMutableTreeNode(entry.date));
            }
            root.add(monthNode);
        }

        treeModel.reload();
    }

    // 📋 日記エントリのソート
    private void sortEntries(List<DiaryEntry> entries) {
        entries.sort(currentSortOrder.equals("日付昇順") ?
                Comparator.comparing(e -> e.date) :
                Comparator.comparing(e -> e.date, Comparator.reverseOrder()));
    }

    // 🔄 ファイルから日記を読み込む
    private void loadDiaryFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("::", 2);
                if (parts.length == 2) {
                    String date = parts[0];
                    String content = parts[1];
                    if (date.length() < 10) continue; // データ不正防止

                    String yearMonth = getYearMonth(date);

                    diaryMap.putIfAbsent(yearMonth, new ArrayList<>());
                    diaryMap.get(yearMonth).add(new DiaryEntry(date, content));
                }
            }
            updateTree();
        } catch (FileNotFoundException e) {
            System.out.println("📂 新しい日記ファイルを作成します。");
        } catch (IOException e) {
            System.out.println("⚠️ ファイル読み込みエラー");
        }
    }

    // 💾 日記をファイルに保存
    private void saveDiaryToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (List<DiaryEntry> entries : diaryMap.values()) {
                for (DiaryEntry entry : entries) {
                    writer.write(entry.date + "::" + entry.content);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ ファイル保存エラー");
        }
    }

    // 🏷 日付から「YYYY-MM」形式を取得
    private String getYearMonth(String date) {
        return date.length() >= 7 ? date.substring(0, 7) : "";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DiaryappGUI::new);
    }
}