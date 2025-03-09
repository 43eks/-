package pg;
	//public static void main(String[] args) {
	// TODO 自動生成されたメソッド・スタブ

	import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

		public class CalculatorGUI extends JFrame implements ActionListener {
		    private JTextField textField;
		    private String operator;
		    private double num1, num2, result;

		    public CalculatorGUI() {
		        // フレームの設定
		        setTitle("電卓");
		        setSize(300, 400);
		        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        setLayout(new BorderLayout());

		        // 数字表示部分
		        textField = new JTextField();
		        textField.setHorizontalAlignment(JTextField.RIGHT);
		        textField.setEditable(false);
		        add(textField, BorderLayout.NORTH);

		        // ボタン配置
		        JPanel panel = new JPanel();
		        panel.setLayout(new GridLayout(4, 4, 5, 5));

		        String[] buttons = {
		            "7", "8", "9", "/",
		            "4", "5", "6", "*",
		            "1", "2", "3", "-",
		            "C", "0", "=", "+"
		        };

		        for (String text : buttons) {
		            JButton button = new JButton(text);
		            button.addActionListener(this);
		            panel.add(button);
		        }

		        add(panel, BorderLayout.CENTER);
		        setVisible(true);
		    }

		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String command = e.getActionCommand();

		        if ("0123456789".contains(command)) {  // 数字ボタン
		            textField.setText(textField.getText() + command);
		        } else if ("+-*/".contains(command)) { // 演算子ボタン
		            num1 = Double.parseDouble(textField.getText());
		            operator = command;
		            textField.setText("");
		        } else if ("=".equals(command)) { // 計算実行
		            num2 = Double.parseDouble(textField.getText());
		            switch (operator) {
		                case "+": result = num1 + num2; break;
		                case "-": result = num1 - num2; break;
		                case "*": result = num1 * num2; break;
		                case "/": 
		                    if (num2 == 0) {
		                        textField.setText("エラー");
		                        return;
		                    }
		                    result = num1 / num2; 
		                    break;
		            }
		            textField.setText(String.valueOf(result));
		        } else if ("C".equals(command)) { // クリア
		            textField.setText("");
		            num1 = num2 = result = 0;
		            operator = "";
		        }
		    }

		    public static void main(String[] args) {
		        new CalculatorGUI();
		    }
		}
