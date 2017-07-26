package samson.core.shiro.token;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.stereotype.Component;
import samson.common.po.User;
import samson.common.util.PasswordUtil;
import samson.core.shiro.token.exception.WrongPasswordException;
import samson.user.service.RolePermissionAuthService;
import samson.user.service.UserRoleAuthService;
import samson.user.service.UserService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * Created by 96428 on 2017/7/23.
 * This in ssmjavaconfig, samson.core.shiro.token
 */
@Component
public class MineRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    @Resource
    private UserRoleAuthService userRoleAuthService;

    @Resource
    private RolePermissionAuthService rolePermissionAuthService;

    /**
     *  认证信息，用于用户登录，
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken) throws AuthenticationException {

        ShiroToken token = (ShiroToken) authenticationToken;
        User user = userService.getUserByName(token.getUsername());

        if (user == null) {
            throw new AccountException("用户不存在！");
        } else {
            if (!PasswordUtil.validatePassword(token.getPassletters(), user.getPassword())) {
                throw new WrongPasswordException("密码错误！");
            } else if (Objects.equals(user.getStatus(), User.FORBIT)) {
                throw new DisabledAccountException("用户被禁止登陆");
            } else {
                user.setLastLoginTime(new Date());
                userService.updateByPrimaryKeySelective(user);
            }
        }

        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        return null;
    }


    /**
     * 清空当前用户权限信息
     */
    public  void clearCachedAuthorizationInfo() {

        PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
        SimplePrincipalCollection principals = new SimplePrincipalCollection(
                principalCollection, getName());
        super.clearCachedAuthorizationInfo(principals);
    }
    /**
     * 指定principalCollection 清除
     */
    public void clearCachedAuthorizationInfo(PrincipalCollection principalCollection) {

        SimplePrincipalCollection principals = new SimplePrincipalCollection(
                principalCollection, getName());
        super.clearCachedAuthorizationInfo(principals);
    }
}
