package samson.user.service;

import org.springframework.stereotype.Service;
import samson.common.dao.UserMapper;
import samson.common.po.User;

import javax.annotation.Resource;

/**
 * Created by 96428 on 2017/7/23.
 * This in ssmjavaconfig, samson.user.service
 */
@Service
public class UserService {
    @Resource
    private UserMapper userDao;

    public User getUserByName(String userName) {

        return userDao.selectByName(userName);
    }

    public void updateByPrimaryKeySelective(User user) {

        userDao.updateByPrimaryKeySelective(user);
    }
}
