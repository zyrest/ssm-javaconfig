package samson.user.service;

import org.springframework.stereotype.Service;
import samson.common.dao.RolePermissionAuthMapper;
import samson.common.po.RolePermissionAuth;

import javax.annotation.Resource;

/**
 * Created by 96428 on 2017/7/25.
 * This in ssmjavaconfig, samson.user.service
 */
@Service
public class RolePermissionAuthService {
    @Resource
    private RolePermissionAuthMapper rolePermissionAuthDao;


}
