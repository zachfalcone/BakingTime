package io.github.zachfalcone.bakingtime.widget;

import android.app.PendingIntent;
import android.app.SearchManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import io.github.zachfalcone.bakingtime.R;
import io.github.zachfalcone.bakingtime.object.Recipe;
import io.github.zachfalcone.bakingtime.ui.MainActivity;
import io.github.zachfalcone.bakingtime.widget.data.WidgetDatabase;

public class RecipeWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_INGREDIENT = "io.github.zachfalcone.bakingtime.widget.ACTION_INGREDIENT";
    private static final String PREFS_WIDGET = "widget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_WIDGET, 0);

        for (int i = 0; i < appWidgetIds.length; ++i) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe);
            int recipeId = sharedPreferences.getInt(appWidgetId + "_id", 0);
            String recipeName = sharedPreferences.getString(appWidgetId + "_name", null);
            views.setTextViewText(R.id.widget_recipe_name, recipeName);

            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("recipeId", recipeId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

            Intent ingredientListIntent = new Intent(context, IngredientsListService.class);
            ingredientListIntent.putExtra("appWidgetId", appWidgetId);
            views.setRemoteAdapter(R.id.widget_ingredients, ingredientListIntent);

            Intent ingredientIntent = new Intent(context, RecipeWidgetProvider.class);
            ingredientIntent.setAction(RecipeWidgetProvider.ACTION_INGREDIENT);
            ingredientIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            ingredientIntent.setData(Uri.parse(ingredientIntent.toUri(ingredientIntent.URI_INTENT_SCHEME)));
            PendingIntent ingredientPendingIntent = PendingIntent.getBroadcast(context, 0, ingredientIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_ingredients, ingredientPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    public static void addNewWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Recipe recipe) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_WIDGET, 0);

        // add widget to database
        WidgetDatabase widgetDatabase = new WidgetDatabase(context);
        widgetDatabase.addWidgetToDatabase(appWidgetId, recipe);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe);
        String recipeName = recipe.getName();
        int recipeId = recipe.getId();
        sharedPreferences.edit()
                .putString(appWidgetId + "_name", recipeName)
                .putInt(appWidgetId + "_id", recipeId)
                .apply();
        views.setTextViewText(R.id.widget_recipe_name, recipeName);
        Intent ingredientListIntent = new Intent(context, IngredientsListService.class);
        ingredientListIntent.putExtra("appWidgetId", appWidgetId);
        views.setRemoteAdapter(R.id.widget_ingredients, ingredientListIntent);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("recipeId", recipeId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

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
            searchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            searchIntent.putExtra(SearchManager.QUERY, ingredient);
            context.startActivity(searchIntent);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_WIDGET, 0);
        WidgetDatabase widgetDatabase = new WidgetDatabase(context);
        for (int appWidgetId : appWidgetIds) {
            sharedPreferences.edit()
                    .remove(appWidgetId + "_name")
                    .remove(appWidgetId + "_id")
                    .apply();
            widgetDatabase.removeWidgetFromDatabase(appWidgetId);
        }
    }
}
