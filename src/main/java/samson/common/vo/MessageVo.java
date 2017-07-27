package samson.common.vo;

import java.util.Date;

/**
 * Created by 96428 on 2017/7/27.
 * This in ssmjavaconfig, samson.common.vo
 */
public class MessageVo {
    private Integer status;
    private String message;
    private Object data;
    private Date createTime;

    public Integer getStatus() {
        return status;
    }

    public MessageVo setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public MessageVo setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public MessageVo setData(Object data) {
        this.data = data;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public MessageVo setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
}
