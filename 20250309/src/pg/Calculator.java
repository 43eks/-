package pg;

//public class 計算機 {
import java.util.Scanner;

public class Calculator {
	    public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);

	        System.out.print("1つ目の数値を入力: ");
	        double num1 = scanner.nextDouble();

	        System.out.print("演算子を入力 (+, -, *, /): ");
	        char operator = scanner.next().charAt(0);

	        System.out.print("2つ目の数値を入力: ");
	        double num2 = scanner.nextDouble();

	        double result;
	        switch (operator) {
	            case '+':
	                result = num1 + num2;
	                break;
	            case '-':
	                result = num1 - num2;
	                break;
	            case '*':
	                result = num1 * num2;
	                break;
	            case '/':
	                if (num2 != 0) {
	                    result = num1 / num2;
	                } else {
	                    System.out.println("エラー: 0で割ることはできません。");
	                    return;
	                }
	                break;
	            default:
	                System.out.println("エラー: 無効な演算子です。");
	                return;
	        }

	        System.out.println("計算結果: " + result);
	        scanner.close();
	    }
	}
