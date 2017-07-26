package samson.common.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Set;

/**
 * Created by 96428 on 2017/7/26.
 * This in ssmjavaconfig, samson.common.util
 */
public class PropertiesUtil {

    public static LinkedHashMap<String, String> getPropertiesContent(String path) {
        return getPropertiesContent(new File(path));
    }

    public static LinkedHashMap<String, String> getPropertiesContent(File file) {
        LinkedHashMap<String, String> ans = new LinkedHashMap<>();
        InputStream in = null;
        try {
            in = FileUtils.openInputStream(file);
            Properties pro = new Properties();
            pro.load(in);
            Set<String> keys = pro.stringPropertyNames();
            for (String key : keys) {
                ans.put(key, pro.getProperty(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return ans;
    }
}
