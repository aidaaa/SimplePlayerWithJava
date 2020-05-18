package com.example.simpleplayerwithjava;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.example.simpleplayerwithjava.encrypteplayer.EncryptDecrypt;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.crypto.AesCipherDataSource;
import com.google.android.exoplayer2.util.Util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.crypto.NoSuchPaddingException;

public class NewPlayerActivity extends AppCompatActivity {

    private PlayerView pw;
    MutableLiveData<String> base_url=new MutableLiveData<>();
    File fileOrginal;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player);
        pw = findViewById(R.id.simplePw);

        fileOrginal = new File(this.getExternalFilesDir(null), "orginal1.mpg");

        HttpClientLocal clientLocal=new HttpClientLocal();
        try {
            String url=clientLocal.execute().get();
            setLivePlayer(url);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private void setPlayerByFile() {

        TrackSelector trackSelector = new DefaultTrackSelector();

        SimpleExoPlayer simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this);

        DataSource.Factory factory = new DataSource.Factory() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public DataSource createDataSource() {
                MyAesCipherDataSource aes = new MyAesCipherDataSource(new FileDataSource());
                return aes;
            }
        };

        ProgressiveMediaSource.Factory pf = new ProgressiveMediaSource.Factory(factory);
        MediaSource mediaSource = pf.createMediaSource(Uri.parse(this.getExternalFilesDir(null) + "/orginal1.mpg"));


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

    public void setLivePlayer(String url)
    {
        TrackSelector trackSelector = new DefaultTrackSelector();

        SimpleExoPlayer simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector);

        DataSource.Factory factory = new DataSource.Factory() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public DataSource createDataSource() {
                MyAesCipherDataSource aes = new MyAesCipherDataSource(new DefaultHttpDataSource(Util.getUserAgent(NewPlayerActivity.this, "exoplayer"), null));
                return aes;
            }
        };

        ProgressiveMediaSource.Factory pf = new ProgressiveMediaSource.Factory(factory);
         MediaSource mediaSource = pf.createMediaSource(Uri.parse(url));

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


    public class PipeAPI extends AsyncTask<String, String, byte[]> {

        public PipeAPI() {
            //set context variables if required
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected byte[] doInBackground(String... params) {
            int count;
            byte[] b = null;

            try {
                URL url = new URL("http://192.168.10.40:1010");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream is = new BufferedInputStream(url.openStream(), 8124);


                OutputStream os = new FileOutputStream(fileOrginal);
                byte data[] = new byte[1024];
                while ((count = is.read(data)) != -1) {
                    os.write(data, 0, count);
                    // b = data;
                }
                os.flush();
                is.close();

              /*  OutputStreamWriter out=new OutputStreamWriter(new FileOutputStream(fileToPipe));

                InputStream inputStream=new BufferedInputStream(url.openStream(),8192);
                // out.write(inputStream.read());

                byte date[]=new byte[1024];
                while ((count=inputStream.read(date))!=-1)
                {
                    out.write(count);
                }

                out.flush();
                inputStream.close();*/


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return b;
        }
    }

    public class HttpClientLocal extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... voids) {
            String base_url;

            try {
                URL url=new URL("http://192.168.10.40:1010");
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                base_url="http://192.168.10.40:1010";

            } catch (Exception e) {
                e.printStackTrace();
                base_url=null;
            }
            return base_url;
        }
    }


/*    public void a()
    {

        HttpDataSource defaultHttpDataSource= new HttpDataSource() {
            @Override
            public void addTransferListener(TransferListener transferListener) {

            }

            @Override
            public long open(DataSpec dataSpec) throws HttpDataSourceException {
                return 0;
            }

            @Override
            public void close() throws HttpDataSourceException {

            }

            @Override
            public int read(byte[] buffer, int offset, int readLength) throws HttpDataSourceException {
                return 0;
            }

            @Nullable
            @Override
            public Uri getUri() {
                return null;
            }

            @Override
            public void setRequestProperty(String name, String value) {

            }

            @Override
            public void clearRequestProperty(String name) {

            }

            @Override
            public void clearAllRequestProperties() {

            }

            @Override
            public Map<String, List<String>> getResponseHeaders() {
                return null;
            }
        };
        ProgressiveMediaSource progressiveMediaSource;
        progressiveMediaSource = new ProgressiveMediaSource(Uri.parse("http://192.168.10.85:3030"),defaultHttpDataSource,
                null,null,null,null,null);
    }*/
}



