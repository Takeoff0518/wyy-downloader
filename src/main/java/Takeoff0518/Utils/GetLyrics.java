package Takeoff0518.Utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetLyrics {
    /**
     * @param url ApiUrl
     * @return Lyrics on json
     */
    public static String getLyrics(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
//            StringBuffer content = new StringBuffer();
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();
            return content.toString();
        } catch (Exception e) {
//            System.out.println("Crashed");
            e.printStackTrace();
            return "null";
        }
    }

    /**
     * @param jsonStr Lyrics on json
     * @return Lyrics on String(LRC)
     */
    public static String toLRCStr(String jsonStr) {
        byte[] bytes = jsonStr.getBytes();
        JSONObject data = JSON.parseObject(bytes);
        int statusCode = data.getInteger("code");
        if (statusCode == 200) {
            return data.getString("lyric");
        } else return "null";
    }
//    public static void main(String[] args) {
//        System.out.println(toLRCStr(getLyrics("http://music.163.com/api/song/media?id=863046037")));
//    }
}
