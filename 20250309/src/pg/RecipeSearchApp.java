package pg;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

class Recipe {
    String name;
    List<String> ingredients;
    String instructions;

    public Recipe(String name, List<String> ingredients, String instructions) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    // レシピが与えられた食材で作れるか判定
    public boolean canMake(Set<String> availableIngredients) {
        return availableIngredients.containsAll(ingredients);
    }

    // 食材不足を表示
    public void displayMissingIngredients(Set<String> availableIngredients) {
        Set<String> missingIngredients = new HashSet<>(ingredients);
        missingIngredients.removeAll(availableIngredients);

        if (!missingIngredients.isEmpty()) {
            System.out.println("⚠️ 不足している食材: " + missingIngredients);
        } else {
            display();  // 全て揃っていればレシピを表示
        }
    }

    // レシピ情報を表示
    public void display() {
        System.out.println("\n🍽 レシピ: " + name);
        System.out.println("📌 材料: " + ingredients);
        System.out.println("📝 作り方: " + instructions);
    }
}

public class RecipeSearchApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // レシピデータ（仮のデータベース）
        List<Recipe> recipes = Arrays.asList(
            new Recipe("パンケーキ", Arrays.asList("卵", "牛乳", "小麦粉"), "材料を混ぜて焼く"),
            new Recipe("オムレツ", Arrays.asList("卵", "牛乳", "塩"), "卵と牛乳を混ぜて焼く"),
            new Recipe("フレンチトースト", Arrays.asList("卵", "牛乳", "パン"), "パンを卵液に浸して焼く"),
            new Recipe("味噌汁", Arrays.asList("味噌", "豆腐", "だし"), "だし汁に味噌と具材を入れる")
        );

        // =============================
        // 食材の入力
        // =============================
        System.out.println("🛒 冷蔵庫にある食材をカンマ区切りで入力してください:");
        String input = scanner.nextLine();
        Set<String> userIngredients = new HashSet<>(Arrays.asList(input.split("\\s*,\\s*"))); // 食材をセットに変換

        while (true) {
            // =============================
            // レシピ検索
            // =============================
            System.out.println("\n🔎 作れるレシピを検索中...\n");
            boolean found = false;

            for (Recipe recipe : recipes) {
                recipe.displayMissingIngredients(userIngredients);
                found = true;  // 少なくとも1つのレシピが表示されたらtrue
            }

            if (!found) {
                System.out.println("😢 該当するレシピが見つかりませんでした。");
            }

            // 食材を追加するかどうかの確認
            System.out.println("\n🍴 もう一度食材を追加しますか？(はい/いいえ):");
            String answer = scanner.nextLine();
            if (answer.equals("はい")) {
                System.out.println("🛒 追加する食材をカンマ区切りで入力してください:");
                input = scanner.nextLine();
                userIngredients.addAll(Arrays.asList(input.split("\\s*,\\s*")));  // 追加した食材をセットに加える
            } else {
                break;  // 追加しない場合は終了
            }
        }

        scanner.close();
    }
}