package io.github.zachfalcone.bakingtime.widget.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.github.zachfalcone.bakingtime.object.Ingredient;
import io.github.zachfalcone.bakingtime.object.Recipe;

public class WidgetDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "widgets.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_INGREDIENT = "ingredient";
    private static final String INGREDIENT_WIDGET_ID = "widget_id";
    private static final String INGREDIENT_RECIPE = "recipe";
    private static final String INGREDIENT_QUANTITY = "quantity";
    private static final String INGREDIENT_MEASURE = "measure";
    private static final String INGREDIENT_NAME = "name";

    private static final String[] INGREDIENT_COLUMNS = {
            INGREDIENT_WIDGET_ID,
            INGREDIENT_RECIPE,
            INGREDIENT_QUANTITY,
            INGREDIENT_MEASURE,
            INGREDIENT_NAME
    };

    public WidgetDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
            "CREATE TABLE " + TABLE_INGREDIENT + " (" +
                INGREDIENT_WIDGET_ID + " INT, " +
                INGREDIENT_RECIPE + " TEXT, " +
                INGREDIENT_QUANTITY + " REAL, " +
                INGREDIENT_MEASURE + " TEXT, " +
                INGREDIENT_NAME + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // no upgrades yet
    }

    public void addWidgetToDatabase(int appWidgetId, Recipe recipe) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                TABLE_INGREDIENT,
                INGREDIENT_COLUMNS,
                INGREDIENT_WIDGET_ID + " = ?",
                new String[] { String.valueOf(appWidgetId) },
                null,
                null,
                null
        );
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                String recipeName = recipe.getName();
                List<Ingredient> ingredients = recipe.getIngredients();

                for (Ingredient ingredient : ingredients) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(INGREDIENT_WIDGET_ID, appWidgetId);
                    contentValues.put(INGREDIENT_RECIPE, recipeName);
                    contentValues.put(INGREDIENT_QUANTITY, ingredient.getQuantity());
                    contentValues.put(INGREDIENT_MEASURE, ingredient.getMeasure());
                    contentValues.put(INGREDIENT_NAME, ingredient.getIngredient());
                    sqLiteDatabase.insert(
                            TABLE_INGREDIENT,
                            null,
                            contentValues
                    );
                }
            }
            cursor.close();
        }
    }

    public void removeWidgetFromDatabase(int appWidgetId) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(
                TABLE_INGREDIENT,
                INGREDIENT_WIDGET_ID + " = ?",
                new String[] { String.valueOf(appWidgetId) }
        );
    }

    public List<Ingredient> getIngredients(int appWidgetId) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                TABLE_INGREDIENT,
                INGREDIENT_COLUMNS,
                INGREDIENT_WIDGET_ID + " = ?",
                new String[] { String.valueOf(appWidgetId) },
                null,
                null,
                null
        );
        List<Ingredient> ingredients = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                double quantity = cursor.getDouble(cursor.getColumnIndex(INGREDIENT_QUANTITY));
                String measure = cursor.getString(cursor.getColumnIndex(INGREDIENT_MEASURE));
                String name = cursor.getString(cursor.getColumnIndex(INGREDIENT_NAME));
                Ingredient ingredient = new Ingredient(quantity, measure, name);
                ingredients.add(ingredient);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return ingredients;
    }
}
