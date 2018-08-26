package io.github.zachfalcone.bakingtime.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.zachfalcone.bakingtime.R;
import io.github.zachfalcone.bakingtime.object.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private ArrayList<Recipe> mRecipes;

    private static OnItemClickListener mOnItemClickListener;

    public RecipeAdapter() {
        mRecipes = new ArrayList<>();
    }

    public void updateRecipes(List<Recipe> recipes) {
        if (recipes != null && recipes.size() > 0) {
            mRecipes.clear();
            mRecipes.addAll(recipes);
            notifyDataSetChanged();
        }
    }

    public ArrayList<Recipe> getRecipes() {
        return mRecipes;
    }

    public Recipe getRecipe(int position) {
        return mRecipes.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_title.setText(mRecipes.get(position).getName());
        holder.tv_description.setText(String.valueOf(mRecipes.get(position).getNumberOfSteps()));
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView tv_title, tv_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.recipe_card_title);
            tv_description = itemView.findViewById(R.id.recipe_card_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

}
