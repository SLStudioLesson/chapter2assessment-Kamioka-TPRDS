package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

import data.RecipeFileHandler;

public class RecipeUI {
    private BufferedReader reader;
    private RecipeFileHandler fileHandler;

    public RecipeUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        fileHandler = new RecipeFileHandler();
    }

    public RecipeUI(BufferedReader reader, RecipeFileHandler fileHandler) {
        this.reader = reader;
        this.fileHandler = fileHandler;
    }

    public void displayMenu() {
        while (true) {
            try {
                System.out.println();
                System.out.println("Main Menu:");
                System.out.println("1: Display Recipes");
                System.out.println("2: Add New Recipe");
                System.out.println("3: Search Recipe");
                System.out.println("4: Exit Application");
                System.out.print("Please choose an option: ");

                String choice = reader.readLine();

                switch (choice) {
                    case "1":
                        // 設問1: 一覧表示機能
                        displayRecipes();
                        break;
                    case "2":
                        // 設問2: 新規登録機能
                        addNewRecipe();
                        break;
                    case "3":
                        // 設問3: 検索機能
                        searchRecipe();
                        break;
                    case "4":
                        System.out.println("Exit the application.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please select again.");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Error reading input from user: " + e.getMessage());
            }
        }
    }

    /**
     * 設問1: 一覧表示機能
     * RecipeFileHandlerから読み込んだレシピデータを整形してコンソールに表示します。
     */
    private void displayRecipes() {
        // レシピデータを受け取るArrayList(recipes)とレシピデータを読み取るメソッドを実行するfileHandlerインスタンスを生成する。
        ArrayList<String> recipes = new ArrayList<>();
        fileHandler = new RecipeFileHandler();
        recipes = fileHandler.readRecipes();

        // レシピデータが空の場合メッセージを出力しmainメソッドに戻る
        if (recipes == null || recipes.size() == 0) {
            System.out.println("\nNo recipes available.");
            return;
        }

        System.out.println("\nRecipes:");
        // 受け取ったレシピデータの1単語目を"Recipe Name"、2単語目以降を"Main Ingredients:"として整形して表示する。
        for (int i = 0; i < recipes.size(); i++) {
            String[] data = recipes.get(i).split(",");
            for (int j = 0; j < data.length; j++) {
                if (j == 0) {
                    System.out.print(
                            "-----------------------------------\r\n" +
                                    "Recipe Name: " + data[j] +
                                    "\nMain Ingredients: ");
                } else {
                    if (j == data.length - 1) {
                        System.out.print(data[j]);
                    } else {
                        System.out.print(data[j] + ",");
                    }
                }
            }
            System.out.println();
        }
    }

    /**
     * 設問2: 新規登録機能
     * ユーザーからレシピ名と主な材料を入力させ、RecipeFileHandlerを使用してrecipes.txtに新しいレシピを追加します。
     *
     * @throws java.io.IOException 入出力が受け付けられない
     */
    private void addNewRecipe() throws IOException {
        // BufferedReaderインスタンスとaddRecipeに渡すための変数を作成
        reader = new BufferedReader(new InputStreamReader(System.in));
        String inputRecipeName = "";
        String inputIngredients = "";

        // 文字列を出力し、レシピ名と材料名の入力を受け取る
        try {
            System.out.print("Enter recipe name: ");
            inputRecipeName = reader.readLine();
            System.out.print("Enter main ingredients (comma separated): ");
            inputIngredients = reader.readLine();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        // RecipeFileHandlerインスタンスを作成し、addRecipeメソッドにレシピ名と材料名を引数にして渡す
        fileHandler = new RecipeFileHandler();
        fileHandler.addRecipe(inputRecipeName, inputIngredients);

        // 処理が終了した事を出力する
        System.out.println("Recipe added successfully.");
    }

    /**
     * 設問3: 検索機能
     * ユーザーから検索クエリを入力させ、そのクエリに基づいてレシピを検索し、一致するレシピをコンソールに表示します。
     *
     * @throws java.io.IOException 入出力が受け付けられない
     */
    private void searchRecipe() throws IOException {
        // BufferReaderインスタンスと入力値を格納する変数を作成
        reader = new BufferedReader(new InputStreamReader(System.in));
        String input = "";

        // try文の中で文字列を出力しクエリを受け取る
        try {
            System.out.print("Enter search query (e.g., 'name=Tomato&ingredient=Garlic'): ");
            input = reader.readLine();
        } catch (IOException e) {
            System.out.println("\"Error reading input from user: \" + e.getMessage()");
        }

        // 受け取ったクエリ検索できるようにレシピ名と材料名を抜き出す
        String[] inputData = input.split("&");
        String[] searchData = new String[inputData.length];
        for (int i = 0; i < inputData.length; i++) {
            String[] temp = inputData[i].split("=");
            searchData[i] = temp[1];
        }

        // レシピデータをArrayListに格納
        ArrayList<String> recipeData = new ArrayList<>();
        fileHandler = new RecipeFileHandler();
        recipeData = fileHandler.readRecipes();

        // ArrayListの中から文字列が探索できたか判定するフラグ用の配列を作成
        boolean[] recipeFlag = new boolean[recipeData.size()];
        boolean[] ingredientsFlag = new boolean[recipeData.size()];

        // 1回目のループで、レシピ名を探索
        // 2回目以降のループでレシピ名を探索できていた文字列の中から材料名を探索
        for (int i = 0; i < searchData.length; i++) {
            for (int j = 0; j < recipeData.size(); j++) {
                if (i == 0) {
                    String recipeName = recipeData.get(j);
                    recipeName = recipeName.substring(0, recipeName.indexOf(","));
                    if (recipeName.indexOf(searchData[i]) >= 0) {
                        recipeFlag[j] = true;
                    }
                }
                if (recipeFlag[j]) {
                    String ingredientName = recipeData.get(j);
                    ingredientName = ingredientName.substring(ingredientName.indexOf(",") + 1);
                    if (ingredientName.indexOf(searchData[i]) >= 0) {
                        ingredientsFlag[j] = true;
                    }
                }
            }
        }

        // レシピ名と材料名の両方が探索できた文字列を出力
        boolean notFoundFlag = true;
        System.out.println("Search Results:");
        for (int i = 0; i < recipeData.size(); i++) {
            if (recipeFlag[i] && ingredientsFlag[i]) {
                System.out.println(recipeData.get(i));
                notFoundFlag = false;
            }
        }

        // レシピ名と材料名を1つも探索できなかった場合の出力
        if (notFoundFlag) {
            System.out.println("No recipes found matching the criteria.");
        }
    }
}
