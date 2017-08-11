package samson.common.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import samson.common.po.User;
import samson.common.vo.MessageVo;
import samson.core.shiro.token.manager.TokenManager;
import samson.user.service.UserService;

import javax.annotation.Resource;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.common.controller
 */
@RestController
public class BaseController {

    @Resource
    private UserService userService;

    @RequestMapping("/index")
    public String getIndex() {
        return "Hello SSM on javaconfig";
    }

    @RequestMapping("/login")
    public MessageVo login(@RequestParam("userName") String userName,
                           @RequestParam("password") String password) {

        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        TokenManager.login(user, false);

        return new MessageVo().setStatus(200);
    }

    @RequestMapping("/register")
    public MessageVo register(@RequestParam("userName") String userName,
                              @RequestParam("password") String password) {

        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        userService.register(user);

        return new MessageVo().setStatus(200);
    }
}
