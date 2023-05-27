package Takeoff0518;

import Takeoff0518.Utils.Downloader;
import Takeoff0518.Utils.GetLyrics;
import Takeoff0518.Utils.GetRedirectUrl;
import Takeoff0518.Utils.RandomAgent;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.print("id=");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        String url = "http://music.163.com/song/media/outer/url?id=" + id;
        System.out.println(url);
        String locationUrl = GetRedirectUrl.getUrl(url, RandomAgent.createRandomUSERAGENTS());

        if (Objects.equals(locationUrl, "null")) { //没有重定向
            System.out.println("No redirect.");
        } else if (Objects.equals(locationUrl, "http://music.163.com/404")) { //付费
            System.out.println("Redirect successfully, but URL=\"http://music.163.com/404\".\nMake sure it isn't a VIP song.");
        } else if (url.charAt(0) != 'h') { //其他原因
            System.out.println("Failed, code=" + Integer.getInteger(locationUrl) + ".");
        } else { //成功
            System.out.println("Redirect successfully,URL=\"" + locationUrl + "\"");
            System.out.println("Do you wang to download it? (Y/N)");
            scanner = new Scanner(System.in);
            String opt = scanner.next();
            if (Objects.equals(opt, "Y") || Objects.equals(opt, "y")) {
                System.out.println("Download Started!");
                System.out.println("Downloading MP3...");
                Downloader.downloadMP3(locationUrl, "file.mp3");
                System.out.println("Downloading LRC...");
                Downloader.downloadLRC(GetLyrics.toLRCStr(GetLyrics.getLyrics("http://music.163.com/api/song/media?id=" + id)), "file.lrc");
                System.out.println("Download finished!");
            }
        }

    }
}