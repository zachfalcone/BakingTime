package io.github.zachfalcone.bakingtime.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import io.github.zachfalcone.bakingtime.object.Ingredient;
import io.github.zachfalcone.bakingtime.widget.data.WidgetDatabase;

public class IngredientsListService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra("appWidgetId", 0);
        return new ListRemoteViewsFactory(getApplicationContext(), appWidgetId);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private List<Ingredient> mIngredients;

    public ListRemoteViewsFactory(Context context, int appWidgetId) {
        mContext = context;
        WidgetDatabase widgetDatabase = new WidgetDatabase(mContext);
        mIngredients = widgetDatabase.getIngredients(appWidgetId);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        String measurement = mIngredients.get(i).getMeasurement();
        String name = mIngredients.get(i).getIngredient();

        RemoteViews views = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
        String ingredientText = measurement + " " + name;
        views.setTextViewText(android.R.id.text1, ingredientText);
        views.setTextColor(android.R.id.text1, Color.BLACK);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("ingredient", name);
        views.setOnClickFillInIntent(android.R.id.text1, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
