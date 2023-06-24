package Takeoff0518.Song;

import Takeoff0518.LiteLogger.Logger;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class CloudMusic {
    static int musicResponseCode;
    static int lyricStatueCode;
    static int lyricSongStatus;
    static int lyricVersion;
    static int musicID;
    static String musicURL;
    static String lyricLRC;
    static String lyricJSON;
    static String musicTitle;
    static boolean isAvailable = false;
    //    static boolean available = false;
    CloudMusic me = this;

    public CloudMusic(int id) {
        musicID = id;
    }

    /**
     * @return Lyric in LRC form. <p>
     * Like [00:16.92] .....
     */
    public String getLyricLRC() {
        return lyricLRC;
    }

    /**
     * @return Lyric in JSON form. <p>
     * Like {"songStatus":1,"lyricVersion":4,"lyric":" ...
     */
    public String getLyricJSON() {
        return lyricJSON;
    }

    /**
     * @return Music file URL. <p>
     * Like "<a href="http://m801.music.126.net/20230607155828/b4dced120af947e146682fe58e506a51/jdymusic/obj/wo3DlMOGwrbDjj7DisKw/16774938352/8b6a/88fe/cfd2/775e1eea92991daa4d10bba9be4c5de6.mp3">...</a>
     */
    public String getMusicURL() {
        return musicURL;
    }

    /**
     * @return HTTP response code <p>
     * like 200(HTTP_OK)
     */
    public int getMusicResponseCode() {
        return musicResponseCode;
    }

    /**
     * @return JSON value <p>
     * {"code":200}
     */
    public int getLyricStatueCode() {
        return lyricStatueCode;
    }

    /**
     * @return JSON value <p>
     * Example: {"songStatus":1}
     */
    public int getLyricSongStatus() {
        return lyricSongStatus;
    }

    /**
     * @return JSON value <p>
     * Example: {"lyricVersion":4}
     */
    public int getLyricVersion() {
        return lyricVersion;
    }

    /**
     * @return If this song is a VIP song, it will be true; <p> Otherwise, it will be false;
     */
    public boolean getAvailable() {
        return isAvailable;
    }

    /**
     * @param fileName File name like "file.mp3"
     * @throws IOException Exception
     */
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

    /**
     * @param fileName File name like "file.mp3"
     * @throws IOException Exception
     */
    public void downloadLRC(String fileName) throws IOException {
        FileUtils.writeStringToFile(new File(fileName), lyricLRC, StandardCharsets.UTF_8);
    }


    public void connect(String userAgent) {
        try {
            me.reqMusicURL(userAgent);
            if (!isAvailable) {
                me.reqLyric(userAgent);
                me.reqTitle(userAgent);
            }
        } catch (Exception e) {
            Logger.log("err", "ERROR!");
            e.printStackTrace();
        }
    }

    /**
     * @param userAgent User-agent
     * @throws Exception Exception
     */
    public void reqMusicURL(String userAgent) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://music.163.com/song/media/outer/url?id=" + musicID).openConnection();
        connection.setRequestProperty("User-Agent", userAgent);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.connect();
        musicResponseCode = connection.getResponseCode();
        if (musicResponseCode == HttpURLConnection.HTTP_MOVED_TEMP || musicResponseCode == HttpURLConnection.HTTP_MOVED_PERM) {
            musicURL = connection.getHeaderField("Location");
            isAvailable = musicURL.equals("http://music.163.com/404");
        } else isAvailable = false;
        connection.disconnect();
    }

    /**
     * Get Lyric in both LRC and JSON form.<p>
     * Values are in lyricJSON & lyricLRC<p>
     * Target: http://music.163.com/api/song/media?id=
     *
     * @param userAgent User-agent
     * @throws Exception Exception
     */
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

    /**
     * Get webpage's title to get song's name & singer<p>
     * Target: https://music.163.com/#/song?id=
     *
     * @param userAgent User-agent
     */
    public void reqTitle(String userAgent) {
        try {
            URL webpageUrl = new URL("https://music.163.com/#/song?id=" + musicID);
            URLConnection connection = webpageUrl.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            String htmlContent = stringBuilder.toString();
            extractTitle(htmlContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void extractTitle(String htmlContent) {
        musicTitle = "";
        int titleStartIndex = htmlContent.indexOf("<title>");
        int titleEndIndex = htmlContent.indexOf("</title>");
        if (titleStartIndex != -1 && titleEndIndex != -1) {
            String titleString = htmlContent.substring(titleStartIndex + 7, titleEndIndex);
            JSONObject jsonTitle = JSONObject.parseObject(titleString);
            musicTitle = jsonTitle.getString("title");
        }
    }

//    public static void main(String[] args) {
//        CloudMusic a= new CloudMusic(1941629582);
//        a.reqTitle(RandomAgent.createRandomUSERAGENTS());
//        System.out.println(musicTitle);
//    }
}
