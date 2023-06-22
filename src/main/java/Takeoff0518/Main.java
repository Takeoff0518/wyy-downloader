package Takeoff0518;

import Takeoff0518.Song.CloudMusic;
import Takeoff0518.Utils.RandomAgent;

import java.net.HttpURLConnection;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.print("id=");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        CloudMusic song = new CloudMusic(id);
        song.connect(RandomAgent.createRandomUSERAGENTS());
//        song.reqMusicURL(RandomAgent.createRandomUSERAGENTS());
        if (song.getMusicResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP ||
                song.getMusicResponseCode() == HttpURLConnection.HTTP_MOVED_PERM) {
            if (Objects.equals(song.getMusicURL(), "http://music.163.com/404")) {
                System.out.println("Redirect successfully, but URL=\"http://music.163.com/404\".\nMake sure it isn't a VIP song.");
            } else {
                System.out.println("Redirect successfully,URL=\"" + song.getMusicURL() + "\"");
                System.out.println("Do you wang to download it? (Y/N)");
                scanner = new Scanner(System.in);
                String opt = scanner.next();
                if (Objects.equals(opt, "Y") || Objects.equals(opt, "y")) {
//                    song.reqLyric(RandomAgent.createRandomUSERAGENTS());
                    System.out.println("Download Started!");
                    System.out.println("Downloading MP3...");
                    song.downloadMusic("file.mp3");
                    System.out.println("Downloading LRC...");
                    song.downloadLRC("file.lrc");
                    System.out.println("Download finished!");
                }
            }
        } else if (song.getMusicResponseCode() == HttpURLConnection.HTTP_OK) {
            System.out.println("No redirect.");
        } else {
            System.out.println("Failed, code=" + song.getMusicResponseCode());
        }
    }
}