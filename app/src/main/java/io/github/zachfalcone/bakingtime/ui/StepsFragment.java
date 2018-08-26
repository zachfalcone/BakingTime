package io.github.zachfalcone.bakingtime.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.github.zachfalcone.bakingtime.R;
import io.github.zachfalcone.bakingtime.object.Recipe;

public class StepsFragment extends Fragment {
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

        ButterKnife.bind(this, view);

        getActivity().setTitle(mRecipe.getName());

        return view;
    }
}
