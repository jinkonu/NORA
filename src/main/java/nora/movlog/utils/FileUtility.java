package nora.movlog.utils;

public class FileUtility {

    private static final String rootPath = System.getProperty("user.dir");
    private static final String fileDirection = rootPath + "/src/main/resources/static/img/";

    public static String getFullPath(String fileName) {
        return fileDirection + fileName;
    }
}
