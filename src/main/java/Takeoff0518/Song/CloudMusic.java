package Takeoff0518.Song;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CloudMusic {
    static int musicStatueCode;
    static int lyricStatueCode;
    static int lyricSongStatus;
    static int lyricVersion;

    static int musicID;
    static String musicURL;
    static String lyricLRC;
    static String lyricJSON;

    public CloudMusic(int id) {
        musicID = id;
    }

    public String getLyricLRC() {
        return lyricLRC;
    }

    public String getLyricJSON() {
        return lyricJSON;
    }

    public String getMusicURL() {
        return musicURL;
    }

    public int getMusicStatueCode() {
        return musicStatueCode;
    }

    public int getLyricStatueCode() {
        return lyricStatueCode;
    }

    public int getLyricSongStatus() {
        return lyricSongStatus;
    }

    public int getLyricVersion() {
        return lyricVersion;
    }

    public void downloadMusic(String fileName) throws IOException {
        InputStream in = new URL(musicURL).openConnection().getInputStream();
        FileOutputStream fos = new FileOutputStream(new File(fileName));
        int length = -1;
        byte[] buffer = new byte[1 << 10];
        while ((length = in.read(buffer)) != -1) {
            fos.write(buffer, 0, length);
        }
        fos.close();
        in.close();
    }

    public void downloadLRC(String fileName) throws IOException {
        FileUtils.writeStringToFile(new File(fileName), lyricLRC, StandardCharsets.UTF_8);
    }

    public void reqMusicURL(String userAgent) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://music.163.com/song/media/outer/url?id=" + musicID).openConnection();
        connection.setRequestProperty("User-Agent", userAgent);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.connect();
        musicStatueCode = connection.getResponseCode();
        if (musicStatueCode == HttpURLConnection.HTTP_MOVED_TEMP || musicStatueCode == HttpURLConnection.HTTP_MOVED_PERM) {
            musicURL = connection.getHeaderField("Location");
        }
        connection.disconnect();
    }

    public void reqLyric(String userAgent) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://music.163.com/api/song/media?id=" + musicID).openConnection();
        connection.setRequestProperty("User-Agent", userAgent);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        connection.connect();
//        lyricStatueCode=connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();
        lyricJSON = content.toString();
        byte[] bytes = lyricJSON.getBytes();
        JSONObject data = JSON.parseObject(bytes);
        lyricStatueCode = data.getInteger("code");
        if (lyricStatueCode == 200) {
            lyricVersion = data.getInteger("lyricVersion");
            lyricSongStatus = data.getInteger("songStatus");
            lyricLRC = data.getString("lyric");
        }
    }
}
