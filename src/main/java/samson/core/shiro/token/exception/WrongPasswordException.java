package samson.core.shiro.token.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by 96428 on 2017/7/23.
 * This in ssmjavaconfig, samson.core.shiro.token.exception
 */
public class WrongPasswordException extends AuthenticationException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
