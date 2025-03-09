package pg;
	// レイアウト用
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//public static void main(String[] args) {
// TODO 自動生成されたメソッド・スタブ
// Swingライブラリをインポート
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CalculatorGUI extends JFrame implements ActionListener {
    private JTextField textField; // 数値や結果を表示するテキストフィールド
    private String operator;      // 現在の演算子（+, -, *, /）
    private double num1, num2, result; // 計算用の変数

    public CalculatorGUI() {
        // ==============================
        // フレーム（ウィンドウ）の設定
        // ==============================
        setTitle("電卓");  // ウィンドウタイトル
        setSize(300, 400); // ウィンドウサイズ（幅x高さ）
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 閉じるボタンで終了
        setLayout(new BorderLayout()); // レイアウトを設定

        // ==============================
        // 数字・計算結果の表示部分
        // ==============================
        textField = new JTextField();
        textField.setHorizontalAlignment(JTextField.RIGHT); // 右寄せ
        textField.setEditable(false); // ユーザーが直接編集できないようにする
        add(textField, BorderLayout.NORTH); // 上部に配置

        // ==============================
        // ボタン配置（グリッドレイアウト）
        // ==============================
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 5, 5)); // 4行×4列のグリッド

        // ボタンのラベル一覧
        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "C", "0", "=", "+"
        };

        // 各ボタンを作成してパネルに追加
        for (String text : buttons) {
            JButton button = new JButton(text);
            button.addActionListener(this); // クリックイベントを追加
            panel.add(button);
        }

        add(panel, BorderLayout.CENTER); // ボタンパネルを中央に配置
        setVisible(true); // ウィンドウを表示
    }

    // ==============================
    // ボタンがクリックされたときの処理
    // ==============================
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand(); // クリックされたボタンのラベルを取得

        if ("0123456789".contains(command)) {  // 数字ボタンが押された場合
            textField.setText(textField.getText() + command);
        } else if ("+-*/".contains(command)) { // 演算子ボタンが押された場合
            num1 = Double.parseDouble(textField.getText()); // 1つ目の数値を取得
            operator = command; // 選択された演算子を保存
            textField.setText(""); // テキストフィールドをクリア
        } else if ("=".equals(command)) { // 「=」ボタンが押された場合
            num2 = Double.parseDouble(textField.getText()); // 2つ目の数値を取得

            // 演算処理
            switch (operator) {
                case "+": result = num1 + num2; break;
                case "-": result = num1 - num2; break;
                case "*": result = num1 * num2; break;
                case "/": 
                    if (num2 == 0) { // 0で割るのを防ぐ
                        textField.setText("エラー");
                        return;
                    }
                    result = num1 / num2; 
                    break;
            }
            textField.setText(String.valueOf(result)); // 計算結果を表示
        } else if ("C".equals(command)) { // 「C」ボタンが押された場合（クリア）
            textField.setText(""); // 表示をクリア
            num1 = num2 = result = 0; // 変数もリセット
            operator = "";
        }
    }

    // ==============================
    // メインメソッド（プログラムの開始点）
    // ==============================
    public static void main(String[] args) {
        new CalculatorGUI(); // 電卓GUIを起動
    }
}
