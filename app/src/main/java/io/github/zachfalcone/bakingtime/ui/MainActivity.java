package io.github.zachfalcone.bakingtime.ui;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

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
            recipesFragment.setArguments(getIntent().getExtras());
            fragmentTransaction.replace(R.id.fragment_main, recipesFragment);
            fragmentTransaction.commit();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        displayUp();

        /*Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());*/
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
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
        return true;
    }
}
