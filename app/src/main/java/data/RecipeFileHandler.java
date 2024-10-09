package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RecipeFileHandler {
    private String filePath;

    public RecipeFileHandler() {
        filePath = "app/src/main/resources/recipes.txt";
    }

    public RecipeFileHandler(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 設問1: 一覧表示機能
     * recipes.txtからレシピデータを読み込み、それをリスト形式で返します。 <br>
     * IOExceptionが発生したときは<i>Error reading file: 例外のメッセージ</i>とコンソールに表示します。
     *
     * @return レシピデータ
     */
    public ArrayList<String> readRecipes() {
        try {
            // filepathからレシピデータを読み込みArrayList(recipeList)に1行ずつ追加する。
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            ArrayList<String> recipeList = new ArrayList<>();
            String data;
            while ((data = reader.readLine()) != null) {
                recipeList.add(data);
            }
            // readerを閉じる処理を行い、recipeListを返す
            reader.close();
            return recipeList;
        } catch (IOException e) {
            // IOExceptionが発生した際の処理
            System.out.println("Error reading file:" + e.getMessage());
        }
        return null;
    }

    /**
     * 設問2: 新規登録機能
     * 新しいレシピをrecipes.txtに追加します。<br>
     * レシピ名と材料はカンマ区切りで1行としてファイルに書き込まれます。
     *
     * @param recipeName  レシピ名
     * @param ingredients 材料名
     */
    //
    public void addRecipe(String recipeName, String ingredients) {
        try {
            // レシピ名と材料名を受け取り、材料名のカンマを整形します。
            ingredients = ingredients.replace(",", ", ");
            // レシピと材料を1行でfilepathのファイルに書き込み改行して処理を終了します。
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
            writer.write(recipeName + "," + ingredients);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error reading file:" + e.getMessage());
        }
    }
}
