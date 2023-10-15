package Takeoff0518;

import Takeoff0518.Song.CloudMusic;
import Takeoff0518.Utils.RandomAgent;
import org.apache.commons.cli.*;

import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("u", "url", true, "Music URL");
        options.addOption("i", "id", true, "Music ID");
        options.addOption("h", "help", false, "Help");
        options.addOption("d", "download", false, "Download Music and LRC");
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine = commandLineParser.parse(options, args);
        if (commandLine.hasOption("h")) {
            System.out.println("Usage:");
            System.out.println("java -jar wyy-downloader.jar [flags]");
            System.out.println("java -jar wyy-downloader.jar [flags] [arguments]");
            System.out.println("Available Commands:");
            System.out.println("-h, --help            Show help.");
            System.out.println("-i, --id [Music ID]   Get song via Music ID");
            System.out.println("-u, --url [Music URL] Get song via Music URL");
            System.exit(0);
        }
        int id = 0;
        // fetch ID
        if (commandLine.hasOption("u")) {
            String webURL = commandLine.getOptionValue("u");
            Pattern pattern = Pattern.compile("id=(\\\\d+)");
            Matcher matcher = pattern.matcher(webURL);
            if (matcher.find()) {
                id = Integer.parseInt(matcher.group(1));
            } else {
                System.out.println("Invalid URL!");
                System.exit(0);
            }
        } else if (commandLine.hasOption("i")) {
            try {
                id = Integer.parseInt(commandLine.getOptionValue("i"));
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID!");
            }
        }
        CloudMusic song = new CloudMusic(id);
        song.connect(RandomAgent.createRandomUSERAGENTS());
        if (song.getMusicResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP ||
                song.getMusicResponseCode() == HttpURLConnection.HTTP_MOVED_PERM) {
            if (song.getAvailable()) {
                System.out.println("Redirect caught exctption. Make sure it isn't a VIP song or a empty song.");
                System.exit(0);
            } else {
                System.out.println("Redirect successfully!");
                System.out.println("Lyric = \"\n" + song.getLyricLRC() + '\"');
                System.out.println("Song URL = \"" + song.getMusicURL() + '\"');
            }
        } else if (song.getMusicResponseCode() == HttpURLConnection.HTTP_OK) {
            System.out.println("Redirect failed!");
        } else {
            System.out.println("Unexpected response code! (" + song.getMusicResponseCode() + ')');
        }
        // fetch download
        if (commandLine.hasOption("d")) {
            System.out.println("This feature will be completed some days.");
        }
    }
}