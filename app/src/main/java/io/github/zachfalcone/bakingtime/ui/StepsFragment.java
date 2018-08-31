package io.github.zachfalcone.bakingtime.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.zachfalcone.bakingtime.R;
import io.github.zachfalcone.bakingtime.adapter.IngredientAdapter;
import io.github.zachfalcone.bakingtime.adapter.RecipeAdapter;
import io.github.zachfalcone.bakingtime.adapter.StepAdapter;
import io.github.zachfalcone.bakingtime.object.Recipe;

public class StepsFragment extends Fragment {

    private RecyclerView recycleSteps;
    private RecyclerView recycleIngredients;
    private Recipe mRecipe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipe = getArguments().getParcelable("recipe");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        getActivity().setTitle(mRecipe.getName());

        recycleSteps = view.findViewById(R.id.recycle_steps);
        recycleIngredients = view.findViewById(R.id.recycle_ingredients);

        IngredientAdapter ingredientAdapter = new IngredientAdapter(mRecipe.getIngredients());
        recycleIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleIngredients.setAdapter(ingredientAdapter);
        recycleIngredients.setHasFixedSize(true);
        recycleIngredients.setNestedScrollingEnabled(false);

        final StepAdapter stepAdapter = new StepAdapter(mRecipe.getSteps());
        recycleSteps.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleSteps.setAdapter(stepAdapter);
        recycleSteps.setHasFixedSize(true);
        recycleSteps.setNestedScrollingEnabled(false);

        if (view.findViewById(R.id.master_detail_flow) == null) {
            stepAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    DetailsPagerFragment detailsPagerFragment = new DetailsPagerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("recipe", mRecipe);
                    bundle.putInt("position", position);
                    detailsPagerFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragment_main, detailsPagerFragment);
                    fragmentTransaction.addToBackStack("stepStack");
                    fragmentTransaction.commit();
                }
            });
        } else {
            DetailsFragment detailsFragment = new DetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("step", mRecipe.getStep(0));
            detailsFragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_details, detailsFragment);
            fragmentTransaction.commit();

            stepAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    DetailsFragment detailsFragment = new DetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("step", mRecipe.getStep(position));
                    detailsFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_details, detailsFragment);
                    fragmentTransaction.commit();
                }
            });
        }

        return view;
    }
}
