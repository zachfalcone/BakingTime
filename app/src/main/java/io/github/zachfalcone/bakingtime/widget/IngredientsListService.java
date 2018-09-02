package io.github.zachfalcone.bakingtime.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

public class IngredientsListService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        ArrayList<String> ingredientsMeasurement = intent.getStringArrayListExtra("ingredientsMeasurement");
        ArrayList<String> ingredientsText = intent.getStringArrayListExtra("ingredientsText");
        return new ListRemoteViewsFactory(getApplicationContext(), ingredientsMeasurement, ingredientsText);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private ArrayList<String> mIngredientsMeasure, mIngredientsText;

    public ListRemoteViewsFactory(Context context, ArrayList<String> ingredientsMeasurement, ArrayList<String> ingredientsText) {
        mContext = context;
        mIngredientsMeasure = ingredientsMeasurement;
        mIngredientsText = ingredientsText;
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
        return mIngredientsText.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
        String ingredientText = mIngredientsMeasure.get(i) + " " + mIngredientsText.get(i);
        views.setTextViewText(android.R.id.text1, ingredientText);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("ingredient", mIngredientsText.get(i));
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
