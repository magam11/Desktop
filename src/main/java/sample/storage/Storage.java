package sample.storage;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.prefs.Preferences;

public class Storage {
    private static Storage ourInstance = new Storage();
    private Preferences preferences = Preferences.userRoot().node("image_manager");
    private static final String TOKEN = "TOKEN";

    private Storage() {
    }

    public static Storage getInstance() {
        return ourInstance;
    }

    public String getToken() {
        return preferences.get(TOKEN,"");
    }

    public void setToken(String token){
        preferences.remove(TOKEN);
        preferences.put(TOKEN,token);
    }
}
