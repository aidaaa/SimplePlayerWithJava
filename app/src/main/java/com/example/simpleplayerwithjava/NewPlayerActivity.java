package com.example.simpleplayerwithjava;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.crypto.AesCipherDataSource;

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

import javax.crypto.NoSuchPaddingException;

public class NewPlayerActivity extends AppCompatActivity {

    private PlayerView pw;
    private SimpleExoPlayer player;
    private File fileOrginal,fileEncrypt_CTR,fileEncrypt_CBC,fileEncrypt_ECB,fileEncrypt_CBC_PKCS7Padding,fileEncrypt_CBC_NO_SALT;
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

        fileOrginal =new File(this.getExternalFilesDir(null),"orginal.mpg");
        fileEncrypt_CTR=new File(this.getExternalFilesDir(null) , "encrypt_ctr.mpg");
        fileEncrypt_CBC=new File(this.getExternalFilesDir(null) , "encrypt_cbc.mpg");
        fileEncrypt_CBC_NO_SALT=new File(this.getExternalFilesDir(null) , "encrypt_cbc_no_salt.mpg");
        fileEncrypt_ECB=new File(this.getExternalFilesDir(null) , "encrypt_ecb.mpg");
        fileEncrypt_CBC_PKCS7Padding=new File(this.getExternalFilesDir(null) , "encrypt_cbc_PKCS7Padding.mpg");
        File test=new File(this.getExternalFilesDir(null) , "test.mpg");
        File test1=new File(this.getExternalFilesDir(null) , "test1.mpg");


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
                //MyAesCipherDataSource aes = new MyAesCipherDataSource(new DefaultHttpDataSource("",null));
                MyAesCipherDataSource aes = new MyAesCipherDataSource(new FileDataSource());
                return aes;
            }
        };
        ProgressiveMediaSource.Factory pf=new ProgressiveMediaSource.Factory(factory);
        MediaSource mediaSource =pf.createMediaSource(Uri.parse(this.getExternalFilesDir(null) + "/encrypt_cbc.mpg"));

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
                URL url = new URL("http://192.168.10.85:3030");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream is = new BufferedInputStream(url.openStream(), 8124);


                OutputStream os = new FileOutputStream(fileOrginal);
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



