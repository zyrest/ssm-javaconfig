package samson.core.shiro.token;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import samson.common.util.PasswordUtil;

/**
 * Created by 96428 on 2017/7/30.
 * This in ssmjavaconfig, samson.core.shiro.token
 */

public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        ShiroToken shiroToken = (ShiroToken) token;

        String encodePassword = info.getCredentials().toString();
        String password = shiroToken.getPassletters();

        return PasswordUtil.validatePassword(password, encodePassword);
    }
}
