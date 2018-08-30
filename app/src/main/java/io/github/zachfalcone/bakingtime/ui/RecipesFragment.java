package io.github.zachfalcone.bakingtime.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.zachfalcone.bakingtime.R;
import io.github.zachfalcone.bakingtime.adapter.RecipeAdapter;
import io.github.zachfalcone.bakingtime.object.Ingredient;
import io.github.zachfalcone.bakingtime.object.Recipe;
import io.github.zachfalcone.bakingtime.object.Step;

public class RecipesFragment extends Fragment {
    @BindView(R.id.recycle_recipes)
    RecyclerView recycleRecipes;

    @BindView(R.id.progress_bar_recipes)
    ProgressBar progressRecipes;

    @BindView(R.id.text_no_connection)
    TextView textNoConnection;

    private RecipeAdapter recipeAdapter;
    private ArrayList<Recipe> recipes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeAdapter = new RecipeAdapter();

        /*while (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipes, container, false);

        ButterKnife.bind(this, view);

        getActivity().setTitle(getString(R.string.recipes));

        recycleRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleRecipes.setAdapter(recipeAdapter);

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (recipes != null) {
            populateRecipes(recipes);
            return view;
        //} else if (savedInstanceState != null) {
            //recipes = savedInstanceState.getParcelableArrayList("recipes");
            //populateRecipes(recipes);
        } else if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            new LoadRecipes().execute();
        } else {
            Log.e(RecipesFragment.class.getSimpleName(), "not connected");

            progressRecipes.setVisibility(View.GONE);
            textNoConnection.setVisibility(View.VISIBLE);
        }

        recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                StepsFragment stepsFragment = new StepsFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("recipe", recipeAdapter.getRecipe(position));
                stepsFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_main, stepsFragment);
                fragmentTransaction.addToBackStack("recipeStack");
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void populateRecipes(ArrayList<Recipe> recipes) {
        recipeAdapter.updateRecipes(recipes);
        progressRecipes.setVisibility(View.GONE);
        recycleRecipes.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList("recipes", recipeAdapter.getRecipes());
    }

    private class LoadRecipes extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(getString(R.string.recipes_url));
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String buffer;

                while ((buffer = bufferedReader.readLine()) != null) {
                    stringBuilder.append(buffer).append('\n');
                }

                return stringBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Log.e(RecipesFragment.class.getSimpleName(), "no json");
                return;
            }
            try {
                recipes = new ArrayList<>();
                JSONArray jsonRecipes = new JSONArray(s);
                for (int i = 0; i < jsonRecipes.length(); ++i) {
                    JSONObject jsonRecipe = jsonRecipes.getJSONObject(i);
                    int id = jsonRecipe.getInt("id");
                    String name = jsonRecipe.getString("name");

                    // ingredients
                    List<Ingredient> ingredients = new ArrayList<>();
                    JSONArray jsonIngredients = jsonRecipe.getJSONArray("ingredients");
                    for (int j = 0; j < jsonIngredients.length(); ++j) {
                        JSONObject jsonIngredient = jsonIngredients.getJSONObject(j);
                        double quantity = jsonIngredient.getDouble("quantity");
                        String measure = jsonIngredient.getString("measure");
                        String ingredientItem = jsonIngredient.getString("ingredient");
                        Ingredient ingredient = new Ingredient(quantity, measure, ingredientItem);
                        ingredients.add(ingredient);
                    }

                    // steps
                    List<Step> steps = new ArrayList<>();
                    JSONArray jsonSteps = jsonRecipe.getJSONArray("steps");
                    for (int j = 0; j < jsonSteps.length(); ++j) {
                        JSONObject jsonStep = jsonSteps.getJSONObject(j);
                        int stepId = jsonStep.getInt("id");
                        String shortDescription = jsonStep.getString("shortDescription");
                        String description = jsonStep.getString("description");
                        String videoURL = jsonStep.getString("videoURL");
                        String thumbnailURL = jsonStep.getString("thumbnailURL");
                        Step step = new Step(stepId,shortDescription, description, videoURL, thumbnailURL);
                        steps.add(step);
                    }

                    int servings = jsonRecipe.getInt("servings");
                    String image = jsonRecipe.getString("image");

                    Recipe recipe = new Recipe(id, name, ingredients, steps, servings, image);
                    recipes.add(recipe);
                }

                populateRecipes(recipes);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
