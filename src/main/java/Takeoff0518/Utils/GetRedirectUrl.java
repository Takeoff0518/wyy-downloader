package Takeoff0518.Utils;

import java.net.HttpURLConnection;
import java.net.URL;

//import java.util.Objects;
public class GetRedirectUrl {
    /**
     * @param url       URL
     * @param userAgent UserAgent
     * @return Redirected URL
     * @throws Exception Exception
     */
    public static String getUrl(String url, String userAgent) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
//        System.out.println("User-Agent=\"" + userAgent + "\"");
        connection.setRequestProperty("User-Agent", userAgent);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        String returnVal;
        if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
            returnVal = connection.getHeaderField("Location");
        } else if (responseCode == HttpURLConnection.HTTP_OK) {
            returnVal = "Null";
        } else {
            returnVal = String.valueOf(responseCode);
        }
        connection.disconnect();
        return returnVal;
    }
}
