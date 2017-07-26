package samson.core.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 由于父类UsernamePasswordToken中password为char[]类型
 * 我们继承他后对其重写
 * Created by 96428 on 2017/7/23.
 * This in ssmjavaconfig, samson.core.shiro.token
 */
public class ShiroToken extends UsernamePasswordToken implements java.io.Serializable{

    private static final long serialVersionUID = -6451794657814516274L;

    public ShiroToken(String username, String passletters) {
        super(username, passletters);
        this.passletters = passletters ;
    }


    /** 登录密码[字符串类型] 因为父类是char[] ] **/
    private String passletters;

    public String getPassletters() {
        return passletters;
    }

    public void setPassletters(String passletters) {
        this.passletters = passletters;
    }
}
