package io.github.zachfalcone.bakingtime.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.zachfalcone.bakingtime.R;
import io.github.zachfalcone.bakingtime.object.Step;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
    private List<Step> mSteps;

    private static RecipeAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(RecipeAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public StepAdapter(List<Step> steps) {
        mSteps = steps;
    }

    @NonNull
    @Override
    public StepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.ViewHolder holder, int position) {
        holder.tv_short_description.setText((position + 1) + ". " + mSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView tv_short_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_short_description = itemView.findViewById(R.id.step_card);
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
