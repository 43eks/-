package pg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

class Recipe {
    String name;
    List<String> ingredients;
    String instructions;
    String category;  // カテゴリを追加

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
        System.out.println("🗂 カテゴリ: " + category);  // カテゴリも表示
    }
}

public class RecipeSearchApp {
    private static final Recipe[] recipes = null;

	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // レシピデータ（仮のデータベース）
     //   List<Recipe> recipes = Arrays.asList(
     //     new Recipe("パンケーキ", Arrays.asList("卵", "牛乳", "小麦粉"), "材料を混ぜて焼く"),
     //     new Recipe("オムレツ", Arrays.asList("卵", "牛乳", "塩"), "卵と牛乳を混ぜて焼く"),
     //     new Recipe("フレンチトースト", Arrays.asList("卵", "牛乳", "パン"), "パンを卵液に浸して焼く"),
     //     new Recipe("味噌汁", Arrays.asList("味噌", "豆腐", "だし"), "だし汁に味噌と具材を入れる")
     // );

        // 食材ごとの栄養情報 (仮のデータ)
        Map<String, String> nutritionInfo = new HashMap<>();
        nutritionInfo.put("卵", "カロリー: 70 kcal, タンパク質: 6g, 脂質: 5g");
        nutritionInfo.put("牛乳", "カロリー: 60 kcal, タンパク質: 3g, 脂質: 3g");
        nutritionInfo.put("小麦粉", "カロリー: 110 kcal, タンパク質: 3g, 脂質: 0.5g");
        nutritionInfo.put("塩", "カロリー: 0 kcal, タンパク質: 0g, 脂質: 0g");
        nutritionInfo.put("パン", "カロリー: 80 kcal, タンパク質: 3g, 脂質: 1g");
        nutritionInfo.put("味噌", "カロリー: 40 kcal, タンパク質: 2g, 脂質: 1g");
        nutritionInfo.put("豆腐", "カロリー: 70 kcal, タンパク質: 8g, 脂質: 4g");
        nutritionInfo.put("だし", "カロリー: 10 kcal, タンパク質: 2g, 脂質: 0g");

        // ユーザーのお気に入りレシピリスト
        List<Recipe> favoriteRecipes = new ArrayList<>();

        // =============================
        // 食材の入力
        // =============================
        System.out.println("🛒 冷蔵庫にある食材をカンマ区切りで入力してください:");
        String input = scanner.nextLine();
        Set<String> userIngredients = new HashSet<>(Arrays.asList(input.split("\\s*,\\s*"))); // 食材をセットに変換

        while (true) {
            // =============================
            // 食材の栄養情報表示
            // =============================
            System.out.println("\n🍎 食材の栄養情報:");
            for (String ingredient : userIngredients) {
                if (nutritionInfo.containsKey(ingredient)) {
                    System.out.println(ingredient + ": " + nutritionInfo.get(ingredient));
                } else {
                    System.out.println(ingredient + ": 栄養情報がありません。");
                }
            }

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

            // お気に入りに追加するか尋ねる
            System.out.println("\nこのレシピをお気に入りに追加しますか？(はい/いいえ):");
            String answer = scanner.nextLine();
            if (answer.equals("はい")) {
                System.out.println("お気に入りに追加するレシピ名を入力してください:");
                String favoriteRecipeName = scanner.nextLine();
                Recipe favoriteRecipe = null;
                for (Recipe recipe : recipes) {
                    if (recipe.name.equals(favoriteRecipeName)) {
                        favoriteRecipe = recipe;
                        break;
                    }
                }
                if (favoriteRecipe != null) {
                    favoriteRecipes.add(favoriteRecipe);
                    System.out.println(favoriteRecipeName + " がお気に入りに追加されました。");
                } else {
                    System.out.println("その名前のレシピは存在しません。");
                }
             // 検索履歴
                List<String> searchHistory = new ArrayList<>();

                // 検索したレシピを履歴に追加
                System.out.println("\nレシピ名を入力して検索:");
                String searchRecipe = scanner.nextLine();
                searchHistory.add(searchRecipe);

                // 検索履歴を表示
                System.out.println("\n🔙 過去の検索履歴:");
                for (String history : searchHistory) {
                    System.out.println(history);
                }
                
            }

            // お気に入りレシピの表示
            System.out.println("\n💖 お気に入りのレシピ:");
            if (!favoriteRecipes.isEmpty()) {
                for (Recipe recipe : favoriteRecipes) {
                    recipe.display();
                }
            } else {
                System.out.println("お気に入りレシピはありません。");
            }

            // 食材を追加するかどうかの確認
            System.out.println("\n🍴 もう一度食材を追加しますか？(はい/いいえ):");
            String moreIngredients = scanner.nextLine();
            if (moreIngredients.equals("はい")) {
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