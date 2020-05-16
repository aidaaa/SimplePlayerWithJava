package com.example.simpleplayerwithjava.encrypteplayer;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDecrypt
{
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String PASSWORD = "1133";
    private static final byte[] SALT = {3, (byte) 253, (byte) 245, (byte) 149, 86, (byte) 148, (byte) 148, 43};
    private static final byte[] IV = {(byte) 139, (byte) 214, 102, 1, (byte) 150, (byte) 134, (byte) 236, (byte) 182, 89, 110, 20, 55, (byte) 243, 120, 76, (byte) 182};
    private static final String HASH_KEY = "SHA-256";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void encryptfile(File path,File out) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, InvalidKeySpecException
    {
        FileInputStream fis = new FileInputStream(path);
        FileOutputStream fos = new FileOutputStream(out);

        MessageDigest sha = MessageDigest.getInstance(HASH_KEY);

        sha.update(SALT);

        sha.update(PASSWORD.getBytes(StandardCharsets.US_ASCII));

        byte[] key=sha.digest();

        SecretKeySpec sks = new SecretKeySpec(key, AES_ALGORITHM);

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE,sks,ivParameterSpec);

        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        int b;
        byte[] d = new byte[8];
        while((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        cos.flush();
        cos.close();
        fis.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void decrypt(File path , File outPath) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        FileInputStream fis = new FileInputStream(path);
        FileOutputStream fos = new FileOutputStream(outPath);

        MessageDigest sha = MessageDigest.getInstance(HASH_KEY);

        sha.update(SALT);

        sha.update(PASSWORD.getBytes(StandardCharsets.US_ASCII));


        byte[] key=sha.digest();

        SecretKeySpec sks = new SecretKeySpec(key, AES_ALGORITHM);

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE,sks,ivParameterSpec);

        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];
        while((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();

    }

    public static void main(String[] args) {
    }
}
