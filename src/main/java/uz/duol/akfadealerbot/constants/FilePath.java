package uz.duol.akfadealerbot.constants;

import java.io.File;

public class FilePath {

    private final static String UPLOAD_PATH = System.getProperty("user.dir");

    //    private final static String UPLOAD_PATH = "/files";

    public static final String PARENT_PATH = UPLOAD_PATH + File.separator;

    public final String IMAGE_PATH  = PARENT_PATH + "/images/";
    public final String FONT_PATH          = PARENT_PATH + "/fonts/";

}