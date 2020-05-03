package com.example.simpleplayerwithjava;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class OldPlayerActivity extends AppCompatActivity {

    SimpleExoPlayerView simpleExoPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_player);
        simpleExoPlayerView=findViewById(R.id.simplePw);

       // setPlayer();
    }

   /* private void setPlayer()
    {
        Uri uri=Uri.parse("http://5.160.10.54:1010");
        LoadControl loadControl=new DefaultLoadControl();
        TrackSelector trackSelector=new DefaultTrackSelector();

        SimpleExoPlayer simpleExoPlayer= ExoPlayerFactory.newSimpleInstance(this,
                trackSelector,loadControl);
        DataSource.Factory factory=new DefaultDataSourceFactory(this, Util.getUserAgent(this,"EXOPlayer"));
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(uri, factory, extractorsFactory, null, null);

        simpleExoPlayerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);

        simpleExoPlayer.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                System.out.println(error.getMessage());
            }

            @Override
            public void onPositionDiscontinuity() {

            }
        });
    }*/

}
