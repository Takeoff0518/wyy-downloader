package Takeoff0518.LiteLogger;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    public static void log(String level, String log) {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        String header = switch (level) {
            case "info" -> '[' + formattedTime + " INFO]: ";
            case "warn" -> '[' + formattedTime + " WARN]: ";
            case "err" -> '[' + formattedTime + " ERROR]: ";
            default -> '[' + formattedTime + ' ' + level + "]: ";
        };
        if (level.equals("warn") || level.equals("err")) System.err.println(header + log);
        else System.out.println(header + log);
    }
}
