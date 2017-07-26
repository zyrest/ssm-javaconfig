package samson.common.util;

import com.alibaba.fastjson.JSON;

import java.io.*;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.common.util
 */
@SuppressWarnings("unchecked")
public class SerializeUtil {
    static final Class<?> CLAZZ = SerializeUtil.class;

    /**
     * 序列化对象 返回byte
     * @param object 对象
     * @return       byte数组
     */
    public static byte[] serialize(Object object) {
        if (object == null) throw new NullPointerException("不能进行序列化！对象为null");

        byte[] result = null;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);

            oos.flush();
            oos.close();
            baos.flush();
            baos.close();

            result = baos.toByteArray();
        } catch (IOException e) {
            LoggerUtil.fmtDebug(CLAZZ, "序列化对象出现错误：%s", JSON.toJSONString(object));
            e.printStackTrace();
        } finally {
            close(oos);
            close(baos);
        }

        return result;
    }

    /**
     * 反序列化 得到Object对象
     * @param from   byte数组
     * @return       Object
     */
    public static Object deserialize(byte[] from) { return deserialize(from, Object.class); }
    /**
     * 反序列化 得到Object对象
     * @param from   byte数组
     * @param tClass 对象的class
     * @return       Object
     */
    public static <T> T deserialize(byte[] from, Class<T> ... tClass) {
        Object result = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;

        try {
            if (from != null) {
                bais = new ByteArrayInputStream(from);
                ois = new ObjectInputStream(bais);
                result = ois.readObject();
            }
        } catch (Exception e) {
            LoggerUtil.fmtError(CLAZZ, e, "反序列化失败 ： %s", from);
        } finally {
            close(ois);
            close(bais);
        }
        return (T) result;
    }

    private static void close(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException e) {
            LoggerUtil.error(CLAZZ, "关闭io流出现错误", e);
        }
    }
}
