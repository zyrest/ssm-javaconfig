package samson.common.util;

import java.io.File;
import java.net.URL;

/**
 * Created by 96428 on 2017/7/26.
 * This in ssmjavaconfig, samson.common.util
 */
public class ClassPathUtil {

    public static File getResourceFile(String fileName) {
        URL url = ClassPathUtil.class.getClassLoader().getResource(fileName);

        if (url != null) {
            String targetFile = url.getPath();
            return new File(targetFile);
        }

        return null;
    }
}
