package sample.storage;


import java.util.prefs.Preferences;

public class Storage {
    private static Storage ourInstance = new Storage();
    private Preferences preferences = Preferences.userRoot().node("PhotoCloud");
    private static final String TOKEN = "TOKEN";
    private static final String CURRENT_TOKEN = "CURRENT_TOKEN";

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

    public String getCurrentToken(){
       return preferences.get(CURRENT_TOKEN,"");
    }

    public void setCurrentToken(String currentToken){
        preferences.remove(CURRENT_TOKEN);
        preferences.put(CURRENT_TOKEN,currentToken);

    }

}
