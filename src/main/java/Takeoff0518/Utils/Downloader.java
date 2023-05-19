package Takeoff0518.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Downloader {

    public static void downloadMP3(String redirectUrl, String fileName) throws IOException {
        InputStream in = new URL(redirectUrl).openStream();
        FileOutputStream fos = new FileOutputStream(new File(fileName));
        int length = -1;
        byte[] buffer = new byte[1024];
        while ((length = in.read(buffer)) != -1) {
            fos.write(buffer, 0, length);
//            System.out.println("Write "+length);
        }

        fos.close();
        in.close();
    }
}
