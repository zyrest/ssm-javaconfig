package samson.common.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 96428 on 2017/7/22.
 * This in ssmjavaconfig, samson.common.util
 */
public class ArrayUtil {
    public static <T> Set<T> arrays2Set(T[] ts) {
        Set<T> res = new HashSet<>();

        res.addAll(Arrays.asList(ts));

        return res;
    }
}
