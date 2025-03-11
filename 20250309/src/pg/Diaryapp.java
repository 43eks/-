package pg;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// DiaryEntryクラス：1つの日記のエントリを表す
class DiaryEntry {
    private String date; // 日付
    private String content; // 内容

    // コンストラクタ：日付と内容を設定する
    public DiaryEntry(String date, String content) {
        this.date = date;
        this.content = content;
    }

    // ゲッター：日付を取得
    public String getDate() {
        return date;
    }

    // ゲッター：内容を取得
    public String getContent() {
        return content;
    }

    // セッター：内容を更新
    public void setContent(String content) {
        this.content = content;
    }

    // 日記エントリを表示するためのtoStringメソッド
    @Override
    public String toString() {
        return "Date: " + date + "\n" + "Content: " + content;
    }
}

// DiaryAppクラス：日記アプリケーションのメインクラス
public class Diaryapp {
    // 日記エントリを保存するためのリスト
    private static List<DiaryEntry> diaryEntries = new ArrayList<>();
    // ユーザーの入力を受け取るためのScanner
    private static Scanner scanner = new Scanner(System.in);

    // アプリケーションのエントリーポイント
    public static void main(String[] args) {
        // メニューを表示して、ユーザーの選択を受け付ける
        while (true) {
            System.out.println("\nDiary Application");
            System.out.println("1. Add Diary Entry");
            System.out.println("2. View All Entries");
            System.out.println("3. Edit Diary Entry");
            System.out.println("4. Delete Diary Entry");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // 改行コードを消費

            // ユーザーの選択に応じて処理を分岐
            switch (option) {
                case 1:
                    addDiaryEntry(); // 日記を追加
                    break;
                case 2:
                    viewAllEntries(); // すべての日記を表示
                    break;
                case 3:
                    editDiaryEntry(); // 日記を編集
                    break;
                case 4:
                    deleteDiaryEntry(); // 日記を削除
                    break;
                case 5:
                    System.out.println("Exiting the Diary Application.");
                    return; // アプリケーション終了
                default:
                    System.out.println("Invalid option. Try again."); // 無効な選択肢
            }
        }
    }

    // 日記を追加するメソッド
    private static void addDiaryEntry() {
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine(); // 日付の入力を受け取る
        System.out.print("Enter content: ");
        String content = scanner.nextLine(); // 内容の入力を受け取る

        // DiaryEntryオブジェクトを作成し、リストに追加
        DiaryEntry entry = new DiaryEntry(date, content);
        diaryEntries.add(entry);
        System.out.println("Diary entry added successfully!");
    }

    // すべての日記エントリを表示するメソッド
    private static void viewAllEntries() {
        if (diaryEntries.isEmpty()) { // 日記がない場合
            System.out.println("No diary entries available.");
            return;
        }

        // リスト内のすべての日記を表示
        for (DiaryEntry entry : diaryEntries) {
            System.out.println("\n" + entry); // DiaryEntryオブジェクトをtoString()メソッドで表示
        }
    }

    // 日記を編集するメソッド
    private static void editDiaryEntry() {
        System.out.print("Enter the date of the entry you want to edit (YYYY-MM-DD): ");
        String date = scanner.nextLine(); // 編集したい日記の日付を入力

        // 日付に一致する日記を検索
        DiaryEntry entry = findDiaryEntryByDate(date);
        if (entry != null) {
            // 内容の更新を受け取る
            System.out.print("Enter new content: ");
            String newContent = scanner.nextLine();
            entry.setContent(newContent); // 内容を更新
            System.out.println("Diary entry updated successfully!");
        } else {
            System.out.println("Entry not found for the given date.");
        }
    }

    // 日記を削除するメソッド
    private static void deleteDiaryEntry() {
        System.out.print("Enter the date of the entry you want to delete (YYYY-MM-DD): ");
        String date = scanner.nextLine(); // 削除したい日記の日付を入力

        // 日付に一致する日記を検索
        DiaryEntry entry = findDiaryEntryByDate(date);
        if (entry != null) {
            diaryEntries.remove(entry); // 日記をリストから削除
            System.out.println("Diary entry deleted successfully!");
        } else {
            System.out.println("Entry not found for the given date.");
        }
    }

    // 日付で日記エントリを検索するメソッド
    private static DiaryEntry findDiaryEntryByDate(String date) {
        // 日付が一致する日記をリスト内で探す
        for (DiaryEntry entry : diaryEntries) {
            if (entry.getDate().equals(date)) {
                return entry;
            }
        }
        return null; // 見つからない場合はnullを返す
    }
}
