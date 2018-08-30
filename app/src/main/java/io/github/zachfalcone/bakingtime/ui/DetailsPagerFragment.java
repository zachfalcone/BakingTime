package io.github.zachfalcone.bakingtime.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.zachfalcone.bakingtime.R;
import io.github.zachfalcone.bakingtime.object.Recipe;

public class DetailsPagerFragment extends Fragment {
    @BindView(R.id.details_pager)
    ViewPager detailsPager;

    private Recipe mRecipe;

    private PagerAdapter mPagerAdapter;

    private int mPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipe = getArguments().getParcelable("recipe");
        mPosition = getArguments().getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details_pager, container, false);

        ButterKnife.bind(this, view);

        mPagerAdapter = new DetailsPagerAdapter(getActivity().getSupportFragmentManager());
        detailsPager.setAdapter(mPagerAdapter);
        detailsPager.setCurrentItem(mPosition);

        return view;
    }

    private class DetailsPagerAdapter extends FragmentStatePagerAdapter {
        public DetailsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            DetailsFragment detailsFragment = new DetailsFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable("step", mRecipe.getStep(position));
            detailsFragment.setArguments(bundle);

            return detailsFragment;
        }

        @Override
        public int getCount() {
            return mRecipe.getNumberOfSteps();
        }

        // TODO setPrimaryItem position
    }
}
