package com.example.simpleplayerwithjava;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.example.simpleplayerwithjava.encrypteplayer.EncryptDecrypt;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.ByteArrayDataSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.UdpDataSource;
import com.google.android.exoplayer2.upstream.crypto.AesCipherDataSource;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.google.android.exoplayer2.extractor.ts.TsExtractor.MODE_SINGLE_PMT;

public class NewPlayerActivity extends AppCompatActivity {

    private PlayerView pw;
    private SimpleExoPlayer player;
    private File fileToPipe;
    private File fileToBeDecrypted;
    File fileDecryptedOutput;
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String PASSWORD = "1133";
    private static final byte[] SALT = {3, (byte) 253, (byte) 245, (byte) 149, 86, (byte) 148, (byte) 148, 43};
    private static final byte[] IV = {(byte) 139, (byte) 214, 102, 1, (byte) 150, (byte) 134, (byte) 236, (byte) 182, 89, 110, 20, 55, (byte) 243, 120, 76, (byte) 182};
    private static final String HASH_KEY = "SHA-256";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player);
        pw = findViewById(R.id.simplePw);

        fileToPipe=new File(this.getExternalFilesDir(null),"orginal.mpg");
        File file=new File(this.getExternalFilesDir(null) , "encrypt.mpg");
        File file1=new File(this.getExternalFilesDir(null) , "decrypt.mp4");
        File file2=new File(this.getExternalFilesDir(null) , "encrypt_ctr.mpg");


        //  long l=fileToPipe.length();
        //  int a=fileToPipe.getPath().length();
        //fileToPipe.delete();

        //  System.out.println(String.valueOf(l));
        //  System.out.println(String.valueOf(a));

       /* PipeAPI pipeAPI = new PipeAPI();
            pipeAPI.execute();*/
            setPlayer();

    }


    private void setPlayer() {

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

        MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory).createMediaSource(Uri.parse(this.getExternalFilesDir(null) + "/encrypt_ctr.mpg"));


        pw.setPlayer(simpleExoPlayer);
        simpleExoPlayer.prepare(mediaSource);
        //simpleExoPlayer.setPlayWhenReady(true);

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
                URL url = new URL("http://192.168.10.85:3030");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream is = new BufferedInputStream(url.openStream(), 8124);


                OutputStream os = new FileOutputStream(fileToPipe);
                byte data[] = new byte[1024];
                while ((count = is.read(data)) != -1) {
                    os.write(data,0,count);
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
}



