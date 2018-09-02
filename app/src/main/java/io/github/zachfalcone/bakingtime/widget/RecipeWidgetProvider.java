package io.github.zachfalcone.bakingtime.widget;

import android.app.PendingIntent;
import android.app.SearchManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import io.github.zachfalcone.bakingtime.R;
import io.github.zachfalcone.bakingtime.object.Ingredient;
import io.github.zachfalcone.bakingtime.object.Recipe;
import io.github.zachfalcone.bakingtime.ui.MainActivity;

public class RecipeWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_INGREDIENT = "io.github.zachfalcone.bakingtime.widget.ACTION_INGREDIENT";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; ++i) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe);
            views.setTextViewText(R.id.widget_recipe_name, "Test Recipe");
            // views.setTextViewText(R.id.widget_ingredients, "• Milk\n• Eggs\n• Butter");
            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    public static void updateNewWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Recipe recipe) {
        //Intent intent = new Intent(context, MainActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String recipeName = recipe.getName() + " - " + context.getString(R.string.ingredients);

        List<Ingredient> ingredients = recipe.getIngredients();
        ArrayList<String> ingredientsMeasurement = new ArrayList<>();
        ArrayList<String> ingredientsText = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            ingredientsMeasurement.add(ingredient.getMeasurement());
            ingredientsText.add(ingredient.getIngredient());
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe);
        views.setTextViewText(R.id.widget_recipe_name, recipeName);

        Intent ingredientListIntent = new Intent(context, IngredientsListService.class);
        ingredientListIntent.putStringArrayListExtra("ingredientsMeasurement", ingredientsMeasurement);
        ingredientListIntent.putStringArrayListExtra("ingredientsText", ingredientsText);
        views.setRemoteAdapter(R.id.widget_ingredients, ingredientListIntent);

        Intent ingredientIntent = new Intent(context, RecipeWidgetProvider.class);
        ingredientIntent.setAction(RecipeWidgetProvider.ACTION_INGREDIENT);
        ingredientIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        ingredientIntent.setData(Uri.parse(ingredientIntent.toUri(ingredientIntent.URI_INTENT_SCHEME)));
        PendingIntent ingredientPendingIntent = PendingIntent.getBroadcast(context, 0, ingredientIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_ingredients, ingredientPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_INGREDIENT)) {
            String ingredient = intent.getStringExtra("ingredient");
            Intent searchIntent = new Intent(Intent.ACTION_WEB_SEARCH);
            searchIntent.putExtra(SearchManager.QUERY, ingredient);
            context.startActivity(searchIntent);
        }
    }
}
