package com.example.simpleplayerwithjava;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MyAesCipherDataSource implements DataSource {

    private final DataSource upstream;
    FileOutputStream output;
    private @Nullable
    Cipher cipher;
    private  CipherInputStream inputStream;
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String PASSWORD = "1133";
    private static final byte[] SALT = {3, (byte) 253, (byte) 245, (byte) 149, 86, (byte) 148, (byte) 148, 43};
    private static final byte[] IV = {(byte) 139, (byte) 214, 102, 1, (byte) 150, (byte) 134, (byte) 236, (byte) 182, 89, 110, 20, 55, (byte) 243, 120, 76, (byte) 182};
    private static final String HASH_KEY = "SHA-256";
    private int index=0;

    public MyAesCipherDataSource(DataSource upstream) {
        this.upstream = upstream;
    }

    @Override
    public void addTransferListener(TransferListener transferListener) {
        upstream.addTransferListener(transferListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public long open(DataSpec dataSpec) throws IOException {
        output=new FileOutputStream(dataSpec.uri.toString()+".ts");
        long dataLength = upstream.open(dataSpec);
        try {
            MessageDigest sha = MessageDigest.getInstance(HASH_KEY);
            sha.update(SALT);
            sha.update(PASSWORD.getBytes(StandardCharsets.US_ASCII));
            byte[] key=sha.digest();
            SecretKeySpec sks = new SecretKeySpec(key, AES_ALGORITHM);
            //cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    sks,
                    ivParameterSpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        inputStream=new CipherInputStream(new MyInputStream(upstream),cipher);
        return -1;
    }

    @Override
    public int read(byte[] data, int offset, int readLength) throws IOException {
        if (readLength == 0) {
            return 0;
        }
        int read = inputStream.read(data, offset, readLength);
        if (read == C.RESULT_END_OF_INPUT) {
            return C.RESULT_END_OF_INPUT;
        }
        output.write(data,offset,read);
        return read;
    }

    @Override
    public @Nullable
    Uri getUri() {
        return upstream.getUri();
    }

    @Override
    public Map<String, List<String>> getResponseHeaders() {
        return upstream.getResponseHeaders();
    }

    @Override
    public void close() throws IOException {
        cipher = null;
        inputStream.close();
        output.close();
    }


}
