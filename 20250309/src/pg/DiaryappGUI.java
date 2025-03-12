package pg;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
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
    private Map<String, List<DiaryEntry>> diaryMap1 = new HashMap<>();
    private static final String FILE_NAME = "diary.txt";

    static class DiaryEntry {
        String date;
        String content;

        public DiaryEntry(String date, String content) {
            this.date = date;
            this.content = content;
        }
    }

    public DiaryappGUI() {
        try {
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

            // 🛠 ボタンパネル
            JButton addButton = new JButton("追加");
            addButton.addActionListener(e -> addDiaryEntry());

            JButton deleteButton = new JButton("削除");
            deleteButton.addActionListener(e -> deleteDiaryEntry());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(addButton);
            buttonPanel.add(deleteButton);

            // 📦 レイアウト設定
            frame.add(treeScrollPane, BorderLayout.WEST);
            frame.add(textScrollPane, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            // ファイルから日記を読み込む
            loadDiaryFromFile();

            System.out.println("フレーム表示処理実行");
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "UIの初期化中にエラーが発生しました", "エラー", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displaySelectedDiary() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) diaryTree.getLastSelectedPathComponent();
        if (selectedNode == null || selectedNode.getParent() == root) return; // 月は無視

        String date = selectedNode.toString();
        for (DiaryEntry entry : diaryMap1.getOrDefault(getYearMonth(date), new ArrayList<>())) {
            if (entry.date.equals(date)) {
                diaryContent.setText(entry.content);
                break;
            }
        }
    }

    private void addDiaryEntry() {
        String date = JOptionPane.showInputDialog(frame, "📅 日付を入力 (例: 2025-03-12):");
        if (date == null || date.trim().isEmpty()) return;

        String content = JOptionPane.showInputDialog(frame, "📝 日記の内容を入力:");
        if (content == null || content.trim().isEmpty()) return;

        DiaryEntry newEntry = new DiaryEntry(date, content);
        String yearMonth = getYearMonth(date);

        diaryMap1.putIfAbsent(yearMonth, new ArrayList<>());
        diaryMap1.get(yearMonth).add(newEntry);

        updateTree();
        saveDiaryToFile();
    }

    private void deleteDiaryEntry() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) diaryTree.getLastSelectedPathComponent();
        if (selectedNode == null || selectedNode.getParent() == root) return;

        String date = selectedNode.toString();
        String yearMonth = getYearMonth(date);

        if (diaryMap1.containsKey(yearMonth)) {
            diaryMap1.get(yearMonth).removeIf(entry -> entry.date.equals(date));
            updateTree();
            saveDiaryToFile();
            diaryContent.setText("");
        }
    }

    private void updateTree() {
        root.removeAllChildren();

        for (String yearMonth : diaryMap1.keySet()) {
            DefaultMutableTreeNode monthNode = new DefaultMutableTreeNode(yearMonth);
            for (DiaryEntry entry : diaryMap1.get(yearMonth)) {
                monthNode.add(new DefaultMutableTreeNode(entry.date));
            }
            root.add(monthNode);
        }

        treeModel.reload();
    }

    private void loadDiaryFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("::", 2);
                if (parts.length == 2) {
                    String date = parts[0];
                    String content = parts[1];
                    String yearMonth = getYearMonth(date);

                    diaryMap1.putIfAbsent(yearMonth, new ArrayList<>());
                    diaryMap1.get(yearMonth).add(new DiaryEntry(date, content));
                }
            }
            updateTree();
        } catch (FileNotFoundException e) {
            System.out.println("📂 新しい日記ファイルを作成します。");
        } catch (IOException e) {
            System.out.println("⚠️ ファイル読み込みエラー");
        }
    }

    private void saveDiaryToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (List<DiaryEntry> entries : diaryMap1.values()) {
                for (DiaryEntry entry : entries) {
                    writer.write(entry.date + "::" + entry.content);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ ファイル保存エラー");
        }
    }

    private String getYearMonth(String date) {
        return date.substring(0, 7); // "2025-03-12" → "2025-03"
    }

    public static void main(String[] args) {
        System.out.println("アプリを起動します");
        SwingUtilities.invokeLater(DiaryappGUI::new);
    }
}