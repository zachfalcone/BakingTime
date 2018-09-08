package io.github.zachfalcone.bakingtime.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

import io.github.zachfalcone.bakingtime.R;
import io.github.zachfalcone.bakingtime.adapter.IngredientAdapter;
import io.github.zachfalcone.bakingtime.adapter.RecipeAdapter;
import io.github.zachfalcone.bakingtime.adapter.StepAdapter;
import io.github.zachfalcone.bakingtime.object.Recipe;

public class StepsFragment extends Fragment {

    private Recipe mRecipe;
    private int selectedStep;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable("recipe");
        }
        if (savedInstanceState != null) {
            selectedStep = savedInstanceState.getInt("selectedStep");
        } else {
            selectedStep = 0;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        getActivity().setTitle(mRecipe.getName());

        RecyclerView recycleSteps = view.findViewById(R.id.recycle_steps);
        RecyclerView recycleIngredients = view.findViewById(R.id.recycle_ingredients);

        TextView textServings = view.findViewById(R.id.text_servings);
        textServings.setText(String.format(getString(R.string.makes_n_servings), mRecipe.getServings()));

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
                    selectedStep = position;
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
            bundle.putParcelable("step", mRecipe.getStep(selectedStep));
            if (savedInstanceState != null) {
                long currentPosition = savedInstanceState.getLong("currentPosition");
                boolean playWhenReady = savedInstanceState.getBoolean("playWhenReady");
                Bundle playerState = new Bundle();
                playerState.putLong("currentPosition", currentPosition);
                playerState.putBoolean("playWhenReady", playWhenReady);
                bundle.putBundle("playerState", playerState);
            }
            detailsFragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_details, detailsFragment);
            fragmentTransaction.commit();

            stepAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    selectedStep = position;
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedStep", selectedStep);
        PlayerView playerView = getActivity().findViewById(R.id.player);
        if (playerView != null) {
            Player player = playerView.getPlayer();
            if (player != null) {
                outState.putLong("currentPosition", player.getCurrentPosition());
                outState.putBoolean("playWhenReady", player.getPlayWhenReady());
            }
        }
    }
}
