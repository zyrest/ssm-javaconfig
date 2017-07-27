package samson.core.shiro.session;

import java.io.Serializable;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.shiro.session
 */
public class SessionStatus implements Serializable {

    /**
     * 是否，true有效，false踢出
     */
    private Boolean onlineStatus = true;

    public Boolean isOnlineStatus() {
        return onlineStatus;
    }

    public Boolean getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}
