package com.example.simpleplayerwithjava;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.upstream.DataSource;

import java.io.IOException;
import java.io.InputStream;

class MyInputStream extends InputStream {
    private DataSource upstream;

    public MyInputStream(DataSource upstream) {

        this.upstream = upstream;
    }

    @Override
    public int read(byte[] b) throws IOException {
      return read(b,0,b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        int read = upstream.read(b, off, len);
        if (read == C.RESULT_END_OF_INPUT) {
            return C.RESULT_END_OF_INPUT;
        }
        return read;
    }

    @Override
    public int read() throws IOException {
       return -1;
    }

    @Override
    public void close() throws IOException {
       upstream.close();
    }

    public long skip(long n) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public int available() throws IOException {
        throw new RuntimeException("Stub!");
    }


    public synchronized void mark(int readlimit)
    {
        throw new RuntimeException("Stub!");
    }

    public synchronized void reset() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public boolean markSupported()
    {
        throw new RuntimeException("Stub!");
    }
}
