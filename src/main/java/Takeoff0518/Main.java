package Takeoff0518;

import Takeoff0518.Utils.Downloader;
import Takeoff0518.Utils.RandomAgent;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.print("id=");
        Scanner scanner = new Scanner(System.in);
        String url = "http://music.163.com/song/media/outer/url?id=" + scanner.nextInt();

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        String userAgent = RandomAgent.createRandomUSERAGENTS();
        connection.setRequestProperty("User-Agent", userAgent);
        System.out.println("User-Agent=\"" + userAgent + "\"");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
            String locationUrl = connection.getHeaderField("Location");
            if (Objects.equals(locationUrl, "http://music.163.com/404")) {
                System.out.println("Redirect successfully, but URL=\"http://music.163.com/404\".\nMake sure it isn't a VIP song.");
            } else {
                System.out.println("Redirect successfully,URL=\"" + locationUrl + "\"");
                System.out.println("Download Started!");
                Downloader.downloadMP3(locationUrl);
                System.out.println("Download finished!");
            }
        } else if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("No redirect.");
        } else {
            System.out.println("Failed, code=" + responseCode + ".");
        }
        connection.disconnect();
    }
}