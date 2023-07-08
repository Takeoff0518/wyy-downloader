package Takeoff0518;

import Takeoff0518.LiteLogger.Logger;
import Takeoff0518.Song.CloudMusic;
import Takeoff0518.Utils.RandomAgent;

import java.net.HttpURLConnection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        Logger.log("info", "Please input Music WebSite URL: ");
        Scanner scanner = new Scanner(System.in);
        String webURL = scanner.next();
        int id;
        Pattern pattern = Pattern.compile("id=(\\d+)");
        Matcher matcher = pattern.matcher(webURL);
        if (matcher.find()) {
            id = Integer.parseInt(matcher.group(1));
            Logger.log("info", "Success! ID = " + id);
        } else {
            Logger.log("warn", "Error while decoding. Please input Music ID:");
            scanner = new Scanner(System.in);
            id = scanner.nextInt();
        }

//        int id = scanner.nextInt();
        CloudMusic song = new CloudMusic(id);
        song.connect(RandomAgent.createRandomUSERAGENTS());
        if (song.getMusicResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP ||
                song.getMusicResponseCode() == HttpURLConnection.HTTP_MOVED_PERM) {
            if (song.getAvailable()) {
                Logger.log("warn", "Redirect successfully, but URL=\"http://music.163.com/404\".");
                Logger.log("warn", "Make sure it isn't a VIP song or a empty song.");
            } else {
                Logger.log("info", "Redirect successfully!");
                Logger.log("info", "Song URL = \"" + song.getMusicURL() + '\"');
                Logger.log("info", "Lyric = \"\n" + song.getLyricLRC() + '\"');
            }
        } else if (song.getMusicResponseCode() == HttpURLConnection.HTTP_OK) {
            Logger.log("warn", "It seems that this song isn't available!(HTTP_OK)");
        } else {
            Logger.log("err", "Unexpected response code! Get code" + song.getMusicResponseCode());
        }
    }
}