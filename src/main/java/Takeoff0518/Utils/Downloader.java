package Takeoff0518.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Downloader {
    /**
     * @param redirectUrl Final music URL
     * @param fileName    File name
     * @throws IOException IOException awa
     */
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

    /**
     * @param lrcData  Lyrics on json
     * @param fileName File name
     * @throws IOException IOException awa
     */
    public static void downloadLRC(String lrcData, String fileName) throws IOException {
        FileUtils.writeStringToFile(new File(fileName), lrcData, StandardCharsets.UTF_8);
    }
}
