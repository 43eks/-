package pg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Diaryapp {
    private static final String FILE_NAME = "diary.txt";
    private static List<DiaryEntry> diaryEntries = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    // 📖 日記のデータ構造
    static class DiaryEntry {
        String date;
        String content;

        public DiaryEntry(String date, String content) {
            this.date = date;
            this.content = content;
        }

        @Override
        public String toString() {
            return date + ": " + content;
        }
    }

    public static void main(String[] args) {
        loadDiaryFromFile();
        while (true) {
            System.out.println("\n📔 === 日記アプリ ===");
            System.out.println("1. 日記を書く");
            System.out.println("2. 日記を表示");
            System.out.println("3. 日記を検索");
            System.out.println("4. 日記を削除");
            System.out.println("5. 終了");
            System.out.print("選択してください: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // バッファクリア

            switch (choice) {
                case 1 -> addDiaryEntry();
                case 2 -> listDiaryEntries();
                case 3 -> searchDiaryEntries();
                case 4 -> deleteDiaryEntry();
                case 5 -> {
                    saveDiaryToFile();
                    System.out.println("📌 データを保存しました。終了します。");
                    return;
                }
                default -> System.out.println("⚠️ 無効な入力です。もう一度選んでください。");
            }
        }
    }

    // 🔄 ファイルから日記データを読み込む
    private static void loadDiaryFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("::", 2);
                if (parts.length == 2) {
                    diaryEntries.add(new DiaryEntry(parts[0], parts[1]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("📂 新しい日記ファイルを作成します。");
        } catch (IOException e) {
            System.out.println("⚠️ ファイルの読み込みエラー。");
        }
    }

    // 💾 ファイルに日記を保存
    private static void saveDiaryToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (DiaryEntry entry : diaryEntries) {
                writer.write(entry.date + "::" + entry.content);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("⚠️ ファイル保存中にエラーが発生しました。");
        }
    }

    // ➕ 日記を追加
    private static void addDiaryEntry() {
        System.out.print("📅 日付を入力 (例: 2025-03-12): ");
        String date = scanner.nextLine().trim();
        if (date.isEmpty()) {
            System.out.println("⚠️ 日付を入力してください。");
            return;
        }

        System.out.print("📝 日記の内容を入力: ");
        String content = scanner.nextLine().trim();
        if (content.isEmpty()) {
            System.out.println("⚠️ 内容を入力してください。");
            return;
        }

        diaryEntries.add(new DiaryEntry(date, content));
        saveDiaryToFile();
        System.out.println("✅ 日記が追加されました！");
    }

    // 📜 日記を一覧表示
    private static void listDiaryEntries() {
        if (diaryEntries.isEmpty()) {
            System.out.println("📭 日記がまだありません。");
            return;
        }

        System.out.println("\n📖 あなたの日記一覧:");
        for (int i = 0; i < diaryEntries.size(); i++) {
            System.out.println((i + 1) + ". " + diaryEntries.get(i).date);
        }

        System.out.print("\n📌 詳細を表示する日記の番号を選択 (0で戻る): ");
        int index = scanner.nextInt();
        scanner.nextLine(); // バッファクリア

        if (index > 0 && index <= diaryEntries.size()) {
            System.out.println("\n📖 " + diaryEntries.get(index - 1).toString());
        } else {
            System.out.println("🔙 メニューに戻ります。");
        }
    }

    // 🔍 日記を検索
    private static void searchDiaryEntries() {
        System.out.print("🔍 検索キーワードを入力 (日付または単語): ");
        String keyword = scanner.nextLine().trim();

        boolean found = false;
        for (DiaryEntry entry : diaryEntries) {
            if (entry.date.contains(keyword) || entry.content.contains(keyword)) {
                System.out.println("\n📖 " + entry.toString());
                found = true;
            }
        }

        if (!found) {
            System.out.println("⚠️ 検索結果がありません。");
        }
    }

    // ❌ 日記を削除
    private static void deleteDiaryEntry() {
        if (diaryEntries.isEmpty()) {
            System.out.println("📭 削除できる日記がありません。");
            return;
        }

        listDiaryEntries();
        System.out.print("\n🗑 削除する日記の番号を選択 (0でキャンセル): ");
        int index = scanner.nextInt();
        scanner.nextLine(); // バッファクリア

        if (index > 0 && index <= diaryEntries.size()) {
            System.out.print("⚠️ 本当に削除しますか？ (y/n): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("y")) {
                diaryEntries.remove(index - 1);
                saveDiaryToFile();
                System.out.println("🗑 日記を削除しました。");
            } else {
                System.out.println("🚫 削除をキャンセルしました。");
            }
        } else {
            System.out.println("🔙 メニューに戻ります。");
        }
    }
}