package net.tiklab.xcode.authority;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamCopier implements Runnable {
    private InputStream inputStream;
    private OutputStream outputStream;

    public StreamCopier(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

