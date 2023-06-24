package Takeoff0518;

import Takeoff0518.LiteLogger.Logger;
import Takeoff0518.Song.CloudMusic;
import Takeoff0518.Utils.RandomAgent;

import java.net.HttpURLConnection;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Logger.log("info", "Please input Music ID: ");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        CloudMusic song = new CloudMusic(id);
        song.connect(RandomAgent.createRandomUSERAGENTS());
        if (song.getMusicResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP ||
                song.getMusicResponseCode() == HttpURLConnection.HTTP_MOVED_PERM) {
            if (song.getAvailable()) {
                Logger.log("warn", "Redirect successfully, but URL=\"http://music.163.com/404\".");
                Logger.log("warn", "Make sure it isn't a VIP song or a empty song.");
            } else {
                Logger.log("info", "Redirect successfully,URL=\"" + song.getMusicURL() + "\"");
                Logger.log("info", "Do you wang to download it? (Y/N)");
                scanner = new Scanner(System.in);
                String opt = scanner.next();
                if (Objects.equals(opt, "Y") || Objects.equals(opt, "y")) {
                    Logger.log("info", "Download Started!");
                    Logger.log("info", "Downloading MP3...");
                    song.downloadMusic("file.mp3");
                    Logger.log("info", "Done.");
                    Logger.log("info", "Downloading LRC...");
                    song.downloadLRC("file.lrc");
                    Logger.log("info", "Done.");
                    Logger.log("info", "Download finished!");
                }
            }
        } else if (song.getMusicResponseCode() == HttpURLConnection.HTTP_OK) {
            Logger.log("warn", "It seems that this song isn't available!(HTTP_OK)");
        } else {
            Logger.log("err", "Unexpected response code! Get code" + song.getMusicResponseCode());
        }
    }
}