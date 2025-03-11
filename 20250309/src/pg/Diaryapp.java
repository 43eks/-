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
        return "日付: " + date + "\n" + "内容: " + content;
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
            System.out.println("\n日記アプリケーション");
            System.out.println("1. 日記を追加");
            System.out.println("2. すべての日記を表示");
            System.out.println("3. 日記を編集");
            System.out.println("4. 日記を削除");
            System.out.println("5. 終了");
            System.out.print("オプションを選んでください: ");
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
                    System.out.println("日記アプリケーションを終了します。");
                    return; // アプリケーション終了
                default:
                    System.out.println("無効なオプションです。もう一度選んでください。"); // 無効な選択肢
            }
        }
    }

    // 日記を追加するメソッド
    private static void addDiaryEntry() {
        System.out.print("日付を入力してください (YYYY-MM-DD): ");
        String date = scanner.nextLine(); // 日付の入力を受け取る
        System.out.print("内容を入力してください: ");
        String content = scanner.nextLine(); // 内容の入力を受け取る

        // DiaryEntryオブジェクトを作成し、リストに追加
        DiaryEntry entry = new DiaryEntry(date, content);
        diaryEntries.add(entry);
        System.out.println("日記が追加されました！");
    }

    // すべての日記エントリを表示するメソッド
    private static void viewAllEntries() {
        if (diaryEntries.isEmpty()) { // 日記がない場合
            System.out.println("保存されている日記はありません。");
            return;
        }

        // リスト内のすべての日記を表示
        for (DiaryEntry entry : diaryEntries) {
            System.out.println("\n" + entry); // DiaryEntryオブジェクトをtoString()メソッドで表示
        }
    }

    // 日記を編集するメソッド
    private static void editDiaryEntry() {
        System.out.print("編集したい日記の日付を入力してください (YYYY-MM-DD): ");
        String date = scanner.nextLine(); // 編集したい日記の日付を入力

        // 日付に一致する日記を検索
        DiaryEntry entry = findDiaryEntryByDate(date);
        if (entry != null) {
            // 内容の更新を受け取る
            System.out.print("新しい内容を入力してください: ");
            String newContent = scanner.nextLine();
            entry.setContent(newContent); // 内容を更新
            System.out.println("日記が更新されました！");
        } else {
            System.out.println("指定された日付の日記は見つかりませんでした。");
        }
    }

    // 日記を削除するメソッド
    private static void deleteDiaryEntry() {
        System.out.print("削除したい日記の日付を入力してください (YYYY-MM-DD): ");
        String date = scanner.nextLine(); // 削除したい日記の日付を入力

        // 日付に一致する日記を検索
        DiaryEntry entry = findDiaryEntryByDate(date);
        if (entry != null) {
            diaryEntries.remove(entry); // 日記をリストから削除
            System.out.println("日記が削除されました！");
        } else {
            System.out.println("指定された日付の日記は見つかりませんでした。");
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