package io.github.zachfalcone.bakingtime.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.zachfalcone.bakingtime.R;
import io.github.zachfalcone.bakingtime.object.Step;

public class DetailsFragment extends Fragment {
    @BindView(R.id.player)
    PlayerView player;

    @BindView(R.id.step_description)
    TextView stepDescription;

    private Step mStep;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStep = getArguments().getParcelable("step");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        ButterKnife.bind(this, view);

        stepDescription.setText(mStep.getDescription());

        return view;
    }
}