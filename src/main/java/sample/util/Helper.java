package sample.util;

import java.io.File;

public class Helper {
    private static Helper ourInstance = new Helper();

    public static Helper getInstance() {
        return ourInstance;
    }

    private Helper() {
    }

    public String operationSystem() {
        String os = "";
        String operationSysstemName = System.getProperty("os.name").toLowerCase();
        String userName = System.getProperty("user.name");
        if (operationSysstemName.contains("win")) { //WINDOWS
            os = "WINDOWS";
        } else if (operationSysstemName.contains("nix") || operationSysstemName.contains("nux") || operationSysstemName.contains("aix")) { //LINUX
            os = "LINUX";
        } else if (operationSysstemName.contains("mac")) { //MAC
            os = "MAX";
        } else if (operationSysstemName.contains("sunos")) { //SOLARIS
            os = "SOLARIS";
        }
        return os;
    }
    public String decideDirectionPath() {
        String home = System.getProperty("user.home");
        String os_name = operationSystem();
        String dirPath = "";
        if (os_name.equals("WINDOWS")) {
            File file = new File(home + "/Downloads/Foto_Cloud");
            if (!file.exists()) {
                file.mkdirs();
            }
            dirPath = home + "/Downloads/Foto_Cloud/";
        }
        if (os_name.equals("LINUX")) {
            File file = new File(home + "\\Downloads\\Foto_Cloud");
            if (!file.exists()) {
                file.mkdirs();
            }
            dirPath = home + "\\Downloads\\Foto_Cloud\\";
        }
        return dirPath;
    }
}
