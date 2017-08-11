package samson.common.util;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by 96428 on 2017/7/30.
 * This in ssmjavaconfig, samson.common.util
 */
public class Base64Util {
    public static String decode(String src) {

        return new String(Base64.decodeBase64(src));
    }

}
