package Takeoff0518;

import Takeoff0518.Utils.Downloader;
import Takeoff0518.Utils.GetRedirectUrl;
import Takeoff0518.Utils.RandomAgent;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.print("id=");
        Scanner scanner = new Scanner(System.in);
        String url = "http://music.163.com/song/media/outer/url?id=" + scanner.nextInt();
        String locationUrl = GetRedirectUrl.getUrl(url, RandomAgent.createRandomUSERAGENTS());

        if (Objects.equals(locationUrl, "Null")) {
            System.out.println("No redirect.");
        } else if (Objects.equals(locationUrl, "http://music.163.com/404")) {
            System.out.println("Redirect successfully, but URL=\"http://music.163.com/404\".\nMake sure it isn't a VIP song.");
        } else if (isInteger(locationUrl)) {
            System.out.println("Failed, code=" + Integer.getInteger(locationUrl) + ".");
        } else {
            System.out.println("Redirect successfully,URL=\"" + locationUrl + "\"");
            System.out.println("Do you wang to download it? (Y/N)");
            scanner = new Scanner(System.in);
            String opt = scanner.next();
            if (Objects.equals(opt, "Y") || Objects.equals(opt, "y")) {
                System.out.println("Download Started!");
                Downloader.downloadMP3(locationUrl, "file.mp3");
                System.out.println("Download finished!");
            }
        }

    }

    public static boolean isInteger(String val) {
        try {
            int value = Integer.getInteger(val);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }
}