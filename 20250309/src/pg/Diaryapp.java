package pg;

import java.io.*;
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
    private static final String FILE_NAME = "diary.txt"; // データを保存するファイル名
    private static List<DiaryEntry> diaryEntries = new ArrayList<>(); // 日記リスト
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadDiaryFromFile(); // アプリ起動時にファイルからデータを読み込む

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

            switch (option) {
                case 1:
                    addDiaryEntry();
                    break;
                case 2:
                    viewAllEntries();
                    break;
                case 3:
                    editDiaryEntry();
                    break;
                case 4:
                    deleteDiaryEntry();
                    break;
                case 5:
                    saveDiaryToFile(); // 終了時に日記をファイルに保存
                    System.out.println("日記アプリケーションを終了します。");
                    return;
                default:
                    System.out.println("無効なオプションです。もう一度選んでください。");
            }
        }
    }

    // 日記を追加するメソッド
    private static void addDiaryEntry() {
        System.out.print("日付を入力してください (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("内容を入力してください: ");
        String content = scanner.nextLine();

        DiaryEntry entry = new DiaryEntry(date, content);
        diaryEntries.add(entry);
        saveDiaryToFile(); // 追加後に保存
        System.out.println("日記が追加されました！");
    }

    // すべての日記を表示するメソッド
    private static void viewAllEntries() {
        if (diaryEntries.isEmpty()) {
            System.out.println("保存されている日記はありません。");
            return;
        }

        for (DiaryEntry entry : diaryEntries) {
            System.out.println("\n" + entry);
        }
    }

    // 日記を編集するメソッド
    private static void editDiaryEntry() {
        System.out.print("編集したい日記の日付を入力してください (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        DiaryEntry entry = findDiaryEntryByDate(date);
        if (entry != null) {
            System.out.print("新しい内容を入力してください: ");
            String newContent = scanner.nextLine();
            entry.setContent(newContent);
            saveDiaryToFile(); // 編集後に保存
            System.out.println("日記が更新されました！");
        } else {
            System.out.println("指定された日付の日記は見つかりませんでした。");
        }
    }

    // 日記を削除するメソッド
    private static void deleteDiaryEntry() {
        System.out.print("削除したい日記の日付を入力してください (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        DiaryEntry entry = findDiaryEntryByDate(date);
        if (entry != null) {
            diaryEntries.remove(entry);
            saveDiaryToFile(); // 削除後に保存
            System.out.println("日記が削除されました！");
        } else {
            System.out.println("指定された日付の日記は見つかりませんでした。");
        }
    }

    // ファイルから日記を読み込むメソッド
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
            System.out.println("データファイルが見つかりませんでした。新しいファイルを作成します。");
        } catch (IOException e) {
            System.out.println("ファイルの読み込み中にエラーが発生しました。");
        }
    }

    // ファイルに日記を保存するメソッド
    private static void saveDiaryToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (DiaryEntry entry : diaryEntries) {
                writer.write(entry.getDate() + "::" + entry.getContent());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("ファイルの保存中にエラーが発生しました。");
        }
    }

    // 日付で日記エントリを検索するメソッド
    private static DiaryEntry findDiaryEntryByDate(String date) {
        for (DiaryEntry entry : diaryEntries) {
            if (entry.getDate().equals(date)) {
                return entry;
            }
        }
        return null;
    }
}}