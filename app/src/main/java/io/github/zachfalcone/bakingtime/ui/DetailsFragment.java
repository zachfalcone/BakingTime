package io.github.zachfalcone.bakingtime.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import io.github.zachfalcone.bakingtime.R;
import io.github.zachfalcone.bakingtime.object.Step;

public class DetailsFragment extends Fragment {

    private PlayerView playerView;
    private TextView stepDescription;
    private Step mStep;
    private ExoPlayer player;
    private boolean autoPlay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStep = getArguments().getParcelable("step");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        playerView = view.findViewById(R.id.player);
        stepDescription = view.findViewById(R.id.step_description);

        String description = mStep.getDescription().replaceAll("([0-9]*)�(F)", "$1°$2");
        stepDescription.setText(description);

        if (mStep.getVideoURL().isEmpty()) {
            playerView.setVisibility(View.GONE);
            // show thumbnail if exists
            String thumbnailURL = mStep.getThumbnailURL();
            if (!TextUtils.isEmpty(thumbnailURL)) {
                ImageView imageThumbnail = view.findViewById(R.id.image_thumbnail);
                imageThumbnail.setVisibility(View.VISIBLE);
                Picasso.with(getContext()).load(thumbnailURL).into(imageThumbnail);
            }
        } else {
            // initialize player after layout inflated
            autoPlay = getActivity().findViewById(R.id.master_detail_flow) == null;
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                    initializePlayer();
                    view.removeOnLayoutChangeListener(this);
                }
            });
        }

        return view;
    }

    private void initializePlayer() {
        if (player == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            playerView.setPlayer(player);
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultDataSourceFactory(getContext(), userAgent)
            ).createMediaSource(Uri.parse(mStep.getVideoURL()));
            player.prepare(mediaSource);
            player.setPlayWhenReady(autoPlay);
            playerView.hideController();

            // video ratio: 1920:1080 = 16:9
            ViewGroup.LayoutParams layoutParams = playerView.getLayoutParams();
            layoutParams.height = (int) (playerView.getWidth() * (9f / 16f));
            playerView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (player != null) {
            player.setPlayWhenReady(isVisibleToUser);
            if (!isVisibleToUser) {
                player.seekTo(0);
            } else {
                playerView.hideController();
            }
        }
    }
}