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
    String category; // カテゴリ追加

    public Recipe(String name, List<String> ingredients, String instructions, String category) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.category = category;
    }

    // レシピが与えられた食材で作れるか判定
    public boolean canMake(Set<String> availableIngredients) {
        return availableIngredients.containsAll(ingredients);
    }

    // 足りない食材を表示
    public Set<String> getMissingIngredients(Set<String> availableIngredients) {
        Set<String> missingIngredients = new HashSet<>(ingredients);
        missingIngredients.removeAll(availableIngredients);
        return missingIngredients;
    }

    // レシピ情報を表示
    public void display() {
        System.out.println("\n🍽 レシピ: " + name);
        System.out.println("📌 材料: " + ingredients);
        System.out.println("📝 作り方: " + instructions);
        System.out.println("📅 カテゴリ: " + category);
    }
}

public class RecipeSearchApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // レシピデータ（仮のデータベース）
        List<Recipe> recipes = Arrays.asList(
            new Recipe("パンケーキ", Arrays.asList("卵", "牛乳", "小麦粉"), "材料を混ぜて焼く", "朝食"),
            new Recipe("オムレツ", Arrays.asList("卵", "牛乳", "塩"), "卵と牛乳を混ぜて焼く", "朝食"),
            new Recipe("フレンチトースト", Arrays.asList("卵", "牛乳", "パン"), "パンを卵液に浸して焼く", "朝食"),
            new Recipe("味噌汁", Arrays.asList("味噌", "豆腐", "だし"), "だし汁に味噌と具材を入れる", "昼食")
        );

        // =============================
        // カテゴリの入力
        // =============================
        System.out.println("🔍 検索したいレシピのカテゴリを入力してください（例: 朝食、昼食、夕食）:");
        String categoryInput = scanner.nextLine().trim();

        // =============================
        // 食材の入力
        // =============================
        System.out.println("🛒 冷蔵庫にある食材をカンマ区切りで入力してください:");
        String input = scanner.nextLine();
        Set<String> userIngredients = new HashSet<>(Arrays.asList(input.split("\\s*,\\s*"))); // 食材をセットに変換

        // =============================
        // レシピ検索
        // =============================
        System.out.println("\n🔎 作れるレシピを検索中...\n");
        boolean found = false;

        for (Recipe recipe : recipes) {
            if (recipe.category.equals(categoryInput) || categoryInput.isEmpty()) { // カテゴリでフィルタリング
                if (recipe.canMake(userIngredients)) {
                    recipe.display();
                    found = true;
                } else {
                    // 足りない食材を表示
                    Set<String> missingIngredients = recipe.getMissingIngredients(userIngredients);
                    if (!missingIngredients.isEmpty()) {
                        System.out.println("😢 " + recipe.name + "を作るには、" + missingIngredients + "が足りません。");
                    }
                }
            }
        }

        if (!found) {
            System.out.println("😢 該当するレシピが見つかりませんでした。");
        }

        scanner.close();
    }
}