package samson.common.statics;

/**
 * Created by 96428 on 2017/7/25.
 * This in ssmjavaconfig, samson.common.statics
 */
public class ResponseStatus {

    public static final Integer SUCCESS = 200;
    public static final Integer IN_PROCESS_LINE = 201;
    public static final Integer RECIEVE = 202;
    public static final Integer NULL_RESPONSE = 203;

    public static final Integer RESOURCE_NOT_FOUND = 40001;
    public static final Integer WRONG_TOKEN = 40002;
    public static final Integer TOKEN_EXPIRE = 40003;
    public static final Integer NO_PERMISSION = 40004;
    public static final Integer NO_SUCH_USER = 40005;
    public static final Integer WRONG_URL = 40006;
    public static final Integer WRONG_FILE_TYPE = 40007;
    public static final Integer WRONG_FILE_SIZE = 40008;
    public static final Integer UNVALID_PARAM = 40009;
    public static final Integer UNVALID_HEADER = 40010;

    public static final Integer SYSTEM_ERROE = 500;
}
