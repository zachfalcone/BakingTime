package io.github.zachfalcone.bakingtime.ui;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.github.zachfalcone.bakingtime.R;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            RecipesFragment recipesFragment = new RecipesFragment();
            fragmentTransaction.replace(R.id.fragment_main, recipesFragment);
            fragmentTransaction.commit();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        displayUp();
    }

    @Override
    public void onBackStackChanged() {
        displayUp();
    }

    public void displayUp() {
        boolean showBack = getSupportFragmentManager().getBackStackEntryCount() > 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }
}
