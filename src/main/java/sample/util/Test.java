package sample.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class Test {

    public static void main(String[] args) {

        isInternetReachable();
    }
    public static boolean isInternetReachable()
    {
        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
            Object objData = urlConnect.getContent();
            urlConnect.disconnect();
            System.out.println("SUCCESSFUL INTERNET CONNECTION");


        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block

            System.out.println("CONNECTION FAILED");
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("CONNECTION FAILED");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
