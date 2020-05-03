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
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class NewPlayerActivity extends AppCompatActivity {

    private PlayerView pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player);
        pw=findViewById(R.id.simplePw);

        setPlayer();
    }

    private void setPlayer()
    {
        Uri uri=Uri.parse("http://5.160.10.54:1010");

        TrackSelector trackSelector=new DefaultTrackSelector();

        SimpleExoPlayer simpleExoPlayer= ExoPlayerFactory.newSimpleInstance(this,
                trackSelector);
        DataSource.Factory factory=new DefaultDataSourceFactory(this, Util.getUserAgent(this,"EXOPlayer"));

        MediaSource mediaSource=new ProgressiveMediaSource
                .Factory(factory).createMediaSource(uri);

        pw.setPlayer(simpleExoPlayer);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);

        simpleExoPlayer.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                System.out.println(error.getMessage());
            }
        });
    }
}
