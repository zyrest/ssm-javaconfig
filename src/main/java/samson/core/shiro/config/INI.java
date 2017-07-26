package samson.core.shiro.config;

import samson.common.util.LoggerUtil;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 96428 on 2017/7/26.
 * This in ssmjavaconfig, samson.core.shiro.config
 */
public class INI {
    private final String encoding = "UTF-8";

    private final LinkedHashMap<String , LinkedHashMap<String, String>> core = new LinkedHashMap<>();

    String currentSection;

    public INI(String filePath) throws FileNotFoundException {

        this(new File(filePath));
    }

    public INI(File file) throws FileNotFoundException {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), encoding));

            this.init(reader);
        } catch (UnsupportedEncodingException e) {
            LoggerUtil.fmtError(getClass(), e,
                    "请检查%s中的encoding属性%s", getClass().getName(), encoding);
        }
    }

    private void init(BufferedReader reader) {
        try {
            read(reader);
        } catch (IOException e) {
            LoggerUtil.fmtError(getClass(), e, "读取错误");
        }
    }

    private void read(BufferedReader reader) throws IOException {
        String line = null;
        while ((line = reader.readLine()) != null) {
            parseLine(line);
        }
    }

    private void parseLine(String line) {
        line = line.trim();

        if (line.matches("^#.*$")) {
            return;
        } else if (line.matches("^\\[(\\S+)]$")){
            String section = line.replaceFirst("^\\[(\\S+)]$", "$1");
            addSection(section);
        } else if (line.matches("^\\S+=.*$")) {
            int i = line.indexOf("=");
            String key = line.substring(0, i);
            String value = line.substring(i+1);
            addKeyValue(currentSection, key, value);
        }
    }

    private void addSection(String section) {
        if (core.containsKey(section)) return;
        currentSection = section;
        core.put(section, new LinkedHashMap<>());
    }

    private void addKeyValue(String section, String key, String value) {
        if (core.containsKey(section)) {
            core.get(section).put(key, value);
        }
    }

    public String getValue(String section, String key) {
        if (core.containsKey(section)) {
            return core.get(section).getOrDefault(key, null);
        }
        return null;
    }

    public Map<String, String> get(String section) {
        if (core.containsKey(section)) {
            return core.get(section);
        }

        return null;
    }
}
