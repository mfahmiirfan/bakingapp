package com.example.mfahmii.bakingapp;

import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mfahmii.bakingapp.model.RecipeCard;
import com.example.mfahmii.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;

import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mfahmii on 8/10/2017.
 */

public class ProcedureDetailFragment extends Fragment implements ExoPlayer.EventListener {
    @BindView(R.id.decription) TextView desc;
    @BindView(R.id.video_url) TextView vidURL;
    @BindView(R.id.thumbnail_url) TextView tmbURL;
    @BindView(R.id.ingredients_tv) TextView ingredients;
    @BindView(R.id.ingredients_frame) FrameLayout igdFrame;
    @BindView(R.id.steps_frame) FrameLayout stpFrame;
    @BindView(R.id.page_idn) TextView pageIdn;
    @BindView(R.id.btn_pref) Button btnPref;
    @BindView(R.id.btn_next) Button btnNext;


    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;

    private Unbinder unbinder;

    RecipeCard recipeCard;

    int stepPosition=-1;

    private SimpleExoPlayer mExoPlayer;

    Boolean stpFrameVisibility;
    Boolean igdFrameVisibility;

    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_procedure_detail, container, false);



        Log.d("tug",""+stepPosition);


        ButterKnife.bind(this, rootView);

        recipeCard=getActivity().getIntent().getParcelableExtra("test");




        btnPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tug","button pref");
                if(stepPosition-1>0)
                    UpdateView(stepPosition-1);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tug","button next");
                if(stepPosition+1<recipeCard.getSteps().size())
                    UpdateView(stepPosition+1);
            }
        });

        if(savedInstanceState!=null){
            stepPosition=savedInstanceState.getInt("posisi");
        }

        if(stepPosition == -1){
            stpFrame.setVisibility(View.GONE);
            igdFrame.setVisibility(View.VISIBLE);
            stpFrameVisibility=false;
            igdFrameVisibility=true;

            ingredients.setText("");
            for(int i=0;i<recipeCard.getIngredients().size();i++){
                if(getActivity().getResources().getBoolean(R.bool.isTablet)){
                    ingredients.append(i+1+". "+recipeCard.getIngredients().get(i).getIngredient()+" ("+recipeCard.getIngredients().get(i).getQuantity()+" "+recipeCard.getIngredients().get(i).getMeasure()+")\n\n");
                }else{
                    ingredients.append(i+1+". "+recipeCard.getIngredients().get(i).getIngredient()+" ("+recipeCard.getIngredients().get(i).getQuantity()+" "+recipeCard.getIngredients().get(i).getMeasure()+")\n");
                }}
        }else {
            stpFrame.setVisibility(View.VISIBLE);
            igdFrame.setVisibility(View.GONE);
            stpFrameVisibility=true;
            igdFrameVisibility=false;



            desc.setText(recipeCard.getSteps().get(stepPosition).getDescription());
            vidURL.setText(recipeCard.getSteps().get(stepPosition).getVideoURL());
            tmbURL.setText(recipeCard.getSteps().get(stepPosition).getThumbnailURL());
            pageIdn.setText("Step "+stepPosition);

            // Initialize the Media Session.
//            initializeMediaSession();

            initializePlayer(Uri.parse(recipeCard.getSteps().get(stepPosition).getVideoURL()));

        }

        return rootView;
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {

            // 1. Create a default TrackSelector
            Handler mainHandler = new Handler();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            // 2. Create the player
            mExoPlayer =
                    ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
            mPlayerView.setPlayer(mExoPlayer);

            // Measures bandwidth during playback. Can be null if not required.
            DefaultBandwidthMeter bandMeter = new DefaultBandwidthMeter();
// Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getActivity(), "BakingApp"), bandMeter);
// Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
// This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource(mediaUri,
                    dataSourceFactory, extractorsFactory, null, null);
// Prepare the player with the source.
            mExoPlayer.prepare(videoSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
//        mNotificationManager.cancelAll();
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getActivity(),"ProcedureDetailFragment");

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    public void UpdateView(int stepPosition){
        this.stepPosition=stepPosition;
        if(stepPosition == -1){
            stpFrame.setVisibility(View.GONE);
            igdFrame.setVisibility(View.VISIBLE);
            stpFrameVisibility=false;
            igdFrameVisibility=true;

            ingredients.setText("");
            for(int i=0;i<recipeCard.getIngredients().size();i++){
                if(getActivity().getResources().getBoolean(R.bool.isTablet)){
                    ingredients.append(i+1+". "+recipeCard.getIngredients().get(i).getIngredient()+" ("+recipeCard.getIngredients().get(i).getQuantity()+" "+recipeCard.getIngredients().get(i).getMeasure()+")\n\n");
                }else{
                    ingredients.append(i+1+". "+recipeCard.getIngredients().get(i).getIngredient()+" ("+recipeCard.getIngredients().get(i).getQuantity()+" "+recipeCard.getIngredients().get(i).getMeasure()+")\n");
                }
            }
        }else {
            if(mExoPlayer!=null)
                releasePlayer();
            stpFrame.setVisibility(View.VISIBLE);
            igdFrame.setVisibility(View.GONE);
            stpFrameVisibility=true;
            igdFrameVisibility=false;


            desc.setText(recipeCard.getSteps().get(stepPosition).getDescription());
            vidURL.setText(recipeCard.getSteps().get(stepPosition).getVideoURL());
            tmbURL.setText(recipeCard.getSteps().get(stepPosition).getThumbnailURL());
            pageIdn.setText("Step "+stepPosition);

            // Initialize the Media Session.
//            initializeMediaSession();


            initializePlayer(Uri.parse(recipeCard.getSteps().get(stepPosition).getVideoURL()));
        }
    }

    public void SetStepPosition(int stepPosition){
        this.stepPosition=stepPosition;
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {}
    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}
    @Override
    public void onLoadingChanged(boolean isLoading) {}
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {}
    @Override
    public void onRepeatModeChanged(int repeatMode) {}
    @Override
    public void onPlayerError(ExoPlaybackException error) {}
    @Override
    public void onPositionDiscontinuity() {}
    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}


    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mExoPlayer!=null)
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("posisi",stepPosition);
        super.onSaveInstanceState(outState);
    }




}
