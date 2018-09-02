package io.github.zachfalcone.bakingtime.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.zachfalcone.bakingtime.R;
import io.github.zachfalcone.bakingtime.object.Ingredient;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private List<Ingredient> mIngredients;

    public IngredientAdapter(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
        return new IngredientAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = mIngredients.get(position);
        String ingredientMeasurement = ingredient.getMeasurement() + " ";
        holder.tv_ingredient_measure.setText(ingredientMeasurement);
        holder.tv_ingredient_text.setText(ingredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tv_ingredient_measure, tv_ingredient_text;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_ingredient_measure = itemView.findViewById(R.id.ingredient_measure);
            tv_ingredient_text = itemView.findViewById(R.id.ingredient_text);
        }
    }
}
